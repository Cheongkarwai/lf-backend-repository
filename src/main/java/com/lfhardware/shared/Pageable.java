package com.lfhardware.shared;

import lombok.*;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Pageable<T> {

    private List<T> items;

    private int size;

    private int currentPage;

    private int totalElements;

    private boolean hasNextPage;

    private boolean hasPreviousPage;

   public Pageable(List<T> items, int size, int currentPage, int totalElements){
       this.items = items;
       this.size = size;
       this.currentPage = currentPage;
       this.totalElements = totalElements;
       this.hasNextPage = checkHasNextPage(totalElements,size,currentPage);
       this.hasPreviousPage = checkHasPreviousPage(totalElements,size,currentPage);
   }

   private boolean checkHasPreviousPage(int totalElements, int pageSize, int currentPage){

       return currentPage != 0;
   }

   private boolean checkHasNextPage(int totalElements, int pageSize, int currentPage){

       return currentPage != totalElements / pageSize;
   }
}
