package com.lfhardware.review.repository;

import com.lfhardware.order.domain.Order;
import com.lfhardware.order.domain.Order_;
import com.lfhardware.review.domain.Review;
import com.lfhardware.shared.PageInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class ReviewRepository implements IReviewRepository{

    private final Stage.SessionFactory sessionFactory;
    public ReviewRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletionStage<List<Review>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<Review> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Review obj) {
        return null;
    }

    @Override
    public CompletionStage<List<Review>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Review> merge(Stage.Session session, Review obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public Review loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Review> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Review> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Review obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<List<Review>> findAll(Stage.Session session, PageInfo pageInfo) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Review> cq = criteriaBuilder.createQuery(Review.class);
        Root<Review> root = cq.from(Review.class);

        List<Predicate> predicates = new ArrayList<>();

//        if(StringUtils.hasText(pageInfo.getSearch())){
//            predicates.add(criteriaBuilder.or(
//                    criteriaBuilder.like(root.get(Order_.ID).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.SUBTOTAL).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.TOTAL).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.SHIPPING_FEES).as(String.class),"%"+pageInfo.getSearch()+"%"),
//                    criteriaBuilder.like(root.get(Order_.PAYMENT_STATUS).as(String.class),"%"+pageInfo.getSearch()+"%")
//            ));
//        }

        cq.select(root).where(predicates.toArray(Predicate[]::new));

        return session.createQuery(cq).setFirstResult(pageInfo.getPage() * pageInfo.getPageSize())
                .setMaxResults(pageInfo.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> count(Stage.Session session, PageInfo pageInfo) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Review> root = cq.from(Review.class);

        List<Predicate> predicates = new ArrayList<>();

        cq.select(criteriaBuilder.count(root)).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq).getSingleResult();
    }
}
