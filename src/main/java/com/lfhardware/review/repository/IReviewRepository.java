package com.lfhardware.review.repository;

import com.lfhardware.review.domain.Review;
import com.lfhardware.core.repository.CrudRepository;
import com.lfhardware.core.repository.PagingRepository;

public interface IReviewRepository extends CrudRepository <Review,Long>, PagingRepository<Review> {
}
