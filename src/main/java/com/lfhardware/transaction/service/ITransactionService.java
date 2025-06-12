package com.lfhardware.transaction.service;

import com.lfhardware.core.dto.Page;
import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.transaction.dto.TransactionDTO;
import reactor.core.publisher.Mono;

public interface ITransactionService {

    Mono<Page<TransactionDTO>> findAll(PageRequest pageRequest);

    Mono<TransactionDTO> findById(String id);

    Mono<Long> count();
}
