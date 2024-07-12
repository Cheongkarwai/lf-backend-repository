package com.lfhardware.provider_business.service;

import com.lfhardware.form.domain.FormConfiguration;
import com.lfhardware.form.dto.FormDTO;
import com.lfhardware.provider_business.dto.ServiceDetailsDTO;
import com.lfhardware.provider_business.dto.ServiceGroupByCategoryDTO;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.BodyInserter;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IProviderBusinessService {

    Mono<List<ServiceGroupByCategoryDTO>> findAllServiceDetails();

    Mono<ServiceDetailsDTO> findDetailsById(Long id);

}
