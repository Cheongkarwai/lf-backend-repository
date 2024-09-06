package com.lfhardware.customer.api;

import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.customer.dto.CustomerDTO;
import com.lfhardware.customer.dto.CustomerInfoInput;
import com.lfhardware.customer.service.ICustomerService;
import com.lfhardware.shared.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class CustomerApi {

    private final ICustomerService customerService;

    private CustomerApi(ICustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(customerService.findById(serverRequest.pathVariable("id")), CustomerDTO.class);
    }


    public Mono<ServerResponse> findCurrentCustomer(ServerRequest serverRequest) {
        return customerService.findCurrentlyLoggedInCustomer()
                .flatMap(customer -> ServerResponse.ok()
                        .bodyValue(customer));
    }

    public Mono<ServerResponse> findCurrentCustomerAppointments(ServerRequest serverRequest) {
        PageInfo pageInfo = new PageInfo();
        Optional<String> pageOptional = serverRequest.queryParam("page");
        Optional<String> pageSizeOptional = serverRequest.queryParam("page_size");

        pageInfo.setPage(Integer.parseInt(pageOptional.orElseGet(() -> String.valueOf(0))));
        pageInfo.setPageSize(Integer.parseInt(pageSizeOptional.orElseGet(() -> String.valueOf(10))));

        LocalDateTime dateTime = serverRequest.queryParam("bookingDateTime")
                .map(bookingDateTime->{
                    ZonedDateTime parsedDate = ZonedDateTime.parse(bookingDateTime);
                    return parsedDate.toLocalDateTime();
                })
                .orElse(null);

        List<String> status = serverRequest.queryParams().getOrDefault("status", List.of());

        return ServerResponse.ok()
                .body(customerService.findAllCurrentCustomerAppointments(pageInfo, dateTime, status), Pageable.class);
    }

    public Mono<ServerResponse> findAppointmentsByCustomerId(ServerRequest serverRequest){
        PageInfo pageInfo = new PageInfo();
        Optional<String> pageOptional = serverRequest.queryParam("page");
        Optional<String> pageSizeOptional = serverRequest.queryParam("page_size");

        pageInfo.setPage(Integer.parseInt(pageOptional.orElseGet(() -> String.valueOf(0))));
        pageInfo.setPageSize(Integer.parseInt(pageSizeOptional.orElseGet(() -> String.valueOf(10))));
        pageInfo.setSort(new Sort(serverRequest.queryParam("sort").orElse("")));

        return ServerResponse.ok()
                .body(customerService.findAllAppointmentsByCustomerId(pageInfo, serverRequest.pathVariable("id")), Pageable.class);
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {

        PageInfo pageRequest = PageQueryParameterBuilder.buildPageRequest(serverRequest);

        return ServerResponse.ok()
                .body(customerService.findAll(pageRequest), Pageable.class);
    }

    public Mono<ServerResponse> findCurrentCustomerAppointmentById(ServerRequest serverRequest){

        String serviceProviderId = serverRequest.pathVariable("serviceProviderId");
        Long serviceId = Long.valueOf(serverRequest.pathVariable("serviceId"));
        LocalDateTime createdAt = LocalDateTime.parse(serverRequest.pathVariable("createdAt"));

        return ServerResponse.ok()
                .body(customerService.findCurrentCustomerAppointmentById(serviceId, serviceProviderId, createdAt), AppointmentDTO.class);
    }

    public Mono<ServerResponse> count(ServerRequest serverRequest){
        String day = serverRequest.queryParam("day").orElse(null);

        return ServerResponse.ok()
                .body(customerService.countCustomers(Objects.nonNull(day) ? Integer.valueOf(day) : null), List.class);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CustomerInfoInput.class)
                .flatMap(customerService::save)
                .then(ServerResponse.noContent()
                        .build());
    }
    public Mono<ServerResponse> update(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CustomerInfoInput.class)
                .flatMap(customerInfoInput -> customerService.update(serverRequest.pathVariable("id"), customerInfoInput))
                .then(ServerResponse.noContent()
                        .build());
    }

}
