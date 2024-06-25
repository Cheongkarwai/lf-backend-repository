package com.lfhardware.shipment.service;

import com.lfhardware.shipment.dto.BalanceDTO;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CreditBalanceService {


    private WebClient webClient;

    private String apiKey;


    public Mono<BalanceDTO> getBalanceAsync() {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder.queryParam("ac", "EPCheckCreditBalance").build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                .body(BodyInserters.fromFormData("api", apiKey))
                .retrieve()
                .bodyToMono(BalanceDTO.class);
    };
}
