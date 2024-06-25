package com.lfhardware.stock.repository;

import com.lfhardware.shared.CrudRepository;
import com.lfhardware.stock.domain.Size;
import com.lfhardware.stock.domain.Stock;
import org.hibernate.reactive.stage.Stage;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

public interface IStockRepository extends CrudRepository<Stock,Long> {

    CompletionStage<Stock> findByProductIdAndSize(Stage.Session session, Long productId, Size size);
}
