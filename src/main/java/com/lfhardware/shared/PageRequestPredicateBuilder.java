package com.lfhardware.shared;

import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class PageRequestPredicateBuilder {

    public static Predicate hasSearch(CriteriaBuilder cb, Root<?> root, String keyword, List<String> attributes){

        List<Predicate> predicates = new ArrayList<>();

        for (String attribute : attributes) {
            predicates.add(cb.like(cb.lower(getPath(root,attribute).as(String.class)), "%" + keyword.toLowerCase() + "%"));
        }
        return cb.or(predicates.toArray(Predicate[]::new));
    }

    public static Order hasOrder(SortOrder order, CriteriaBuilder cb, Root<?> root, String sortAttributeName){
        if(order.equals(SortOrder.DESC)){
            return isDescending(cb, root, sortAttributeName);
        }
        return isAscending(cb, root, sortAttributeName);
    }
    public static Order isDescending(CriteriaBuilder cb, Root<?> root, String sortAttributeName){
        return cb.desc(root.get(sortAttributeName));
    }
    public static Order isAscending(CriteriaBuilder cb, Root<?> root, String sortAttributeName){
        return cb.asc(root.get(sortAttributeName));
    }

    private static <T> Path<T> getPath(Root<T> root, String attributeName) {
        Path<T> path = root;
        for (String part : attributeName.split("\\.")) {
            path = path.get(part);
        }
        return path;
    }
}
