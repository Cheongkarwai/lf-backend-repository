package com.lfhardware.product.event;

import com.lfhardware.event.TopicName;
import com.lfhardware.product.dto.ProductDTO;
import com.lfhardware.product.dto.ProductInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductEventPublisher {

    private final StreamBridge streamBridge;

    public ProductEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }


    public void publishProductCreatedEvent(ProductEvent productDTO) {
        log.info("publishProductCreatedEvent");
        this.streamBridge.setAsync(true);
        this.streamBridge.send(TopicName.PRODUCT_CREATED_OUTPUT, MessageBuilder.withPayload(productDTO).build());
    }


}
