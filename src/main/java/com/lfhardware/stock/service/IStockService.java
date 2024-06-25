package com.lfhardware.stock.service;

import com.lfhardware.cart.domain.CartDetails;
import com.lfhardware.order.dto.OrderItemInput;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface IStockService {


    CompletionStage<Void> updateStockQuantityByOrderItems(List<OrderItemInput> orderItemInputs);
}
