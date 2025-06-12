package com.lfhardware.review.service;

import com.lfhardware.review.domain.ReviewInput;
import com.lfhardware.review.dto.ReviewDTO;
import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.dto.Page;
import reactor.core.publisher.Mono;

public interface IReviewService {

    Mono<Page<ReviewDTO>> findAll(PageRequest pageRequest);

    Mono<Void> save(ReviewInput reviewInput);
}
