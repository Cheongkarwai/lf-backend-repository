package com.lfhardware.provider_business.api;

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

    public ProviderBusinessHandler(IProviderBusinessService providerBusinessService){
        this.providerBusinessService = providerBusinessService;
    }

    public Mono<ServerResponse> findAllServices(ServerRequest serverRequest){
        return ServerResponse.ok().body(providerBusinessService.findAllServiceDetails(), ServiceGroupByCategoryDTO.class);
    }

    public Mono<ServerResponse> findDetailsById(ServerRequest serverRequest){
        return ServerResponse.ok().body(providerBusinessService.findDetailsById(Long.valueOf(serverRequest.pathVariable("id"))), ServiceDetailsDTO.class);
    }
}
