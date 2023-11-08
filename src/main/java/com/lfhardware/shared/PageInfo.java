package com.lfhardware.shared;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageInfo {

    @JsonProperty("page_size")
    private int pageSize;

    private int page;

    private Sort sort;
}



