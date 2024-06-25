package com.lfhardware.report.service;

import com.lfhardware.report.dto.MonthlySalesStat;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ISalesService {

    Mono<List<MonthlySalesStat>> getFullYearSales();
}
