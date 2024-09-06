package com.lfhardware.faq.api;

import com.lfhardware.faq.dto.FaqDTO;
import com.lfhardware.faq.service.IFaqService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FaqApi {

    private final IFaqService faqService;

    public FaqApi(IFaqService faqService) {
        this.faqService = faqService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FaqDTO.class)
                .flatMap(faqService::save)
                .flatMap(faqDTO -> ServerResponse.ok().bodyValue(faqDTO));
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok().body(faqService.findAll(), FaqDTO.class);
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        return faqService.deleteById(Long.valueOf(serverRequest.pathVariable("id")))
                .then(Mono.defer(() -> ServerResponse.noContent().build()));
    }

    public Mono<ServerResponse> updateById(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FaqDTO.class)
                .flatMap(faq -> faqService.updateById(Long.valueOf(serverRequest.pathVariable("id")), faq)
                        .then(Mono.defer(() -> ServerResponse.noContent().build())));
    }
}
