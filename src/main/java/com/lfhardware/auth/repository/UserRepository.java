package com.lfhardware.auth.repository;

import com.lfhardware.auth.domain.Profile_;
import com.lfhardware.auth.domain.User;
import com.lfhardware.auth.domain.User_;
import com.lfhardware.auth.dto.DailyUserStat;
import com.lfhardware.auth.dto.UserPageRequest;
import com.lfhardware.order.domain.Order;
import com.lfhardware.order.domain.Order_;
import com.lfhardware.report.dto.MonthlySalesStat;
import com.lfhardware.shared.SortOrder;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class UserRepository implements IUserRepository {

    private Stage.SessionFactory sessionFactory;

    public UserRepository(Stage.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletionStage<List<User>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<User> findById(Stage.Session session, String id) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<User> cq = criteriaBuilder.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root).where(criteriaBuilder.equal(root.get(User_.USERNAME), id));
        return session.createQuery(cq).setPlan(session.getEntityGraph(User.class, "user-role-graph")).getSingleResultOrNull();
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, User user) {
        return session.persist(user);
    }

    @Override
    public CompletionStage<List<User>> findAllByIds(Stage.Session session, List<String> strings) {
        return null;
    }

    @Override
    public CompletionStage<List<User>> findAll(Stage.Session session, UserPageRequest userPageRequest) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<User> cq = criteriaBuilder.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (userPageRequest.getSort().getOrder() != null && StringUtils.hasText(userPageRequest.getSort().getName())) {
            if (userPageRequest.getSort().getOrder().equals(SortOrder.ASC)) {
                cq.orderBy(criteriaBuilder.asc(root.get(userPageRequest.getSort().getName())));
            } else {
                cq.orderBy(criteriaBuilder.desc(root.get(userPageRequest.getSort().getName())));
            }
        }

//        if(StringUtils.hasText(userPageRequest.getSearch())){
//            predicates.add(criteriaBuilder.or(
//                    criteriaBuilder.like(root.get(Order_.ID).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.SUBTOTAL).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.TOTAL).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.SHIPPING_FEES).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.PAYMENT_STATUS).as(String.class),"%"+pageInfo.getSearch()+"%")
//            ));
//        }

        cq.select(root).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq).setFirstResult(userPageRequest.getPage() * userPageRequest.getPageSize())
                .setPlan(session.getEntityGraph(User.class, "user-role-graph"))
                .setMaxResults(userPageRequest.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> count(Stage.Session session, UserPageRequest userPageRequest) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<User> root = cq.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

//        if(StringUtils.hasText(userPageRequest.getSearch())){
//            predicates.add(criteriaBuilder.or(
//                    criteriaBuilder.like(root.get(Order_.ID).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.SUBTOTAL).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.TOTAL).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.SHIPPING_FEES).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.PAYMENT_STATUS).as(String.class),"%"+pageInfo.getSearch()+"%")
//            ));
//        }

        cq.select(criteriaBuilder.count(root)).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public CompletionStage<User> findUserRoleById(Stage.Session session, String username) {
        EntityGraph<User> userEntityGraph = session.getEntityGraph(User.class, "user-role-graph");
        return session.find(userEntityGraph, username);
    }

    @Override
    public CompletionStage<User> findByEmailAddress(Stage.Session session, String emailAddress) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<User> cq = criteriaBuilder.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root).where(criteriaBuilder.equal(root.get(User_.PROFILE).get(Profile_.EMAIL_ADDRESS), emailAddress));
        return session.createQuery(cq).setPlan(session.createEntityGraph(User.class, "user-role-graph")).getSingleResultOrNull();
    }

    @Override
    public CompletionStage<User> merge(Stage.Session session, User user) {
        return session.detach(user).merge(user);
    }

    @Override
    public CompletionStage<List<DailyUserStat>> countDailyUserGroupByDays(Stage.Session session, Integer days) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime daysBefore = today.minusDays(days);

        return session.createQuery("SELECT extract(day from u.createdAt), COUNT(u) " +
                        "FROM User u " +
                        "WHERE u.createdAt BETWEEN :startDate AND :endDate " +
                        "GROUP BY extract(day from u.createdAt)", Object[].class)
                .setParameter("startDate", daysBefore)
                .setParameter("endDate", today)
                .getResultList().thenApply(objects -> {

                    List<DailyUserStat> dailyUserStats = new ArrayList<>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

                    first:
                    for (LocalDateTime date = daysBefore; date.isBefore(today); date = date.plusDays(1)) {

                        second:
                        for(Object[] object : objects){
                            System.out.println(object[0] instanceof String);
                            if(String.valueOf(object[0]).equals(String.valueOf(date.getDayOfMonth()))){
                                dailyUserStats.add(new DailyUserStat(date.toLocalDate().format(formatter), Integer.parseInt(String.valueOf(object[1]))));

                                continue first;
                            }
                        }
                        dailyUserStats.add(new DailyUserStat(date.toLocalDate().format(formatter), 0));
                    }
                    return dailyUserStats;
                });
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, String s) {
        return null;
    }


    @Override
    public User loadReferenceById(Stage.Session session, String s) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<User> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<User> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, User obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<String> strings) {
        return null;
    }
}
