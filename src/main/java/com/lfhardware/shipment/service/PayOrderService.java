package com.lfhardware.shipment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.shipment.dto.order.OrderBulkDTO;
import com.lfhardware.shipment.dto.order.OrderInput;
import com.lfhardware.shipment.dto.order.OrderResultDTO;
import com.lfhardware.shipment.dto.payment.PaymentResponse;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class PayOrderService {

    private WebClient webClient;

    private String apiKey;

    private ObjectMapper objectMapper;

    public Mono<PaymentResponse> payOrder(List<String> orderNumbers){


        MultiValueMap<String, String> formMap = new LinkedMultiValueMap<>();
        formMap.add("api", this.apiKey);

        for(int i = 0;i < orderNumbers.size(); i++){
            formMap.add("bulk["+i+"][order_no]", orderNumbers.get(i));
        }

        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder.queryParam("ac", "EPPayOrderBulk").build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                .body(BodyInserters.fromFormData(formMap))
                .retrieve()
                //.onStatus(httpStatusCode -> httpStatusCode.isError(), clientResponse -> clientResponse.createError())
                .bodyToMono(PaymentResponse.class);
    }
}
