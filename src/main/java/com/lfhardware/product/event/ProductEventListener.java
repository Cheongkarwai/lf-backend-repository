package com.lfhardware.product.event;

import com.lfhardware.keycloak.KeycloakUmaAdapter;
import com.lfhardware.authorization.policy.factory.PolicyRepresentationFactory;
import com.lfhardware.authorization.resource.factory.ResourceRepresentationFactory;
import com.lfhardware.authorization.policy.service.IPolicyService;
import com.lfhardware.authorization.resource.service.IResourceService;
import com.lfhardware.product.dto.ProductDTO;
import com.lfhardware.product.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverOffset;

import java.util.Set;
import java.util.function.Consumer;

@Service
@Slf4j
public class ProductEventListener {

    private final IProductService productService;

    private final IResourceService resourceService;

    private final IPolicyService policyService;

    private final ResourceRepresentationFactory<ProductDTO> productResourceRepresentationFactory;

    private final PolicyRepresentationFactory<String> userPolicyRepresentationFactory;

    private final KeycloakUmaAdapter keycloakUmaAdapter;

    public ProductEventListener(IProductService productService,
                                IResourceService resourceService,
                                IPolicyService policyService,
                                @Qualifier("productResourceRepresentationFactory") ResourceRepresentationFactory<ProductDTO> productResourceRepresentationFactory,
                                @Qualifier("userPolicyRepresentationFactory") PolicyRepresentationFactory<String> userPolicyRepresentationFactory,
                                KeycloakUmaAdapter keycloakUmaAdapter) {
        this.productService = productService;
        this.resourceService = resourceService;
        this.policyService = policyService;
        this.productResourceRepresentationFactory = productResourceRepresentationFactory;
        this.userPolicyRepresentationFactory = userPolicyRepresentationFactory;
        this.keycloakUmaAdapter = keycloakUmaAdapter;
    }

    @Bean
    public Consumer<Flux<Message<ProductEvent>>> productCreatedInput() {
        return message -> {
            message.flatMap(productInputMessage -> Mono.just(productInputMessage.getPayload())
                    .flatMap(productEvent -> resourceService.create(productResourceRepresentationFactory.createResourceRepresentation(productEvent.getJwt(), productEvent.getProductDTO(), Set.of("product:read", "product:write")))
                            .onErrorResume(err -> {
                                log.error("Error when creating product resource id = {}", productEvent.getProductDTO().getId());
                                return productService.rollback(productEvent.getProductDTO().getId());
                            })
                            .then(Mono.defer(() -> {
                                AbstractPolicyRepresentation userPolicy =
                                        userPolicyRepresentationFactory.createPolicyRepresentation(String.valueOf(productEvent.getProductDTO().getId()),
                                                Set.of("product:write", "product:read"), "Allow access to owner", "Owner access", Set.of(productEvent.getJwt().getSubject()));
                                return keycloakUmaAdapter.createUmaPolicy(productEvent.getJwt().getTokenValue(), String.valueOf(productEvent.getProductDTO().getId()), userPolicy)
                                        .then()
                                        .onErrorResume(err -> {
                                            log.error("Error when creating user policy for resource id = {}, user id = {}", productEvent.getProductDTO().getId(), productEvent.getJwt().getSubject());
                                            return resourceService.delete(String.valueOf(productEvent.getProductDTO().getId()))
                                                    .then(Mono.defer(() -> productService.rollback(productEvent.getProductDTO().getId())));
                                        });
                            }))
                    )
                    .doFinally(productDTO -> {
                        ReceiverOffset receiverOffset = productInputMessage.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, ReceiverOffset.class);
                        if (receiverOffset != null) {
                            receiverOffset.acknowledge();
                        }
                    })).subscribe();
        };
    }

}
