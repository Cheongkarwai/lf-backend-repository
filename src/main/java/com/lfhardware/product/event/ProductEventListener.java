package com.lfhardware.product.event;

import com.lfhardware.authorization.policy.factory.PolicyRepresentationFactory;
import com.lfhardware.authorization.resource.factory.ResourceRepresentationFactory;
import com.lfhardware.authorization.resource.service.IResourceService;
import com.lfhardware.keycloak.KeycloakUmaAdapter;
import com.lfhardware.product.repository.IProductRepository;
import com.lfhardware.product.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.mutiny.Mutiny;
import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class ProductEventListener {

    private final Mutiny.SessionFactory sessionFactory;

    private final IProductRepository productRepository;

    private final IProductService productService;

    private final IResourceService resourceService;

    private final ResourceRepresentationFactory productResourceRepresentationFactory;

    private final PolicyRepresentationFactory<String> userPolicyRepresentationFactory;

    private final KeycloakUmaAdapter keycloakUmaAdapter;

    public ProductEventListener(Mutiny.SessionFactory sessionFactory,
                                IProductRepository productRepository,
                                IProductService productService,
                                IResourceService resourceService,
                                @Qualifier("productResourceRepresentationFactory") ResourceRepresentationFactory productResourceRepresentationFactory,
                                @Qualifier("userPolicyRepresentationFactory") PolicyRepresentationFactory<String> userPolicyRepresentationFactory,
                                KeycloakUmaAdapter keycloakUmaAdapter) {
        this.sessionFactory = sessionFactory;
        this.productRepository = productRepository;
        this.productService = productService;
        this.resourceService = resourceService;
        this.productResourceRepresentationFactory = productResourceRepresentationFactory;
        this.userPolicyRepresentationFactory = userPolicyRepresentationFactory;
        this.keycloakUmaAdapter = keycloakUmaAdapter;
    }

//    @Bean
//    public Consumer<Flux<Message<ProductEvent>>> productInput() {
//        return message -> {
//            message.flatMap(this::processProductEvent)
//                    .doOnError(error -> log.error("Error processing product input message: {}", error.getMessage(), error))
//                    .subscribe();
//        };
//    }
//
//    public Mono<Void> processProductEvent(Message<ProductEvent> productEventMessage){
//        ProductEvent productEvent = productEventMessage.getPayload();
//        log.info("Consuming product event : {}", productEvent);
//        Mono<Void> eventProcessingResult = switch (productEvent.getEventType()) {
//            case CREATED -> publishCreatedEvent(productEvent);
//            case DELETED -> publishDeletedEvent(productEvent);
//            default -> {
//                log.warn("Unknown event type: {}", productEvent.getEventType());
//                yield Mono.empty(); // No action for unknown event types
//            }
//        };
//        return eventProcessingResult
//                .doOnTerminate(() -> acknowledgeMessage(productEventMessage));
//    }
//
//    private void acknowledgeMessage(Message<ProductEvent> productMessage) {
//        ReceiverOffset receiverOffset = productMessage
//                .getHeaders()
//                .get(KafkaHeaders.ACKNOWLEDGMENT, ReceiverOffset.class);
//
//        if (receiverOffset != null) {
//            receiverOffset.acknowledge();
//            log.debug("Message acknowledged successfully");
//        } else {
//            log.warn("ReceiverOffset for message acknowledgment is null!");
//        }
//    }

    public void createProductResource(ProductCreatedEvent productCreatedEvent) {
        log.info("Handle");
        resourceService.create(productResourceRepresentationFactory.createResourceRepresentation(productCreatedEvent.getJwt(), productCreatedEvent.getId(), productCreatedEvent.getName(), Set.of("product:read", "product:write")))
                .onErrorResume(err -> {
                    log.error("Error when creating product resource id = {}", productCreatedEvent.getId());
                    return productService.rollback(UUID.fromString(productCreatedEvent.getId()));
                })
                .then(Mono.defer(() -> {
                    AbstractPolicyRepresentation userPolicy =
                            userPolicyRepresentationFactory.createPolicyRepresentation(String.valueOf(productCreatedEvent.getId()),
                                    Set.of("product:write", "product:read"), "Allow access to owner", "Owner " + productCreatedEvent.getJwt().getTokenValue() + " access", Set.of(productCreatedEvent.getJwt().getSubject()));
                    return keycloakUmaAdapter.createUmaPolicy(productCreatedEvent.getJwt().getTokenValue(), String.valueOf(productCreatedEvent.getId()), userPolicy)
                            .then()
                            .onErrorResume(err -> {
                                log.error("Error when creating user policy for resource id = {}, user id = {}", productCreatedEvent.getId(), productCreatedEvent.getJwt().getSubject());
                                return resourceService.delete(productCreatedEvent.getId())
                                        .then(Mono.defer(() -> productService.rollback(UUID.fromString(productCreatedEvent.getId()))));
                            });
                }));
    }


    private Mono<Void> publishDeletedEvent(ProductEvent productEvent) {
        return resourceService.delete(String.valueOf(productEvent.getProductDTO().getId()))
                .then(Mono.defer(() -> keycloakUmaAdapter.deleteUmaPolicy(productEvent.getJwt().getTokenValue(), String.valueOf(productEvent.getProductDTO().getId()))
                        .then()));
    }

}
