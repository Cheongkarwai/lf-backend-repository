package com.lfhardware.product.repository;


import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Search;
import com.lfhardware.core.repository.SortOrder;
import com.lfhardware.product.domain.Brand_;
import com.lfhardware.product.domain.Category_;
import com.lfhardware.product.domain.Product;
import com.lfhardware.product.domain.Product_;
import com.lfhardware.product.dto.ProductFilterCriteria;
import com.lfhardware.stock.domain.Stock;
import com.lfhardware.stock.domain.Stock_;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class ProductRepository implements IProductRepository {

    private final Mutiny.SessionFactory sessionFactory;

    public ProductRepository(Mutiny.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Uni<List<Product>> findAll(Mutiny.Session session, PageRequest pageRequest, Search searchRequest, ProductFilterCriteria filterCriteria) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Product> cq = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        root.fetch(Product_.BRAND, JoinType.LEFT);
        root.fetch(Product_.CATEGORY, JoinType.LEFT);
        root.fetch(Product_.PRODUCT_IMAGES, JoinType.LEFT);
        root.fetch(Product_.STOCKS, JoinType.LEFT);
        root.fetch(Product_.REVIEWS, JoinType.LEFT);

        //Sorting
        if (pageRequest.getSort().getName() != null && pageRequest.getSort().getOrder() != null) {
            if (pageRequest.getSort().getOrder().equals(SortOrder.ASC)) {
                cq.orderBy(criteriaBuilder.asc(root.get(pageRequest.getSort().getName())));
            } else {
                cq.orderBy(criteriaBuilder.desc(root.get(pageRequest.getSort().getName())));
            }
        }

        //Search
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(searchRequest) && searchRequest.getAttributes() != null && !searchRequest.getAttributes().isEmpty() && searchRequest.getKeyword() != null && !searchRequest.getKeyword().isEmpty()) {
            for(String attribute : searchRequest.getAttributes()){
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get(attribute).as(String.class), "%"+ searchRequest.getKeyword() +"%")));
            }
        }

        if(Objects.nonNull(filterCriteria)){
            //Filter by categories
            if (!filterCriteria.getCategoryIds().isEmpty()) {
                Path<Object> path = root.get(Product_.CATEGORY).get(Category_.ID);
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                for (Long categoryId : filterCriteria.getCategoryIds()) {
                    in.value(categoryId);
                }
                predicates.add(in);
            }

            if (!filterCriteria.getBrandIds().isEmpty()) {
                Path<Object> path = root.get(Product_.BRAND).get(Brand_.ID);
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                for (Long brandId : filterCriteria.getBrandIds()) {
                    in.value(brandId);
                }
                predicates.add(in);
            }

            //Create a left join when quantity is specified
            if (filterCriteria.getQuantity() != null) {
                Join<Product, Stock> join = root.join(Product_.STOCKS, JoinType.LEFT);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(join.get(Stock_.QUANTITY), filterCriteria.getQuantity()));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));

        cq.select(root);
        return session.createQuery(cq)
                .setFirstResult(pageRequest.getPageNo() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();


    }

    @Override
    public Uni<Long> count(Mutiny.Session session, Search searchRequest, ProductFilterCriteria filterCriteria) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Product> root = cq.from(Product.class);
        root.join(Product_.BRAND, JoinType.LEFT);
        root.join(Product_.CATEGORY, JoinType.LEFT);
        root.join(Product_.PRODUCT_IMAGES, JoinType.LEFT);
        root.join(Product_.STOCKS, JoinType.LEFT);

        //Search
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(searchRequest) && searchRequest.getAttributes() != null && !searchRequest.getAttributes().isEmpty() && searchRequest.getKeyword() != null && !searchRequest.getKeyword().isEmpty()) {
            for(String attribute : searchRequest.getAttributes()){
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get(attribute).as(String.class), "%"+ searchRequest.getKeyword() +"%")));
            }
        }

        if(Objects.nonNull(filterCriteria)){
            if (!filterCriteria.getCategoryIds().isEmpty()) {
                log.info("Category ids are not empty");
                Path<Object> path = root.get(Product_.CATEGORY).get(Category_.ID);
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                for (Long categoryId : filterCriteria.getCategoryIds()) {
                    in.value(categoryId);
                }
                predicates.add(in);
            }

            if (!filterCriteria.getBrandIds().isEmpty()) {
                Path<Object> path = root.get(Product_.BRAND).get(Brand_.ID);
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                for (Long brandId : filterCriteria.getBrandIds()) {
                    in.value(brandId);
                }
                predicates.add(in);
            }

            if (filterCriteria.getQuantity() != null) {
                Join<Product, Stock> join = root.join(Product_.STOCKS, JoinType.LEFT);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(join.get(Stock_.QUANTITY), filterCriteria.getQuantity()));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.select(criteriaBuilder.countDistinct(root));

        return session.createQuery(cq)
                .getSingleResult();
    }


//    @Override
//    public Uni<List<Product>> findAllRightJoinStock(Mutiny.Session session, ProductPageRequest pageInfo) {
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
//    public Uni<Long> countRightJoinStock(Mutiny.Session session, ProductPageRequest pageInfo){
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
    public Uni<List<Product>> findAll(Mutiny.Session session) {
        return null;
    }

    @Override
    public Uni<Product> findById(Mutiny.Session session, UUID id) {
        return session.find(session.getEntityGraph(Product.class, "ProductDetails"), id);
    }

    @Override
    public Uni<Product> findByName(Mutiny.Session session, String name) {
        return session.createNamedQuery("Product.findByName", Product.class)
                .setParameter("name", name)
                .setPlan(session.getEntityGraph(Product.class, "ProductDetails"))
                .getSingleResultOrNull();
    }

    @Override
    public Uni<Void> save(Mutiny.Session session, Product obj) {
        return session.persist(obj);
    }

    @Override
    public Uni<List<Product>> findAllByIds(Mutiny.Session session, List<UUID> ids) {
        return null;
    }

    @Override
    public Uni<Product> merge(Mutiny.Session session, Product obj) {
        return session.merge(obj);
    }

    @Override
    public Uni<Integer> deleteById(Mutiny.Session session, UUID id) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaDelete<Product> cd = cb.createCriteriaDelete(Product.class);
        Root<Product> root = cd.from(Product.class);
        cd.where(cb.equal(root.get(Product_.ID), id));
        return session.createQuery(cd)
                .executeUpdate();
    }


    @Override
    public Product loadReferenceById(Mutiny.Session session, UUID id) {
        return session.getReference(Product.class, id);
    }

    @Override
    public Uni<Void> saveAll(Mutiny.Session session, Collection<Product> objs) {
        return null;
    }

    @Override
    public Uni<Void> deleteAll(Mutiny.Session session, List<Product> objs) {
        return null;
    }

    @Override
    public Uni<Void> delete(Mutiny.Session session, Product obj) {
        return session.remove(obj);
    }

    @Override
    public Uni<Integer> deleteAllByIds(Mutiny.Session session, List<UUID> ids) {
        return null;
    }
}
