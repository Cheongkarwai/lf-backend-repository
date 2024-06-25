package com.lfhardware.review.mapper;

import com.lfhardware.review.domain.Review;
import com.lfhardware.review.domain.ReviewInput;
import com.lfhardware.review.dto.ReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {


    @Mappings({
            @Mapping(target = "userId", source = "userId"),
            @Mapping(target = "rating", source = "rating"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "createdAt", source = "createdAt")
    })
    ReviewDTO mapToReviewDTO(Review review);

    @Mappings({
            @Mapping(target = "userId", source = "userId"),
            @Mapping(target = "rating", source = "rating"),
            @Mapping(target = "description", source = "description"),
    })
    Review mapToReview(ReviewInput reviewInput);
}
