package com.lfhardware.provider.mapper;

import com.lfhardware.provider.domain.CityCoverage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CityCoverageMapper{

    protected Set<String> mapCityCoverageName(Set<CityCoverage> coverages){
        return coverages.stream().map(coverage->coverage.getCity().getName()).collect(Collectors.toSet());
    }
}
