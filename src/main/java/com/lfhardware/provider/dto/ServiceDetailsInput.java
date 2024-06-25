package com.lfhardware.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailsInput {

    private List<ServiceInput> services;

    private CoverageInput coverage;
}
