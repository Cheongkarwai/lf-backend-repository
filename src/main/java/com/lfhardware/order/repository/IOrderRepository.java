package com.lfhardware.order.repository;

import com.lfhardware.order.domain.Order;
import com.lfhardware.order.domain.PaymentStatus;
import com.lfhardware.order.dto.DailyOrderStat;
import com.lfhardware.order.dto.OrderPageRequest;
import com.lfhardware.report.dto.MonthlySalesStat;
import com.lfhardware.shared.CrudRepository;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface IOrderRepository extends CrudRepository<Order,Long> {
    CompletionStage<List<Order>> findAllByUsername(Stage.Session session,OrderPageRequest pageInfo, String username);

    CompletionStage<List<Order>> findAll(Stage.Session session, OrderPageRequest pageInfo);

    CompletionStage<Long> count(Stage.Session session, OrderPageRequest pageInfo);
    CompletionStage<Long> count(Stage.Session session, OrderPageRequest pageInfo, String username);


    CompletionStage<Order> findByIdAndUsername(Stage.Session session, Long id, String username);

    CompletionStage<List<Order>> findAllOrdersProduct(Stage.Session session, OrderPageRequest orderPageRequest, String username);

    CompletionStage<Long> countOrdersProduct(Stage.Session session,OrderPageRequest orderPageRequest, String username);

    CompletionStage<List<MonthlySalesStat>> findAllByPaymentStatusGroupByMonth(Stage.Session session, PaymentStatus paymentStatus);

    CompletionStage<List<DailyOrderStat>> countDailyOrderGroupByDays(Stage.Session session, Integer days);

    CompletionStage<List<DailyOrderStat>>  countDailySalesGroupByDays(Stage.Session session, Integer days);
}
