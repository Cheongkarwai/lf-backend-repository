package com.lfhardware.provider.repository;

import com.lfhardware.provider.domain.*;
import com.lfhardware.provider.dto.ServiceProviderReviewCountGroupByRatingDTO;
import com.lfhardware.provider.dto.ServiceProviderReviewDTO;
import com.lfhardware.provider_business.domain.Service_;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.PageRepository;
import com.lfhardware.shared.SortOrder;
import com.lfhardware.state.domain.State_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.Query;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Repository
public class ServiceProviderReviewRepository extends PageRepository implements IServiceProviderReviewRepository {

    private final Stage.SessionFactory sessionFactory;

    public ServiceProviderReviewRepository(Stage.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public CompletionStage<List<ServiceProviderReview>> findAll(Stage.Session session) {

        return null;
    }

    @Override
    public CompletionStage<ServiceProviderReview> findById(Stage.Session session, Long id) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, ServiceProviderReview obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<ServiceProviderReview>> findAllByIds(Stage.Session session, List<Long> serviceProviderReviewIds) {
        return null;
    }

    @Override
    public CompletionStage<ServiceProviderReview> merge(Stage.Session session, ServiceProviderReview obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long serviceProviderReviewId) {
        return null;
    }

    @Override
    public ServiceProviderReview loadReferenceById(Stage.Session session, Long serviceProviderReviewId) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<ServiceProviderReview> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<ServiceProviderReview> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, ServiceProviderReview obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> serviceProviderReviewIds) {
        return null;
    }

    @Override
    public CompletionStage<List<ServiceProviderReview>> findAllReviewByServiceProviderId(Stage.Session session, PageInfo pageRequest, String id, Double rating) {

        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<ServiceProviderReview> cq = criteriaBuilder.createQuery(ServiceProviderReview.class);
        Root<ServiceProviderReview> root = cq.from(ServiceProviderReview.class);
        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, criteriaBuilder, root, cq);

        if (Objects.nonNull(id)) {
            predicates.add(criteriaBuilder.equal(root.get(ServiceProviderReview_.SERVICE_PROVIDER)
                    .get(ServiceProvider_.ID), id));
        }
        if (Objects.nonNull(rating)) {
            if(rating.equals(1.0)){
                predicates.add(criteriaBuilder.and(criteriaBuilder.lt(root.get(ServiceProvider_.RATING), rating), criteriaBuilder.greaterThanOrEqualTo(root.get(ServiceProvider_.RATING), 0)));
            }
            else if(rating.equals(2.0)){
                predicates.add(criteriaBuilder.and(criteriaBuilder.lt(root.get(ServiceProvider_.RATING), rating), criteriaBuilder.greaterThanOrEqualTo(root.get(ServiceProvider_.RATING), 1)));
            }
            else if(rating.equals(3.0)){
                predicates.add(criteriaBuilder.and(criteriaBuilder.lt(root.get(ServiceProvider_.RATING), rating), criteriaBuilder.greaterThanOrEqualTo(root.get(ServiceProvider_.RATING), 2)));
            }
            else if(rating.equals(4.0)){
                predicates.add(criteriaBuilder.and(criteriaBuilder.lt(root.get(ServiceProvider_.RATING), rating), criteriaBuilder.greaterThanOrEqualTo(root.get(ServiceProvider_.RATING), 3)));
            }
            else if(rating.equals(5.0)){
                predicates.add(criteriaBuilder.and(criteriaBuilder.lt(root.get(ServiceProvider_.RATING), rating), criteriaBuilder.greaterThanOrEqualTo(root.get(ServiceProvider_.RATING), 4)));
            }
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(root);
        return session.createQuery(cq)
                .setFirstResult(pageRequest.getPage() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> countByServiceProviderId(Stage.Session session, String id, Double rating) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<ServiceProviderReview> root = cq.from(ServiceProviderReview.class);

        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(id)) {
            predicates.add(criteriaBuilder.equal(root.get(ServiceProviderReview_.SERVICE_PROVIDER)
                    .get(ServiceProvider_.ID), id));
        }

        if (Objects.nonNull(rating)) {
            if(rating.equals(1.0)){
                predicates.add(criteriaBuilder.and(criteriaBuilder.lt(root.get(ServiceProvider_.RATING), rating), criteriaBuilder.greaterThanOrEqualTo(root.get(ServiceProvider_.RATING), 0)));
            }
            else if(rating.equals(2.0)){
                predicates.add(criteriaBuilder.and(criteriaBuilder.lt(root.get(ServiceProvider_.RATING), rating), criteriaBuilder.greaterThanOrEqualTo(root.get(ServiceProvider_.RATING), 1)));
            }
            else if(rating.equals(3.0)){
                predicates.add(criteriaBuilder.and(criteriaBuilder.lt(root.get(ServiceProvider_.RATING), rating), criteriaBuilder.greaterThanOrEqualTo(root.get(ServiceProvider_.RATING), 2)));
            }
            else if(rating.equals(4.0)){
                predicates.add(criteriaBuilder.and(criteriaBuilder.lt(root.get(ServiceProvider_.RATING), rating), criteriaBuilder.greaterThanOrEqualTo(root.get(ServiceProvider_.RATING), 3)));
            }
            else if(rating.equals(5.0)){
                predicates.add(criteriaBuilder.and(criteriaBuilder.lt(root.get(ServiceProvider_.RATING), rating), criteriaBuilder.greaterThanOrEqualTo(root.get(ServiceProvider_.RATING), 4)));
            }
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(criteriaBuilder.count(root));

        return session.createQuery(cq)
                .getSingleResult();
    }

    @Override
    public CompletionStage<List<ServiceProviderReviewCountGroupByRatingDTO>> countReviewsByServiceProviderIdGroupByRating(Stage.Session session, String serviceProviderId) {

        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        String nativeQuery = "SELECT  COUNT(*), " +
                "CASE WHEN (rating >= 3.5 AND rating <= 4.0) OR (rating >= 4.5 AND rating <= 5.0) OR (rating >= 2.5 AND rating <= 3.0) OR (rating >= 1.5 AND rating <= 2.0) " +
                "THEN CEIL(rating) " +
                "ELSE FLOOR(rating) " +
                "END " +
                "FROM tbl_service_provider_review " +
                "WHERE service_provider_id = :serviceProviderId " +
                "GROUP BY rating";
        Stage.Query<Object> query = session.createNativeQuery(nativeQuery)
                .setParameter("serviceProviderId", serviceProviderId);
        return query.getResultList()
                .thenApply(results -> {
                    List<ServiceProviderReviewCountGroupByRatingDTO> serviceProviderReviewCountGroupByRatingDTOS = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        ServiceProviderReviewCountGroupByRatingDTO serviceProviderReviewCountGroupByRatingDTO = new ServiceProviderReviewCountGroupByRatingDTO();
                        serviceProviderReviewCountGroupByRatingDTO.setRating(String.valueOf(i + 1));
                        serviceProviderReviewCountGroupByRatingDTOS.add(serviceProviderReviewCountGroupByRatingDTO);
                    }
                   results.forEach(result -> {
                                Object[] obj = (Object[]) result;
                                BigDecimal rating = (BigDecimal) obj[1];
                                Long total = (Long) obj[0];
                                ServiceProviderReviewCountGroupByRatingDTO serviceProviderReviewCountGroupByRatingDTO = serviceProviderReviewCountGroupByRatingDTOS.get(rating.intValue() - 1);
                                serviceProviderReviewCountGroupByRatingDTO.setTotal(total.intValue());
                            });

                    return serviceProviderReviewCountGroupByRatingDTOS;
                });
    }
}
