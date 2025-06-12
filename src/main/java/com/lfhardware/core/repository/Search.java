package com.lfhardware.core.repository;

import com.lfhardware.core.validation.ValidKeywordAndAttributeList;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidKeywordAndAttributeList
public class Search {

    private List<String> attributes;

    private String keyword;


}
