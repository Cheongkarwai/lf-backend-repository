package com.lfhardware.provider.repository;

import com.lfhardware.provider.domain.ServiceProviderReview;
import com.lfhardware.provider.dto.ServiceProviderReviewCountGroupByRatingDTO;
import com.lfhardware.shared.CrudRepository;
import com.lfhardware.shared.PageInfo;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface IServiceProviderReviewRepository extends CrudRepository<ServiceProviderReview, Long> {

    CompletionStage<List<ServiceProviderReview>> findAllReviewByServiceProviderId(Stage.Session session, PageInfo pageInfo, String id, Double rating);

    CompletionStage<Long> countByServiceProviderId(Stage.Session session, String id, Double rating);

    CompletionStage<List<ServiceProviderReviewCountGroupByRatingDTO>> countReviewsByServiceProviderIdGroupByRating(Stage.Session session, String serviceProviderId);
}
