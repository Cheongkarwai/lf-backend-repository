package com.lfhardware.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandDTO {

    private Long id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandDTO brandDTO = (BrandDTO) o;
        return Objects.equals(id, brandDTO.id) && Objects.equals(name, brandDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

