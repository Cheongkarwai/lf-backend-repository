package com.lfhardware.core.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.core.repository.Search;
import com.lfhardware.core.repository.Sort;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {

    @JsonProperty("page_size")
    private int pageSize;

    private int page;

    private Sort sort;

    private Search search;

    public PageInfo(int page, int pageSize){
        this.page = page;
        this.pageSize = pageSize;
    }

    public void incrementPage(){
        this.page++;
    }

//    private String search;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        PageInfo pageInfo = (PageInfo) o;
//        return pageSize == pageInfo.pageSize;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(pageSize);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageInfo pageInfo = (PageInfo) o;
        return pageSize == pageInfo.pageSize && page == pageInfo.page && Objects.equals(sort, pageInfo.sort) && Objects.equals(search, pageInfo.search);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageSize, page, sort, search);
    }

}



