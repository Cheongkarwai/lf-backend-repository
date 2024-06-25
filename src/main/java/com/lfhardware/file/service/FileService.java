package com.lfhardware.file.service;

import com.lfhardware.file.dto.ImageDTO;
import com.lfhardware.order.domain.OrderDetails;
import com.lfhardware.order.dto.OrderDetailsDTO;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FileService<I,O> {

    Mono<byte[]> exportOrders(List<OrderDetailsDTO> orders);

    Mono<byte[]> createFile(Map<String, Object> content);


    O upload(I input);


    O uploadServiceProviderDocuments(I input);

    O uploadCompleteAppointmentEvidences(UUID id, Long serviceId, String serviceProviderId, String customerId, LocalDateTime createdAt,
                                         I input);
}
