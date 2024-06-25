package com.lfhardware.review.service;

import com.lfhardware.product.repository.IProductRepository;
import com.lfhardware.review.domain.Review;
import com.lfhardware.review.domain.ReviewInput;
import com.lfhardware.review.dto.ReviewDTO;
import com.lfhardware.review.mapper.ReviewMapper;
import com.lfhardware.review.repository.IReviewRepository;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;

    private final IProductRepository productRepository;

    private final ReviewMapper reviewMapper;

    private final Stage.SessionFactory sessionFactory;

    public ReviewService(IReviewRepository reviewRepository, IProductRepository productRepository, ReviewMapper reviewMapper, Stage.SessionFactory sessionFactory) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.reviewMapper = reviewMapper;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Mono<Pageable<ReviewDTO>> findAll(PageInfo pageInfo) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> reviewRepository.findAll(session, null))
                .thenCombine(sessionFactory.withSession(session -> reviewRepository.count(session, null)), (items, totalElements) -> new Pageable<ReviewDTO>(items.stream().map(reviewMapper::mapToReviewDTO).collect(Collectors.toList()), pageInfo.getPageSize(), pageInfo.getPage(), totalElements.intValue())));
    }

    @Override
    public Mono<Void> save(ReviewInput reviewInput){

        Review review = reviewMapper.mapToReview(reviewInput);

        return Mono.fromCompletionStage(sessionFactory.withSession(session -> productRepository.findById(session, reviewInput.getProductId())
                .thenCompose(product->{
                    product.addReview(review);
                    return productRepository.save(session, product);
                })));
    }
}
