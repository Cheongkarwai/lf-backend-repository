package com.lfhardware.shared;

import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.Optional;

public class PageQueryParameterBuilder {

    public static PageInfo buildPageRequest(ServerRequest serverRequest) {
        PageInfo pageInfo = new PageInfo();
        Optional<String> pageOptional = serverRequest.queryParam("page");
        Optional<String> pageSizeOptional = serverRequest.queryParam("page_size");

        pageInfo.setPage(Integer.parseInt(pageOptional.orElseGet(() -> String.valueOf(0))));
        pageInfo.setPageSize(Integer.parseInt(pageSizeOptional.orElseGet(() -> String.valueOf(10))));

        if (serverRequest.queryParam("keyword")
                .isPresent()) {
            Search search = new Search(serverRequest.queryParams()
                    .get("search"), serverRequest.queryParam("keyword")
                    .orElse(""));
            pageInfo.setSearch(search);
        }
        Sort sort = new Sort(serverRequest.queryParam("sort")
                .orElse(""));
        pageInfo.setSort(sort);

        return pageInfo;
    }
}
