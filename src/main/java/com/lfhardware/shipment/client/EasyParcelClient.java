package com.lfhardware.shipment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.shipment.service.CreateOrderService;
import com.lfhardware.shipment.service.CreditBalanceService;
import com.lfhardware.shipment.service.PayOrderService;
import com.lfhardware.shipment.service.RateCheckingService;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor
public class EasyParcelClient {

    private String baseUrl;

   private String apiKey;

   private WebClient webClient;

   private ObjectMapper objectMapper;

   public EasyParcelClient(String url, String apiKey) {
       this.baseUrl = url;
       this.apiKey = apiKey;
       final int size = 16 * 1024 * 1024;
       final ExchangeStrategies strategies = ExchangeStrategies.builder()
               .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size)).build();
       this.webClient = WebClient.builder()
               .exchangeStrategies(strategies)
               .baseUrl(baseUrl)
               .build();
       this.objectMapper = new ObjectMapper();
   }

   public PayOrderService payOrder(){
       return new PayOrderService(this.webClient, apiKey, objectMapper);
   }

   public CreditBalanceService creditBalance(){
       return new CreditBalanceService(this.webClient, apiKey);
   }

   public RateCheckingService rateChecking(){
       return new RateCheckingService(this.webClient, apiKey, objectMapper);
   }

   public CreateOrderService order(){
       return new CreateOrderService(this.webClient, apiKey, objectMapper);
   }
}
