package com.lfhardware.form.api;


import com.lfhardware.form.domain.FormId;
import com.lfhardware.form.dto.FormDTO;
import com.lfhardware.form.dto.FormInput;
import com.lfhardware.form.dto.FormPageRequest;
import com.lfhardware.form.service.IFormService;
import com.lfhardware.shared.PageInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class FormApi {

    private final IFormService formService;

    public FormApi(IFormService formService) {
        this.formService = formService;
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest){

        FormPageRequest formPageRequest = new FormPageRequest();

        Optional<String> pageOptional = serverRequest.queryParam("page");
        Optional<String> pageSizeOptional = serverRequest.queryParam("page_size");
        Optional<String> serviceProviderIdOptional = serverRequest.queryParam("service_provider_id");
        Optional<String> serviceId = serverRequest.queryParam("service_id");

        formPageRequest.setPage(Integer.parseInt(pageOptional.orElseGet(()-> String.valueOf(0))));
        formPageRequest.setPageSize(Integer.parseInt(pageSizeOptional.orElseGet(()-> String.valueOf(10))));
        serviceProviderIdOptional.ifPresent(id -> formPageRequest.setServiceProviderId(Long.valueOf(id)));
        serviceId.ifPresent(id-> formPageRequest.setServiceId(Long.valueOf(id)));

        return ServerResponse.ok().body(formService.findAll(formPageRequest), FormDTO.class);
    }
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FormInput.class)
                .flatMap(formService::save)
                .onErrorResume(e -> ServerResponse.badRequest().build().then())
                .then(Mono.defer(() -> ServerResponse.noContent().build()));
    }

    public Mono<ServerResponse> findByFormId(ServerRequest serverRequest) {

        String serviceProviderId = serverRequest.pathVariable("service_provider_id");
        Long serviceId = Long.valueOf(serverRequest.pathVariable("service_id"));

        return formService.findById(new FormId(serviceProviderId, serviceId))
                .flatMap(form -> ServerResponse.ok().bodyValue(form))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
