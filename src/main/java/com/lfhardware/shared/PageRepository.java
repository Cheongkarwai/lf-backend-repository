package com.lfhardware.shared;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PageRepository {

    protected List<Predicate> buildPageRequestPredicates(PageInfo pageRequest, CriteriaBuilder cb, Root<?> root, CriteriaQuery<?> cq){
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(pageRequest.getSearch()) && StringUtils.hasText(pageRequest.getSearch()
                .getKeyword()) && CollectionUtils.isNotEmpty(pageRequest.getSearch()
                .getAttributes())) {

            predicates.add(PageRequestPredicateBuilder.hasSearch(cb, root, pageRequest.getSearch()
                    .getKeyword(), pageRequest.getSearch()
                    .getAttributes()));
        }

        if (Objects.nonNull(pageRequest.getSort()) && Objects.nonNull(pageRequest.getSort()
                .getOrder()) && StringUtils.hasText(pageRequest.getSort()
                .getName())) {

            cq.orderBy(PageRequestPredicateBuilder.hasOrder(pageRequest.getSort()
                    .getOrder(), cb, root, pageRequest.getSort()
                    .getName()));
        }

        return predicates;
    }
}
