package com.lfhardware.provider_business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceGroupByCategoryDTO {

    private Long id;

    private String name;

    private List<ServiceDTO> services;

}
