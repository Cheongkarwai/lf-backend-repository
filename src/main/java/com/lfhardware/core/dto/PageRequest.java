package com.lfhardware.core.dto;


import com.lfhardware.core.repository.Search;
import com.lfhardware.core.repository.Sort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {

    private int pageSize;

    private int pageNo;

    private Sort sort;

    private Search search;


//    public PageRequest(int page, int pageSize){
//        this.page = page;
//        this.pageSize = pageSize;
//    }
//
//    public void incrementPage(){
//        this.page++;
//    }
//
////    private String search;
//
////    @Override
////    public boolean equals(Object o) {
////        if (this == o) return true;
////        if (o == null || getClass() != o.getClass()) return false;
////        PageInfo pageInfo = (PageInfo) o;
////        return pageSize == pageInfo.pageSize;
////    }
////
////    @Override
////    public int hashCode() {
////        return Objects.hash(pageSize);
////    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        PageRequest pageRequest = (PageRequest) o;
//        return pageSize == pageRequest.pageSize && page == pageRequest.page && Objects.equals(sort, pageRequest.sort) && Objects.equals(search, pageRequest.search);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(pageSize, page, sort, search);
//    }

}



