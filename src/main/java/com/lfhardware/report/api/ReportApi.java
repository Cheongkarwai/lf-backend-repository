package com.lfhardware.report.api;

import com.lfhardware.report.dto.MonthlySalesStat;
import com.lfhardware.report.service.ISalesService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ReportApi {

    private ISalesService salesService;

    public ReportApi(ISalesService salesService){
        this.salesService = salesService;
    }

    public Mono<ServerResponse> findSales(ServerRequest serverRequest){
        return ServerResponse.ok()
                .body(salesService.getFullYearSales(), new ParameterizedTypeReference<List<MonthlySalesStat>>() {});
    }
}
