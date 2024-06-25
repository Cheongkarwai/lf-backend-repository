package com.lfhardware.stock.service;

import com.lfhardware.cart.domain.CartDetails;
import com.lfhardware.order.dto.OrderItemInput;
import com.lfhardware.stock.domain.Stock;
import com.lfhardware.stock.repository.IStockRepository;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Service
public class StockService implements  IStockService{

    public final Stage.SessionFactory sessionFactory;

    private final IStockRepository stockRepository;

    public StockService(Stage.SessionFactory sessionFactory, IStockRepository stockRepository){
        this.sessionFactory = sessionFactory;
        this.stockRepository = stockRepository;
    }

   // @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public CompletionStage<Void> updateStockQuantityByOrderItems(List<OrderItemInput> orderItemInputs) {
        return sessionFactory.withTransaction((session, transaction) ->stockRepository.findAllByIds(session,orderItemInputs.stream().map(item->item.stockId()).collect(Collectors.toList()))
                .thenCompose(stocks->{
                    for(Stock stock : stocks){
                        OrderItemInput orderItemInput = orderItemInputs.parallelStream().filter(item -> item.stockId() == stock.getId()).findFirst().orElseThrow();
                        stock.setQuantity(stock.getQuantity() - orderItemInput.quantity());
                    }
                    return stockRepository.saveAll(session,stocks);
                }));
    }
}
