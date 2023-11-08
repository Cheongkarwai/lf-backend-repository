package com.lfhardware.product.repository;


import com.lfhardware.auth.domain.User;
import com.lfhardware.product.domain.Product;
import com.lfhardware.shared.PageInfo;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Repository
public class ProductRepository implements  IProductRepository{

    private Stage.SessionFactory sessionFactory;
    private EntityManager em;
    @PostConstruct
    public void init(){
        this.sessionFactory = Persistence.createEntityManagerFactory("postgres").unwrap(Stage.SessionFactory.class);
    }


    @Override
    public CompletionStage<List<Product>> findAll(Stage.Session session, PageInfo pageInfo) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Product> cq = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        cq.select(root);


//        //Count
//        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
//        countQuery.select(criteriaBuilder.count(root));


        return session.createQuery(cq).setFirstResult(pageInfo.getPage() * pageInfo.getPageSize())
                .setMaxResults(pageInfo.getPageSize())
                .getResultList();

    }

    @Override
    public CompletionStage<Long> count(Stage.Session session, PageInfo pageInfo){
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Product> root = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        cq.select(criteriaBuilder.count(root));

        return session.createQuery(cq).getSingleResult();
    }



    @Override
    public CompletableFuture<Product> findById(Long id) {
        return sessionFactory.withSession(session -> session.find(Product.class,id))
                .toCompletableFuture();
    }

    @Override
    public CompletionStage<Product> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, User user) {
        return null;
    }
}
