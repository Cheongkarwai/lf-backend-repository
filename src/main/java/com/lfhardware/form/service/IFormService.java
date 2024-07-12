package com.lfhardware.form.service;

import com.lfhardware.form.domain.FormId;
import com.lfhardware.form.dto.FormDTO;
import com.lfhardware.form.dto.FormInput;
import com.lfhardware.form.dto.FormPageRequest;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFormService {

    Flux<Pageable<FormDTO>>  findAll(FormPageRequest pageRequest);

    Mono<Void> save(Long serviceId, FormInput formInput);

    Mono<FormDTO> findById(Long id);
}
