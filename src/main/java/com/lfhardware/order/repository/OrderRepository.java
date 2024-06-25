package com.lfhardware.order.repository;

import com.lfhardware.auth.dto.DailyUserStat;
import com.lfhardware.order.domain.Order;
import com.lfhardware.order.domain.OrderDetails_;
import com.lfhardware.order.domain.Order_;
import com.lfhardware.order.domain.PaymentStatus;
import com.lfhardware.order.dto.DailyOrderStat;
import com.lfhardware.order.dto.OrderPageRequest;
import com.lfhardware.report.dto.MonthlySalesStat;
import com.lfhardware.stock.domain.Stock_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

@Repository
public class OrderRepository implements IOrderRepository {

    private final Stage.SessionFactory sessionFactory;

    private final EntityManager entityManager;

    public OrderRepository(Stage.SessionFactory sessionFactory, EntityManager entityManager) {
        this.sessionFactory = sessionFactory;
        this.entityManager = entityManager;
    }

    @Override
    public CompletionStage<List<Order>> findAll(Stage.Session session) {
        return session.createNamedQuery("Order.findAll", Order.class)
                .setPlan(session.createEntityGraph(Order.class, "Orders"))
                .getResultList();
    }

    @Override
    public CompletionStage<Order> findById(Stage.Session session, Long id) {
        return session.createNamedQuery("Order.fetchJoinOrderDetailsFetchJoinShippingDetailsFetchJoinStockFetchJoinProduct", Order.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Order obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<Order>> findAllByIds(Stage.Session session, List<Long> strings) {
        return null;
    }

    @Override
    public CompletionStage<Order> merge(Stage.Session session, Order obj) {
        return session.merge(obj);
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long s) {
        return null;
    }

    @Override
    public Order loadReferenceById(Stage.Session session, Long s) {
        return session.getReference(Order.class, s);
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Order> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Order> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Order obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> strings) {
        return null;
    }

    @Override
    public CompletionStage<List<Order>> findAllByUsername(Stage.Session session, OrderPageRequest pageInfo, String username) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Order> cq = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();

        if (pageInfo.getDeliveryStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get(Order_.DELIVERY_STATUS), pageInfo.getDeliveryStatus()));
        }
        predicates.add(criteriaBuilder.equal(root.get(Order_.USERNAME), username));

        cq.select(root).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq).setFirstResult(pageInfo.getPage() * pageInfo.getPageSize())
                .setPlan(session.getEntityGraph(Order.class, "Orders"))
                .setMaxResults(pageInfo.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<List<Order>> findAll(Stage.Session session, OrderPageRequest pageInfo) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Order> cq = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();

        if(Objects.nonNull(pageInfo.getSearch())){
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get(Order_.ID).as(String.class),"%"+pageInfo.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Order_.SUBTOTAL).as(String.class),"%"+pageInfo.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Order_.TOTAL).as(String.class),"%"+pageInfo.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Order_.SHIPPING_FEES).as(String.class),"%"+pageInfo.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Order_.PAYMENT_STATUS).as(String.class),"%"+pageInfo.getSearch()+"%")
                    ));
        }

        if (pageInfo.getDeliveryStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get(Order_.DELIVERY_STATUS), pageInfo.getDeliveryStatus()));
        }
        cq.select(root).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq).setFirstResult(pageInfo.getPage() * pageInfo.getPageSize())
                .setPlan(session.getEntityGraph(Order.class, "Orders"))
                .setMaxResults(pageInfo.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> count(Stage.Session session, OrderPageRequest pageInfo, String username) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = cq.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();

        if (pageInfo.getDeliveryStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get(Order_.DELIVERY_STATUS), pageInfo.getDeliveryStatus()));
        }
        predicates.add(criteriaBuilder.equal(root.get(Order_.USERNAME), username));

        cq.select(criteriaBuilder.count(root)).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public CompletionStage<Long> count(Stage.Session session, OrderPageRequest pageInfo) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = cq.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();

        if(Objects.nonNull(pageInfo.getSearch())){
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get(Order_.ID).as(String.class),"%"+pageInfo.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Order_.SUBTOTAL).as(String.class),"%"+pageInfo.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Order_.TOTAL).as(String.class),"%"+pageInfo.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Order_.SHIPPING_FEES).as(String.class),"%"+pageInfo.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Order_.PAYMENT_STATUS).as(String.class),"%"+pageInfo.getSearch()+"%")
                    ));
        }

        if (pageInfo.getDeliveryStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get(Order_.DELIVERY_STATUS), pageInfo.getDeliveryStatus()));
        }
        cq.select(criteriaBuilder.count(root)).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public CompletionStage<Order> findByIdAndUsername(Stage.Session session, Long id, String username) {
        return session.createNamedQuery("Order.fetchJoinOrderDetailsFetchJoinShippingDetailsFetchJoinStockFetchJoinProductByUsername", Order.class)
                .setParameter("id", id)
                .setParameter("username", username)
                //.setPlan(session.getEntityGraph(Order.class,"OrderDetails"))
                .getSingleResult();
    }


    @Override
    public CompletionStage<List<Order>> findAllOrdersProduct(Stage.Session session, OrderPageRequest pageRequest, String username) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Order> cq = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        root.fetch(Order_.ORDER_DETAILS, JoinType.LEFT)
                .fetch(OrderDetails_.STOCK, JoinType.LEFT)
                .fetch(Stock_.PRODUCT, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if(StringUtils.hasText(username)){
            predicates.add(criteriaBuilder.equal(root.get(Order_.username),username));
        }

        if (pageRequest.getDeliveryStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get(Order_.DELIVERY_STATUS), pageRequest.getDeliveryStatus()));
        }

        cq.select(root).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq).setFirstResult(pageRequest.getPage() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> countOrdersProduct(Stage.Session session, OrderPageRequest pageRequest, String username){
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = cq.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();

        if(StringUtils.hasText(username)){
            predicates.add(criteriaBuilder.equal(root.get(Order_.username),username));
        }

        if (pageRequest.getDeliveryStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get(Order_.DELIVERY_STATUS), pageRequest.getDeliveryStatus()));
        }
        cq.select(criteriaBuilder.count(root)).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public CompletionStage<List<MonthlySalesStat>> findAllByPaymentStatusGroupByMonth(Stage.Session session, PaymentStatus paymentStatus) {

//        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
//        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
//        Root<Order> root = criteriaQuery.from(Order.class);
//
//        //Expression<LocalDate> date = criteriaBuilder.function("EXTRACT", LocalDate.class, root.get(Order_.CREATED_AT));
//        Expression<Integer> month = criteriaBuilder.function("EXTRACT", Integer.class ,root.get(Order_.CREATED_AT));
//
//        criteriaQuery.groupBy(criteriaBuilder.truncate(cb.localDate(), TemporalUnit.DAY));
//        criteriaQuery.multiselect(month, criteriaBuilder.count(root));
//        criteriaQuery.having(criteriaBuilder.equal(root.get(Order_.PAYMENT_STATUS), paymentStatus));

        return session.createNativeQuery("SELECT EXTRACT('MONTH' FROM created_at) AS month, " +
                "COUNT(id) AS total " +
                "FROM tbl_order " +
                "WHERE payment_status = :paymentStatus " +
                "GROUP BY EXTRACT('MONTH' FROM created_at) ", Object[].class)
                .setParameter("paymentStatus",  paymentStatus.name())
                .getResultList().thenApply(objects->objects.stream().map(object-> new MonthlySalesStat(String.valueOf(object[0]), Integer.parseInt(String.valueOf(object[1])))).toList());
    }

    @Override
    public CompletionStage<List<DailyOrderStat>> countDailyOrderGroupByDays(Stage.Session session, Integer days) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime daysBefore = today.minusDays(days);

        return session.createQuery("SELECT extract(day from u.createdAt), COUNT(u) " +
                        "FROM Order u " +
                        "WHERE u.createdAt BETWEEN :startDate AND :endDate " +
                        "GROUP BY extract(day from u.createdAt)", Object[].class)
                .setParameter("startDate", daysBefore)
                .setParameter("endDate", today)
                .getResultList().thenApply(objects -> {

                    List<DailyOrderStat> dailyOrderStats = new ArrayList<>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

                    first:
                    for (LocalDateTime date = daysBefore; date.isBefore(today); date = date.plusDays(1)) {

                        second:
                        for(Object[] object : objects){
                            System.out.println(object[0] instanceof String);
                            if(String.valueOf(object[0]).equals(String.valueOf(date.getDayOfMonth()))){
                                dailyOrderStats.add(new DailyOrderStat(date.toLocalDate().format(formatter), Integer.parseInt(String.valueOf(object[1]))));

                                continue first;
                            }
                        }
                        dailyOrderStats.add(new DailyOrderStat(date.toLocalDate().format(formatter), 0));
                    }
                    return dailyOrderStats;
                });
    }

    @Override
    public CompletionStage<List<DailyOrderStat>> countDailySalesGroupByDays(Stage.Session session, Integer days) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime daysBefore = today.minusDays(days);

        return session.createQuery("SELECT extract(day from u.createdAt), SUM(u.subtotal) " +
                        "FROM Order u " +
                        "WHERE u.createdAt BETWEEN :startDate AND :endDate " +
                        "GROUP BY extract(day from u.createdAt)", Object[].class)
                .setParameter("startDate", daysBefore)
                .setParameter("endDate", today)
                .getResultList().thenApply(objects -> {

                    List<DailyOrderStat> dailyOrderStats = new ArrayList<>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

                    first:
                    for (LocalDateTime date = daysBefore; date.isBefore(today); date = date.plusDays(1)) {

                        second:
                        for(Object[] object : objects){
                            System.out.println(object[0] instanceof String);
                            if(String.valueOf(object[0]).equals(String.valueOf(date.getDayOfMonth()))){
                                dailyOrderStats.add(new DailyOrderStat(date.toLocalDate().format(formatter), Integer.parseInt(String.valueOf(object[1]))));

                                continue first;
                            }
                        }
                        dailyOrderStats.add(new DailyOrderStat(date.toLocalDate().format(formatter), 0));
                    }
                    return dailyOrderStats;
                });
    }
}
