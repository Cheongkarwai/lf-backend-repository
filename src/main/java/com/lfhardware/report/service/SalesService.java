package com.lfhardware.report.service;

import com.lfhardware.order.domain.PaymentStatus;
import com.lfhardware.order.repository.IOrderRepository;
import com.lfhardware.report.dto.MonthlySalesStat;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SalesService implements ISalesService{

    private IOrderRepository orderRepository;

    private Stage.SessionFactory sessionFactory;

    public SalesService(IOrderRepository orderRepository, Stage.SessionFactory sessionFactory){
        this.orderRepository = orderRepository;
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Mono<List<MonthlySalesStat>> getFullYearSales() {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> {
            return orderRepository.findAllByPaymentStatusGroupByMonth(session, PaymentStatus.PAID);
        })).doOnSuccess((e)-> System.out.println("Success"))
                .doOnError(e-> System.out.println(e.getMessage()));
    }
}
