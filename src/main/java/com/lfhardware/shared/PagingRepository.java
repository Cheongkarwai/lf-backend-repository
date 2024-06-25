package com.lfhardware.shared;

import com.lfhardware.order.domain.Order;
import com.lfhardware.order.dto.OrderPageRequest;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface PagingRepository<T> {

    CompletionStage<List<T>> findAll(Stage.Session session, PageInfo pageInfo);

    CompletionStage<Long> count(Stage.Session session, PageInfo pageInfo);
}
