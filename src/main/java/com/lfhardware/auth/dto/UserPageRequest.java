package com.lfhardware.auth.dto;

import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Search;
import com.lfhardware.core.repository.Sort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPageRequest {

    private int pageSize;

    private int page;

    private Sort sort;

    private Search search;

}
