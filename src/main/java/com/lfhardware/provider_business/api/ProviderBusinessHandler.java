package com.lfhardware.provider_business.api;

import com.lfhardware.form.dto.FormDTO;
import com.lfhardware.form.dto.FormInput;
import com.lfhardware.form.service.IFormService;
import com.lfhardware.provider_business.dto.ServiceDTO;
import com.lfhardware.provider_business.dto.ServiceDetailsDTO;
import com.lfhardware.provider_business.dto.ServiceGroupByCategoryDTO;
import com.lfhardware.provider_business.service.IProviderBusinessService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProviderBusinessHandler {

    private final IProviderBusinessService providerBusinessService;

    private final IFormService formService;

    public ProviderBusinessHandler(IProviderBusinessService providerBusinessService,
                                   IFormService formService){
        this.providerBusinessService = providerBusinessService;
        this.formService = formService;
    }

    public Mono<ServerResponse> findAllServices(ServerRequest serverRequest){
        return ServerResponse.ok().body(providerBusinessService.findAllServiceDetails(), ServiceGroupByCategoryDTO.class);
    }

    public Mono<ServerResponse> findDetailsById(ServerRequest serverRequest){
        return ServerResponse.ok().body(providerBusinessService.findDetailsById(Long.valueOf(serverRequest.pathVariable("id"))), ServiceDetailsDTO.class);
    }

    public Mono<ServerResponse> findFormById(ServerRequest serverRequest){
        return ServerResponse.ok()

                .body(formService.findById(Long.valueOf(serverRequest.pathVariable("id"))),
                        FormDTO.class);
    }

    public Mono<ServerResponse> saveForm(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FormInput.class)
                .flatMap(formInput -> formService.save(Long.parseLong(serverRequest.pathVariable("id")), formInput))
                .then(Mono.defer(() -> ServerResponse.noContent()
                        .build()));
    }
}
