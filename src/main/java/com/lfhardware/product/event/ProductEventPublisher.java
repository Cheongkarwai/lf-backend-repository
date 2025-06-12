package com.lfhardware.product.event;

import com.lfhardware.event.TopicName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

import java.util.UUID;

@Service
@Slf4j
public class ProductEventPublisher {

    private final ReactiveKafkaProducerTemplate<String, ProductEvent> reactiveKafkaProducerTemplate;

    public ProductEventPublisher(ReactiveKafkaProducerTemplate<String, ProductEvent> reactiveKafkaProducerTemplate) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
    }


    public Mono<SenderResult<Void>> publishProductCreatedEvent(ProductEvent productEvent) {
        return reactiveKafkaProducerTemplate.send(TopicName.PRODUCT, UUID.randomUUID().toString(), productEvent)
                .doOnNext(senderResult-> log.info("Topic {} Partition {} Offset {} Exception {}", senderResult.recordMetadata().topic(), senderResult.recordMetadata().partition(), senderResult.recordMetadata().offset(), senderResult.exception() != null ? senderResult.exception().getMessage(): "No error"))
                .doOnError(error-> log.error("Kafka producer error {}", error.getMessage(), error));
    }


}
