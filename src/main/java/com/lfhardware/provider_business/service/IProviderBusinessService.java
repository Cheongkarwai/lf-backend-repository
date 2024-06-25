package com.lfhardware.provider_business.service;

import com.lfhardware.provider_business.dto.ServiceDetailsDTO;
import com.lfhardware.provider_business.dto.ServiceGroupByCategoryDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IProviderBusinessService {

    Mono<List<ServiceGroupByCategoryDTO>> findAllServiceDetails();

    Mono<ServiceDetailsDTO> findDetailsById(Long id);
}
