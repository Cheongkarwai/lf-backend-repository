package com.lfhardware.order.service;

import com.lfhardware.order.dto.*;
import com.lfhardware.shared.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IOrderService {

    Mono<OrderDetailsDTO> create(OrderInput orderInput);

    Mono<Pageable<OrderDTO>> findAll(OrderPageRequest pageInfo);

    Mono<Pageable<OrderProductDTO>> findAllOrdersProduct(OrderPageRequest pageRequest);

    Mono<OrderDetailsDTO> findById(Long id);

    Mono<Void> partialUpdate(Long id, Map<String,Object> map);

    Mono<byte[]> downloadInvoiceById(Long id);

    Mono<byte[]> exportCsv(Long id);

    Mono<Long> count();

    Mono<List<DailyOrderStat>> findDailyOrderCount(Integer days);

    Mono<List<DailyOrderStat>> findDailySales(Integer days);
}
