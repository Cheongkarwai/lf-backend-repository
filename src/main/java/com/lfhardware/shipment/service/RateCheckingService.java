package com.lfhardware.shipment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.shipment.dto.BulkDTO;
import com.lfhardware.shipment.dto.RateCheckingDTO;
import com.lfhardware.shipment.dto.RateCheckingInput;
import com.lfhardware.shipment.exception.RateCheckingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class RateCheckingService {

    private WebClient webClient;

    private String apiKey;

    private ObjectMapper objectMapper;

    public Mono<RateCheckingDTO> checkRate(RateCheckingInput rateCheckingInput){

        rateCheckingInput.setApi(this.apiKey);

        MultiValueMap<String, String> formMap = new LinkedMultiValueMap<>();
        formMap.add("api", this.apiKey);
        for(int i = 0;i < rateCheckingInput.getBulk().size(); i++){
            BulkDTO bulkDTO = rateCheckingInput.getBulk().get(i);
            formMap.add("bulk["+i+"][pick_code]", bulkDTO.getPickCode());
            formMap.add("bulk["+i+"][pick_state]", bulkDTO.getPickState());
            formMap.add("bulk["+i+"][pick_country]", bulkDTO.getPickCountry());
            formMap.add("bulk["+i+"][send_code]", bulkDTO.getSendCode());
            formMap.add("bulk["+i+"][send_state]", bulkDTO.getSendState());
            formMap.add("bulk["+i+"][send_country]", bulkDTO.getSendCountry());
            formMap.add("bulk["+i+"][weight]", String.valueOf(bulkDTO.getWeight()));

            if(bulkDTO.getLength() != null){
                formMap.add("bulk["+i+"][length]", String.valueOf(bulkDTO.getLength()));
            }

            if(bulkDTO.getWidth() != null){
                formMap.add("bulk["+i+"][width]", String.valueOf(bulkDTO.getWidth()));
            }

            if(bulkDTO.getHeight() != null){
                formMap.add("bulk["+i+"][height]", String.valueOf(bulkDTO.getHeight()));
            }

            if(bulkDTO.getDateCollect() != null){
                formMap.add("bulk["+i+"][date_coll]", bulkDTO.getDateCollect().toString());
            }
        }

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("ac", "EPRateCheckingBulk").build())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromFormData(formMap))
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RateCheckingException("Rate checking failed due to server error")))
                .bodyToMono(RateCheckingDTO.class);
//                .onErrorResume(e-> {
//                    System.out.println(e.getMessage());
//                    return Mono.error(e);
//                });
    }
}
