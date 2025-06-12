package com.lfhardware.core.repository;

import com.lfhardware.core.dto.PageRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;

public class PageQueryParameterBuilder {

    public static PageRequest buildPageRequest(ServerRequest serverRequest) {
//        PageRequest pageRequest = new PageRequest();
//        Optional<String> pageOptional = serverRequest.queryParam("page");
//        Optional<String> pageSizeOptional = serverRequest.queryParam("page_size");
//
//        pageRequest.setPage(Integer.parseInt(pageOptional.orElseGet(() -> String.valueOf(0))));
//        pageRequest.setPageSize(Integer.parseInt(pageSizeOptional.orElseGet(() -> String.valueOf(10))));
//
//        if (serverRequest.queryParam("keyword")
//                .isPresent()) {
//            Search search = new Search(serverRequest.queryParams()
//                    .get("search"), serverRequest.queryParam("keyword")
//                    .orElse(""));
//            pageRequest.setSearch(search);
//        }
//        Sort sort = new Sort(serverRequest.queryParam("sort")
//                .orElse(""));
//        pageRequest.setSort(sort);

//        return pageRequest;
        return null;
    }
}
