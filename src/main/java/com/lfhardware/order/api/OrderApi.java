package com.lfhardware.order.api;

import com.lfhardware.order.domain.DeliveryStatus;
import com.lfhardware.order.dto.*;
import com.lfhardware.order.service.IOrderService;
import com.lfhardware.shared.Search;
import com.lfhardware.shared.Sort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Component
public class OrderApi {

    private final IOrderService orderService;

    public OrderApi(IOrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(OrderInput.class)
                .flatMap(orderService::create)
                .flatMap(orderDetailsDTO -> ServerResponse.ok().bodyValue(orderDetailsDTO));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAllOrders(ServerRequest request) {

        Optional<String> deliveryStatus = request.queryParam("delivery_status");

        Search search = null;

        if(!request.queryParam("keyword").isEmpty()){
            search = new Search(request.queryParams().get("search"), request.queryParam("keyword").orElse(""));
        }

        OrderPageRequest pageRequest = new OrderPageRequest(
                Integer.parseInt(request.queryParam("page_size").orElse("3")),
                Integer.parseInt(request.queryParam("page").orElse("0")),
                new Sort(request.queryParam("sort").orElse("")),
                search, deliveryStatus.map(DeliveryStatus::valueOf).orElse(null));

        return ServerResponse.ok().body(orderService.findAll(pageRequest), OrderDTO.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAllOrdersProduct(ServerRequest request) {

        Optional<String> deliveryStatus = request.queryParam("delivery_status");

        Search search = null;

        if(!request.queryParam("keyword").isEmpty()){
            search = new Search(request.queryParams().get("search"), request.queryParam("keyword").orElse(""));
        }

        OrderPageRequest pageRequest = new OrderPageRequest(
                Integer.parseInt(request.queryParam("page_size").orElse("3")),
                Integer.parseInt(request.queryParam("page").orElse("0")),
                new Sort(request.queryParam("sort").orElse("")),
                search, deliveryStatus.map(DeliveryStatus::valueOf).orElse(null));

        return ServerResponse.ok().body(orderService.findAllOrdersProduct(pageRequest), OrderProductDTO.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return ServerResponse.ok().body(orderService.findById(Long.valueOf(serverRequest.pathVariable("id"))), OrderDetailsDTO.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> partialUpdate(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .flatMap(map -> orderService.partialUpdate(Long.valueOf(serverRequest.pathVariable("id")), map))
                .then(Mono.defer(() -> ServerResponse.ok().build()));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> downloadInvoiceById(ServerRequest serverRequest) {
        return orderService.downloadInvoiceById(Long.valueOf(serverRequest.pathVariable("id")))
                .flatMap(fileBytes -> ServerResponse.ok().headers(httpHeaders -> {
                    httpHeaders.setCacheControl(CacheControl.noCache());
                    httpHeaders.setContentDisposition(ContentDisposition.builder("inline")
                            .filename(serverRequest.pathVariable("id") + ".pdf").build());
                    httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                }).bodyValue(fileBytes));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> exportCsv(ServerRequest serverRequest) {
        return orderService.exportCsv(Long.valueOf(serverRequest.pathVariable("id")))
                .flatMap(fileBytes -> ServerResponse.ok().headers(httpHeaders -> {
                    httpHeaders.setCacheControl(CacheControl.noCache());
                    httpHeaders.setContentDisposition(ContentDisposition.builder("inline")
                            .filename("orders" + ".csv").build());
                    httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                }).bodyValue(fileBytes));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> count(ServerRequest serverRequest) {
        return ServerResponse.ok().body(orderService.count(), Long.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findDailyOrderCount(ServerRequest serverRequest){

        Integer days = Integer.valueOf(serverRequest.queryParam("days").orElse(null));
        return orderService.findDailyOrderCount(days)
                .flatMap(dailyOrderCount-> ServerResponse.ok().bodyValue(dailyOrderCount));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findDailySales(ServerRequest serverRequest){

        Integer days = Integer.valueOf(serverRequest.queryParam("days").orElse(null));
        return orderService.findDailySales(days)
                .flatMap(dailyOrderSales-> ServerResponse.ok().bodyValue(dailyOrderSales));
    }
}
