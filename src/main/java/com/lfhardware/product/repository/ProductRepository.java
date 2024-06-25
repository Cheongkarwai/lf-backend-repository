package com.lfhardware.product.repository;


import com.lfhardware.product.domain.*;
import com.lfhardware.product.dto.ProductPageRequest;
import com.lfhardware.shared.SortOrder;
import com.lfhardware.stock.domain.Stock;
import com.lfhardware.stock.domain.Stock_;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

@Repository
@Slf4j
public class ProductRepository implements IProductRepository {

    private Stage.SessionFactory sessionFactory;

    public ProductRepository(Stage.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public CompletionStage<List<Product>> findAll(Stage.Session session, ProductPageRequest pageInfo) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Product> cq = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        root.fetch(Product_.BRAND, JoinType.LEFT);
        root.fetch(Product_.CATEGORY, JoinType.LEFT);
        root.fetch(Product_.PRODUCT_IMAGES, JoinType.LEFT);
        root.fetch(Product_.STOCKS, JoinType.LEFT);
        root.fetch(Product_.REVIEWS, JoinType.LEFT);

        //Sorting
        if (pageInfo.getSort().getName() != null && pageInfo.getSort().getOrder() != null) {
            if (pageInfo.getSort().getOrder().equals(SortOrder.ASC)) {
                cq.orderBy(criteriaBuilder.asc(root.get(pageInfo.getSort().getName())));
            } else {
                cq.orderBy(criteriaBuilder.desc(root.get(pageInfo.getSort().getName())));
            }
        }

        //Search
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(pageInfo.getSearch())) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get(Product_.ID).as(String.class), "%" + pageInfo.getSearch() + "%"),
                    criteriaBuilder.like(root.get(Product_.NAME).as(String.class), "%" + pageInfo.getSearch() + "%"),
                    criteriaBuilder.like(root.get(Product_.DESCRIPTION), "%" + pageInfo.getSearch() + "%")
            ));
        }

        //Filter by categories
        if (!pageInfo.getCategoryIds().isEmpty()) {
            log.info("Category ids are not empty");
            Path<Object> path = root.get(Product_.CATEGORY).get(Category_.ID);
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (Long categoryId : pageInfo.getCategoryIds()) {
                in.value(categoryId);
            }
            predicates.add(in);
        }

        if (!pageInfo.getBrandIds().isEmpty()) {
            Path<Object> path = root.get(Product_.BRAND).get(Brand_.ID);
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (Long brandId : pageInfo.getBrandIds()) {
                in.value(brandId);
            }
            predicates.add(in);
        }

        //Create a left join when quantity is specified
        if (pageInfo.getMinQuantity() != null) {
            Join<Product, Stock> join = root.join(Product_.STOCKS, JoinType.LEFT);
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(join.get(Stock_.QUANTITY), pageInfo.getMinQuantity()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        cq.select(root);
        return session.createQuery(cq)
                .setFirstResult(pageInfo.getPage() * pageInfo.getPageSize())
                .setMaxResults(pageInfo.getPageSize())
                .getResultList();


    }

    @Override
    public CompletionStage<Long> count(Stage.Session session, ProductPageRequest pageInfo) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Product> root = cq.from(Product.class);
        root.join(Product_.BRAND, JoinType.LEFT);
        root.join(Product_.CATEGORY, JoinType.LEFT);
        root.join(Product_.PRODUCT_IMAGES, JoinType.LEFT);
        root.join(Product_.STOCKS, JoinType.LEFT);

        //Search
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(pageInfo.getSearch())) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get(Product_.ID).as(String.class), "%" + pageInfo.getSearch() + "%"),
                    criteriaBuilder.like(root.get(Product_.NAME).as(String.class), "%" + pageInfo.getSearch() + "%")
            ));
        }

        if (!pageInfo.getCategoryIds().isEmpty()) {
            log.info("Category ids are not empty");
            Path<Object> path = root.get(Product_.CATEGORY).get(Category_.ID);
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (Long categoryId : pageInfo.getCategoryIds()) {
                in.value(categoryId);
            }
            predicates.add(in);
        }

        if (!pageInfo.getBrandIds().isEmpty()) {
            Path<Object> path = root.get(Product_.BRAND).get(Brand_.ID);
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (Long brandId : pageInfo.getBrandIds()) {
                in.value(brandId);
            }
            predicates.add(in);
        }

        if (pageInfo.getMinQuantity() != null) {
            Join<Product, Stock> join = root.join(Product_.STOCKS, JoinType.LEFT);
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(join.get(Stock_.QUANTITY), pageInfo.getMinQuantity()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.select(criteriaBuilder.countDistinct(root));

        return session.createQuery(cq)
                .getSingleResult();
    }


//    @Override
//    public CompletionStage<List<Product>> findAllRightJoinStock(Stage.Session session, ProductPageRequest pageInfo) {
//        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
//        CriteriaQuery<Product> cq = criteriaBuilder.createQuery(Product.class);
//        Root<Product> root = cq.from(Product.class);
//        Join<Product, Stock> stock = root.join(Product_.STOCKS, JoinType.LEFT);
//
//
//        //Sorting
//        if(pageInfo.getSort().getName() != null && pageInfo.getSort().getOrder() != null){
//            if(pageInfo.getSort().getOrder().equals(SortOrder.ASC)){
//                cq.orderBy(criteriaBuilder.asc(stock.get(pageInfo.getSort().getName())));
//            }else{
//                cq.orderBy(criteriaBuilder.desc(stock.get(pageInfo.getSort().getName())));
//            }
//        }
//
//        //Search
//        List<Predicate> predicates = new ArrayList<>();
//        if(pageInfo.getSearch().length() > 0){
//            predicates.add(criteriaBuilder.or(
//                    criteriaBuilder.like(stock.get(Product_.ID).as(String.class),pageInfo.getSearch()),
//                    criteriaBuilder.like(stock.get(Product_.NAME).as(String.class),pageInfo.getSearch()),
//                    criteriaBuilder.like(stock.get(Product_.DESCRIPTION),pageInfo.getSearch())
//            ));
//        }
//
//        //Filter by categories
//        if(!pageInfo.getCategoryIds().isEmpty()){
//            log.info("Category ids are not empty");
//            Path<Object> path = stock.get(Product_.CATEGORY).get(Category_.ID);
//            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
//            for(Long categoryId : pageInfo.getCategoryIds()){
//                in.value(categoryId);
//            }
//            predicates.add(in);
//        }
//
//        if(!pageInfo.getBrandIds().isEmpty()){
//            Path<Object> path = stock.get(Product_.BRAND).get(Brand_.ID);
//            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
//            for(Long brandId : pageInfo.getBrandIds()){
//                in.value(brandId);
//            }
//            predicates.add(in);
//        }
//
//        cq.where(predicates.toArray(new Predicate[0]));
//        cq.select(root).distinct(true);
//        return session.createQuery(cq).setFirstResult(pageInfo.getPage() * pageInfo.getPageSize())
//                .setPlan(session.getEntityGraph(Product.class,"ProductDetails"))
//                .setMaxResults(pageInfo.getPageSize())
//                .getResultList();
//
//    }
//
//    @Override
//    public CompletionStage<Long> countRightJoinStock(Stage.Session session, ProductPageRequest pageInfo){
//        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
//        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
//        Root<Product> root = cq.from(Product.class);
//
//        //Search
//        List<Predicate> predicates = new ArrayList<>();
//
//        if(pageInfo.getSearch().length() > 0){
//            predicates.add(criteriaBuilder.or(
//                    criteriaBuilder.like(root.get(Product_.ID).as(String.class),pageInfo.getSearch()),
//                    criteriaBuilder.like(root.get(Product_.NAME).as(String.class),pageInfo.getSearch())
//            ));
//        }
//
//        if(!pageInfo.getCategoryIds().isEmpty()){
//            log.info("Category ids are not empty");
//            Path<Object> path = root.get(Product_.CATEGORY).get(Category_.ID);
//            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
//            for(Long categoryId : pageInfo.getCategoryIds()){
//                in.value(categoryId);
//            }
//            predicates.add(in);
//        }
//
//        if(!pageInfo.getBrandIds().isEmpty()){
//            Path<Object> path = root.get(Product_.BRAND).get(Brand_.ID);
//            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
//            for(Long brandId : pageInfo.getBrandIds()){
//                in.value(brandId);
//            }
//            predicates.add(in);
//        }
//
//        cq.where(predicates.toArray(new Predicate[0]));
//        cq.select(criteriaBuilder.count(root));
//
//        return session.createQuery(cq).getSingleResult();
//    }


    @Override
    public CompletionStage<List<Product>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<Product> findById(Stage.Session session, Long id) {
        return session.find(session.getEntityGraph(Product.class, "ProductDetails"), id);
    }

    @Override
    public CompletionStage<Product> findByName(Stage.Session session, String name) {
        return session.createNamedQuery("Product.findByName", Product.class)
                .setParameter("name", name)
                .setPlan(session.getEntityGraph(Product.class, "ProductDetails"))
                .getSingleResultOrNull();
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Product obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<Product>> findAllByIds(Stage.Session session, List<Long> ids) {
        return null;
    }

    @Override
    public CompletionStage<Product> merge(Stage.Session session, Product obj) {
        return session.merge(obj);
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long id) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaDelete<Product> cd = cb.createCriteriaDelete(Product.class);
        Root<Product> root = cd.from(Product.class);
        cd.where(cb.equal(root.get(Product_.ID), id));
        return session.createQuery(cd)
                .executeUpdate().thenAccept(e-> System.out.println("Hi"));
    }


    @Override
    public Product loadReferenceById(Stage.Session session, Long id) {
        return session.getReference(Product.class, id);
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Product> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Product> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Product obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
