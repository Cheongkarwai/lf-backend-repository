package com.lfhardware.faq.service;

import com.lfhardware.faq.dto.FaqDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFaqService {

    Mono<List<FaqDTO>> findAll();

    Mono<FaqDTO> save(FaqDTO faqDTO);

    Mono<Void> deleteById(Long id);
    Mono<Void> updateById(Long id, FaqDTO faqDTO);
}
