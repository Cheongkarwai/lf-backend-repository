package com.lfhardware.file.service;

import com.lfhardware.order.domain.OrderDetails;
import com.lfhardware.order.dto.OrderDetailsDTO;
import com.lfhardware.order.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CSVService implements FileService<Object,Object>{


    public CSVService(){

    }

    @Override
    public Mono<byte[]> exportOrders(List<OrderDetailsDTO> orders) {


        return null;
    }

    @Override
    public Mono<byte[]> createFile(Map<String, Object> content) {
        return null;
    }

    @Override
    public Flux<Object> upload(Object input) {
        return null;
    }

    @Override
    public Object uploadServiceProviderDocuments(Object input) {
        return null;
    }

    @Override
    public Object uploadCompleteAppointmentEvidences(UUID id, Long serviceId, String serviceProviderId, String customerId, LocalDateTime createdAt, Object input) {
        return null;
    }

}
