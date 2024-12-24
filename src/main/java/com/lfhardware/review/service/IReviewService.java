package com.lfhardware.review.service;

import com.lfhardware.review.domain.ReviewInput;
import com.lfhardware.review.dto.ReviewDTO;
import com.lfhardware.core.dto.PageInfo;
import com.lfhardware.core.dto.Pageable;
import reactor.core.publisher.Mono;

public interface IReviewService {

    Mono<Pageable<ReviewDTO>> findAll(PageInfo pageInfo);

    Mono<Void> save(ReviewInput reviewInput);
}
