package com.lfhardware.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Pageable<T> {

    private List<T> items;

    private int size;

    @JsonProperty("current_page")
    private int currentPage;

    @JsonProperty("total_elements")
    private int totalElements;

    @JsonProperty("has_next_page")
    private boolean hasNextPage;

    @JsonProperty("has_previous_page")
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
       System.out.println("total page"+totalElements/pageSize);
       return totalElements > 0 && ((currentPage + 1) < (totalElements / pageSize));
   }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pageable<?> pageable = (Pageable<?>) o;
        return size == pageable.size && currentPage == pageable.currentPage && totalElements == pageable.totalElements && hasNextPage == pageable.hasNextPage && hasPreviousPage == pageable.hasPreviousPage && Objects.equals(items, pageable.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, size, currentPage, totalElements, hasNextPage, hasPreviousPage);
    }

}
