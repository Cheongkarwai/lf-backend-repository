package com.lfhardware.provider.repository;

import com.lfhardware.auth.domain.User;
import com.lfhardware.auth.domain.User_;
import com.lfhardware.product.domain.Brand_;
import com.lfhardware.product.domain.Category_;
import com.lfhardware.product.domain.Product;
import com.lfhardware.product.domain.Product_;
import com.lfhardware.provider.domain.*;
import com.lfhardware.provider.dto.ServiceProviderAppointmentCountGroupByDayDTO;
import com.lfhardware.provider.dto.ServiceProviderCountGroupByDayDTO;
import com.lfhardware.provider.dto.ServiceProviderPageRequest;
import com.lfhardware.provider.repository.predicate.ProviderPredicateBuilder;
import com.lfhardware.provider.service.ProviderService;
import com.lfhardware.provider_business.domain.Service;
import com.lfhardware.provider_business.domain.ServiceCategory;
import com.lfhardware.provider_business.domain.ServiceCategory_;
import com.lfhardware.provider_business.domain.Service_;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.PageRepository;
import com.lfhardware.shared.PageRequestPredicateBuilder;
import com.lfhardware.shared.SortOrder;
import com.lfhardware.state.domain.State_;
import com.lfhardware.stock.domain.Stock;
import com.lfhardware.stock.domain.Stock_;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

@Repository
@Slf4j
public class ProviderRepository extends PageRepository implements IProviderRepository {

    private final Stage.SessionFactory sessionFactory;

    public ProviderRepository(Stage.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletionStage<List<ServiceProvider>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<ServiceProvider> findById(Stage.Session session, String id) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<ServiceProvider> cq = criteriaBuilder.createQuery(ServiceProvider.class);
        Root<ServiceProvider> root = cq.from(ServiceProvider.class);
//        root.fetch(ServiceProvider_.CITY_COVERAGES);
//        root.fetch(ServiceProvider_.STATE_COVERAGES)
//                .fetch(StateCoverage_.STATE);
//        root.fetch(ServiceProvider_.COUNTRY_COVERAGES);
//        root.fetch(ServiceProvider_.SERVICE_DETAILS)
//                .fetch(ServiceDetails_.SERVICE);
//        root.fetch(ServiceProvider_.REVIEWS);
        cq.select(root).where(criteriaBuilder.equal(root.get(ServiceProvider_.ID), id));
        return session.createQuery(cq).setPlan(session.getEntityGraph(ServiceProvider.class, "serviceProvider")).getSingleResultOrNull();
    }


    @Override
    public CompletionStage<Void> save(Stage.Session session, ServiceProvider serviceProvider) {
        return session.persist(serviceProvider);
    }

    @Override
    public CompletionStage<List<ServiceProvider>> findAllByIds(Stage.Session session, List<String> ids) {
        return null;
    }

    @Override
    public CompletionStage<ServiceProvider> merge(Stage.Session session, ServiceProvider serviceProvider) {
        return session.merge(serviceProvider);
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, String aString) {
        return null;
    }


    @Override
    public ServiceProvider loadReferenceById(Stage.Session session, String aString) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<ServiceProvider> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<ServiceProvider> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, ServiceProvider obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<String> longs) {
        return null;
    }

    @Override
    public CompletionStage<List<ServiceProvider>> findAll(Stage.Session session, PageInfo pageRequest, List<String> status,
                                                          List<String> states, Double rating, String serviceName) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<ServiceProvider> cq = cb.createQuery(ServiceProvider.class);
        Root<ServiceProvider> root = cq.from(ServiceProvider.class);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, cb, root, cq);

        if (StringUtils.hasText(serviceName)) {
            predicates.add(ProviderPredicateBuilder.hasServiceName(cb, root, serviceName));
        }

        if (!CollectionUtils.isEmpty(states)) {
            predicates.add(ProviderPredicateBuilder.hasStates(cb, root, states));
        }

        if (rating != null) {
            predicates.add(ProviderPredicateBuilder.hasRating(cb, root, rating));
        }


        if(!CollectionUtils.isEmpty(status)){
            predicates.add(ProviderPredicateBuilder.hasStatus(cb, root, status));
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(root);
        return session.createQuery(cq).setFirstResult(pageRequest.getPage() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> count(Stage.Session session, PageInfo pageRequest, List<String> status,
                                       List<String> states,  Double rating, String serviceName) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<ServiceProvider> root = cq.from(ServiceProvider.class);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, criteriaBuilder, root, cq);

        if (StringUtils.hasText(serviceName)) {
            predicates.add(ProviderPredicateBuilder.hasServiceName(criteriaBuilder, root, serviceName));
        }

        if (!CollectionUtils.isEmpty(states)) {
            predicates.add(ProviderPredicateBuilder.hasStates(criteriaBuilder, root, states));
        }

        if (rating != null) {
            predicates.add(ProviderPredicateBuilder.hasRating(criteriaBuilder, root, rating));
        }


        if(!CollectionUtils.isEmpty(status)){
            predicates.add(ProviderPredicateBuilder.hasStatus(criteriaBuilder, root, status));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.select(criteriaBuilder.count(root));

        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public CompletionStage<ServiceProvider> findDetailsById(Stage.Session session, String id) {

        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<ServiceProvider> cq = criteriaBuilder.createQuery(ServiceProvider.class);
        Root<ServiceProvider> root = cq.from(ServiceProvider.class);
        root.fetch(ServiceProvider_.CITY_COVERAGES, JoinType.LEFT);
        root.fetch(ServiceProvider_.STATE_COVERAGES, JoinType.LEFT)
                .fetch(StateCoverage_.STATE);
        root.fetch(ServiceProvider_.COUNTRY_COVERAGES, JoinType.LEFT);
        root.fetch(ServiceProvider_.SERVICE_DETAILS, JoinType.LEFT)
                .fetch(ServiceDetails_.SERVICE);
        root.fetch(ServiceProvider_.REVIEWS, JoinType.LEFT);

        cq.where(criteriaBuilder.equal(root.get(ServiceProvider_.ID),id));
        cq.select(root);

        return session.createQuery(cq)
                .getSingleResultOrNull();
    }

    @Override
    public CompletionStage<Integer> updateStatus(Stage.Session session, String id, Status status) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaUpdate<ServiceProvider> cu = cb.createCriteriaUpdate(ServiceProvider.class);
        Root<ServiceProvider> root = cu.from(ServiceProvider.class);
        cu.set(root.get(ServiceProvider_.STATUS),status).where(cb.equal(root.get(ServiceProvider_.ID),id));
        return session.createQuery(cu)
                .executeUpdate();
    }

    @Override
    public CompletionStage<List<ServiceProviderCountGroupByDayDTO>> countServiceProvidersGroupByDay(Stage.Session session, Integer day) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime daysBefore = today.minusDays(day);

        return session.createQuery("SELECT extract(day from u.createdAt), COUNT(u) " +
                        "FROM ServiceProvider u " +
                        "WHERE u.createdAt BETWEEN :startDate " +
                        "AND :endDate " +
                        "GROUP BY extract(day from u.createdAt)", Object[].class)
                .setParameter("startDate", daysBefore)
                .setParameter("endDate", today)
                .getResultList().thenApply(objects -> {

                    List<ServiceProviderCountGroupByDayDTO> serviceProviderCountList = new ArrayList<>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

                    first:
                    for (LocalDateTime date = daysBefore; date.isBefore(today); date = date.plusDays(1)) {

                        second:
                        for(Object[] object : objects){
                            if(String.valueOf(object[0]).equals(String.valueOf(date.getDayOfMonth()))){
                                serviceProviderCountList.add(new ServiceProviderCountGroupByDayDTO(date.toLocalDate().format(formatter), Integer.parseInt(String.valueOf(object[1]))));

                                continue first;
                            }
                        }
                        serviceProviderCountList.add(new ServiceProviderCountGroupByDayDTO(date.toLocalDate().format(formatter), 0));
                    }
                    return serviceProviderCountList;
                });
    }
}
