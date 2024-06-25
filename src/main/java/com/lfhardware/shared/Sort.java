package com.lfhardware.shared;

import lombok.*;

import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sort{

    private String name;

    private SortOrder order;

    public Sort(String sort){
        if(sort.indexOf(",") != -1){
            String [] sortArr = sort.split(",");
            name = sortArr[0];
            order = sortArr[1].equals("DESC") ? SortOrder.DESC : SortOrder.ASC;

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sort sort = (Sort) o;
        return Objects.equals(name, sort.name) && order == sort.order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, order);
    }
}

