package com.lfhardware.review.repository;

import com.lfhardware.review.domain.Review;
import com.lfhardware.shared.CrudRepository;
import com.lfhardware.shared.PagingRepository;

public interface IReviewRepository extends CrudRepository <Review,Long>, PagingRepository<Review> {
}
