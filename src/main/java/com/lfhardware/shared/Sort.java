package com.lfhardware.shared;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sort{

    private String name;

    private SortOrder order;
}

