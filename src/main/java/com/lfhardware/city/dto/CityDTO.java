package com.lfhardware.city.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {

    private Long id;

    @NotNull(message = "{error.required}")
    @Length(min = 2, max = 50, message = "{error.length}")
    @Pattern(
            regexp = "^[a-zA-ZÀ-ÿ'\\-\\s]+$",
            message = "{error.should-contains-only-letter-spaces-hyphens-apostrophes}"
    )
    private String name;
}
