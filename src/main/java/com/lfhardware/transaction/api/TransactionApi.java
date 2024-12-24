package com.lfhardware.transaction.api;

import com.lfhardware.core.dto.PageInfo;
import com.lfhardware.core.dto.Pageable;
import com.lfhardware.core.repository.Search;
import com.lfhardware.core.repository.Sort;
import com.lfhardware.transaction.dto.TransactionDTO;
import com.lfhardware.transaction.service.ITransactionService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransactionApi {

    private final ITransactionService transactionService;

    public TransactionApi(ITransactionService transactionService){
        this.transactionService = transactionService;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){

        Search search = null;

        if(!request.queryParam("keyword").isEmpty()){
            search = new Search(request.queryParams().get("search"), request.queryParam("keyword").orElse(""));
        }

        PageInfo pageRequest = new PageInfo(
                Integer.parseInt(request.queryParam("page_size").orElse("3")),
                Integer.parseInt(request.queryParam("page").orElse("0")),
                new Sort(request.queryParam("sort").orElse("")),
                search);

        return ServerResponse.ok().body(transactionService.findAll(pageRequest), Pageable.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest){
        return ServerResponse.ok().body(transactionService.findById(serverRequest.pathVariable("id")), TransactionDTO.class);
    }

    public Mono<ServerResponse> count(ServerRequest serverRequest) {
        return ServerResponse.ok().body(transactionService.count(),Long.class);
    }
}
