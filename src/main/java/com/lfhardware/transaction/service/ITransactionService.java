package com.lfhardware.transaction.service;

import com.lfhardware.core.dto.PageInfo;
import com.lfhardware.core.dto.Pageable;
import com.lfhardware.transaction.dto.TransactionDTO;
import reactor.core.publisher.Mono;

public interface ITransactionService {

    Mono<Pageable<TransactionDTO>> findAll(PageInfo pageRequest);

    Mono<TransactionDTO> findById(String id);

    Mono<Long> count();
}
