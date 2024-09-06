package com.lfhardware.review.api;

import com.lfhardware.product.dto.ProductPageRequest;
import com.lfhardware.review.domain.ReviewInput;
import com.lfhardware.review.service.IReviewService;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Search;
import com.lfhardware.shared.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class ReviewApi {

    private final IReviewService reviewService;

    public ReviewApi(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {

        Search search = null;

        if(!serverRequest.queryParam("keyword").isEmpty()){
            search = new Search(serverRequest.queryParams().get("search"), serverRequest.queryParam("keyword").orElse(""));
        }

        PageInfo pageInfo = new PageInfo(
                Integer.parseInt(serverRequest.queryParam("page_size").orElse("3")),
                Integer.parseInt(serverRequest.queryParam("page").orElse("0")),
                new Sort(serverRequest.queryParam("sort").orElse("")),
                search);

        return ServerResponse.ok().bodyValue(reviewService.findAll(pageInfo));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ReviewInput.class)
                .flatMap(reviewService::save)
                .flatMap(e-> ServerResponse.noContent().build());
    }
}
