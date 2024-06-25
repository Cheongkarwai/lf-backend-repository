package com.lfhardware.transaction.service;

import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import com.lfhardware.transaction.dto.TransactionDTO;
import com.lfhardware.transaction.dto.TransactionDetailsDTO;
import reactor.core.publisher.Mono;

public interface ITransactionService {

    Mono<Pageable<TransactionDTO>> findAll(PageInfo pageRequest);

    Mono<TransactionDTO> findById(String id);

    Mono<Long> count();
}
