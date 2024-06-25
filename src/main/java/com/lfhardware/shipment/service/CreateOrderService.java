package com.lfhardware.shipment.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.shipment.dto.BalanceDTO;
import com.lfhardware.shipment.dto.BulkDTO;
import com.lfhardware.shipment.dto.order.OrderBulkDTO;
import com.lfhardware.shipment.dto.order.OrderDTO;
import com.lfhardware.shipment.dto.order.OrderInput;
import com.lfhardware.shipment.dto.order.OrderResultDTO;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@AllArgsConstructor
public class CreateOrderService {

    private WebClient webClient;

    private String apiKey;

    private ObjectMapper objectMapper;

    public Mono<OrderResultDTO> createOrder(OrderInput orderInput){


        MultiValueMap<String, String> formMap = new LinkedMultiValueMap<>();
        formMap.add("api", this.apiKey);
        for(int i = 0;i < orderInput.getBulk().size(); i++){
            OrderBulkDTO bulkDTO = orderInput.getBulk().get(i);
            formMap.add("bulk["+i+"][content]", bulkDTO.getContent());
            formMap.add("bulk["+i+"][value]", String.valueOf(bulkDTO.getValue()));
            formMap.add("bulk["+i+"][weight]", String.valueOf(bulkDTO.getWeight()));
            formMap.add("bulk["+i+"][width]", String.valueOf(bulkDTO.getWidth()));
            formMap.add("bulk["+i+"][height]", String.valueOf(bulkDTO.getHeight()));
            formMap.add("bulk["+i+"][length]", String.valueOf(bulkDTO.getLength()));
            formMap.add("bulk["+i+"][service_id]", bulkDTO.getServiceId());
            formMap.add("bulk["+i+"][pick_point]", bulkDTO.getPickPoint());
            formMap.add("bulk["+i+"][pick_name]", bulkDTO.getPickName());
            formMap.add("bulk["+i+"][pick_email]", bulkDTO.getPickEmail());
            formMap.add("bulk["+i+"][pick_company]", bulkDTO.getPickCompany());
            formMap.add("bulk["+i+"][pick_contact]", bulkDTO.getPickContact());
            formMap.add("bulk["+i+"][pick_mobile]", bulkDTO.getPickMobile());
            formMap.add("bulk["+i+"][pick_addr1]", bulkDTO.getPickAddress1());
            formMap.add("bulk["+i+"][pick_addr2]", bulkDTO.getPickAddress2());
            formMap.add("bulk["+i+"][pick_addr3]", bulkDTO.getPickAddress3());
            formMap.add("bulk["+i+"][pick_addr4]", bulkDTO.getPickAddress4());
            formMap.add("bulk["+i+"][pick_city]", bulkDTO.getPickCity());
            formMap.add("bulk["+i+"][pick_state]", bulkDTO.getPickState());
            formMap.add("bulk["+i+"][pick_code]", bulkDTO.getPickCode());
            formMap.add("bulk["+i+"][pick_country]", bulkDTO.getPickCountry());
            formMap.add("bulk["+i+"][send_point]", bulkDTO.getSendPoint());
            formMap.add("bulk["+i+"][send_name]", bulkDTO.getSendName());
            formMap.add("bulk["+i+"][send_email]", bulkDTO.getSendEmail());
            formMap.add("bulk["+i+"][send_company]", bulkDTO.getSendCompany());
            formMap.add("bulk["+i+"][send_contact]", bulkDTO.getSendContact());
            formMap.add("bulk["+i+"][send_mobile]", bulkDTO.getSendMobile());
            formMap.add("bulk["+i+"][send_addr1]", bulkDTO.getSendAddress1());
            formMap.add("bulk["+i+"][send_addr2]", bulkDTO.getSendAddress2());
            formMap.add("bulk["+i+"][send_addr3]", bulkDTO.getSendAddress3());
            formMap.add("bulk["+i+"][send_addr4]", bulkDTO.getSendAddress4());
            formMap.add("bulk["+i+"][send_city]", bulkDTO.getSendCity());
            formMap.add("bulk["+i+"][send_state]", bulkDTO.getSendState());
            formMap.add("bulk["+i+"][send_code]", bulkDTO.getSendCode());
            formMap.add("bulk["+i+"][send_country]", bulkDTO.getSendCountry());

            if(Objects.nonNull(bulkDTO.getCollectDate())){
                formMap.add("bulk["+i+"][collect_date]", bulkDTO.getCollectDate().toString());
            }

        }

        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder.queryParam("ac", "EPSubmitOrderBulk").build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                .body(BodyInserters.fromFormData(formMap))
                .retrieve()
                //.onStatus(httpStatusCode -> httpStatusCode.isError(), clientResponse -> clientResponse.createError())
                .bodyToMono(OrderResultDTO.class);
    }
}
