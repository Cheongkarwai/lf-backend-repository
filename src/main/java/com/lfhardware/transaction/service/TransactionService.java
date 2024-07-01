package com.lfhardware.transaction.service;

import com.lfhardware.charges.repository.ITransactionRepository;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import com.lfhardware.transaction.dto.TransactionDTO;

import com.lfhardware.transaction.dto.TransactionDetailsDTO;
import com.lfhardware.transaction.mapper.TransactionMapper;
import com.stripe.StripeClient;
import com.stripe.model.Charge;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {

    public final Stage.SessionFactory sessionFactory;

    private final ITransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    private final StripeClient stripeClient;

    public TransactionService(Stage.SessionFactory sessionFactory,
                              ITransactionRepository transactionRepository,
                              TransactionMapper transactionMapper,
                              StripeClient stripeClient) {
        this.sessionFactory = sessionFactory;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.stripeClient = stripeClient;
    }

    @Override
    public Mono<Pageable<TransactionDTO>> findAll(PageInfo pageRequest) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> transactionRepository.findAll(session, pageRequest))
                .thenCombine(sessionFactory.withSession(session -> transactionRepository.count(session, pageRequest)),
                        (transactions, totalRecords) -> new Pageable<>(
                                transactions.stream().map(transactionMapper::mapToTransactionDTO).collect(Collectors.toList()),
                                pageRequest.getPageSize(), pageRequest.getPage(), totalRecords.intValue())));
    }

    @Override
    public Mono<TransactionDTO> findById(String id) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> {
            return transactionRepository.findById(session, id);
        }).thenApply(transactionMapper::mapToTransactionDTO));
    }

    @Override
    public Mono<Long> count() {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> transactionRepository.count(session, new PageInfo())));
    }


}
