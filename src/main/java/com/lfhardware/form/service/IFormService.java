package com.lfhardware.form.service;

import com.lfhardware.form.dto.FormDTO;
import com.lfhardware.form.dto.FormInput;
import com.lfhardware.form.dto.FormPageRequest;
import com.lfhardware.core.dto.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFormService {

    Flux<Page<FormDTO>>  findAll(FormPageRequest pageRequest);

    Mono<Void> save(Long serviceId, FormInput formInput);

    Mono<FormDTO> findById(Long id);
}
