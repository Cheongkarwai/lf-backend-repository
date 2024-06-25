package com.lfhardware.order.service;

import com.lfhardware.auth.dto.Role;
import com.lfhardware.auth.service.IUserService;
import com.lfhardware.cart.domain.CartDetailsId;
import com.lfhardware.cart.repository.ICartDetailsRepository;
import com.lfhardware.file.service.FileService;
import com.lfhardware.notification.dto.NotificationDTO;
import com.lfhardware.notification.service.INotificationService;
import com.lfhardware.order.domain.DeliveryStatus;
import com.lfhardware.order.domain.Order;
import com.lfhardware.order.domain.OrderDetails;
import com.lfhardware.order.domain.PaymentStatus;
import com.lfhardware.order.dto.*;
import com.lfhardware.order.mapper.OrderDetailsMapper;
import com.lfhardware.order.mapper.OrderMapper;
import com.lfhardware.order.repository.IOrderDetailsRepository;
import com.lfhardware.order.repository.IOrderRepository;
import com.lfhardware.shared.Pageable;
import com.lfhardware.shipment.domain.ShippingDetails;
import com.lfhardware.shipment.dto.order.OrderBulkDTO;
import com.lfhardware.shipment.service.IShipmentService;
import com.lfhardware.stock.domain.Stock;
import com.lfhardware.stock.repository.IStockRepository;
import com.lfhardware.stock.service.IStockService;
import org.hibernate.reactive.stage.Stage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;

    private final IOrderDetailsRepository orderDetailsRepository;

    private final IStockRepository stockRepository;

    private final ICartDetailsRepository cartDetailsRepository;

    private final Stage.SessionFactory sessionFactory;

    private final OrderMapper orderMapper;

    private final OrderDetailsMapper orderDetailsMapper;

    private final IUserService userService;

    private final IStockService stockService;

    private final INotificationService notificationService;

    private final FileService pdfService;

    private final FileService csvService;

    private final IShipmentService shipmentService;


    public OrderService(IOrderRepository orderRepository, IOrderDetailsRepository orderDetailsRepository,
                        IStockRepository stockRepository, ICartDetailsRepository cartDetailsRepository, Stage.SessionFactory sessionFactory,
                        OrderMapper orderMapper, OrderDetailsMapper orderDetailsMapper,
                        IUserService userService, IStockService stockService, INotificationService notificationService,
                        @Qualifier("PDFService") FileService pdfService, @Qualifier("CSVService") FileService csvService,
                        IShipmentService shipmentService) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.stockRepository = stockRepository;
        this.cartDetailsRepository = cartDetailsRepository;
        this.sessionFactory = sessionFactory;
        this.orderMapper = orderMapper;
        this.orderDetailsMapper = orderDetailsMapper;
        this.userService = userService;
        this.stockService = stockService;
        this.notificationService = notificationService;
        this.pdfService = pdfService;
        this.csvService = csvService;
        this.shipmentService = shipmentService;
    }

    @PreAuthorize("isFullyAuthenticated()")
    @Override
    public Mono<OrderDetailsDTO> create(OrderInput orderInput) {
        Order order = orderMapper.mapToOrderEntity(orderInput);
        OrderBulkDTO orderBulkDTO = orderMapper.mapToShipmentOrderBulkDTO(orderInput);
        com.lfhardware.shipment.dto.order.OrderInput shipmentOrder = new com.lfhardware.shipment.dto.order.OrderInput();
        shipmentOrder.setBulk(List.of(orderBulkDTO));
        orderBulkDTO.setPickName("Cheong Kar Wai");
        orderBulkDTO.setPickContact("+601128188291");
        orderBulkDTO.setContent("Parcel");
        orderBulkDTO.setPickAddress1("B-2-06, Pangsapuri Meranti");
        orderBulkDTO.setPickAddress2("Jalan Cheras Hartamas");
        orderBulkDTO.setPickCity("Cheras");
        orderBulkDTO.setPickState("sgr");
        orderBulkDTO.setPickCode("43200");
        orderBulkDTO.setPickCountry("MY");
        orderBulkDTO.setSendCountry("MY");

//        return Mono.zip(ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
//                .flatMap(jwt -> userService.findById(jwt.getSubject())).flatMap(userDTO ->
//                        Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> {
//                            order.setUsername(userDTO.getUsername());
//                            return orderRepository.save(session, order)
//                                    .thenCompose(e -> {
//                                        List<OrderDetails> orderDetailsList = new ArrayList<>();
//                                        for (OrderItemInput orderItemDTO : orderInput.items()) {
//                                            Stock stock = stockRepository.loadReferenceById(session, orderItemDTO.stockId());
//                                            OrderDetails orderDetails = new OrderDetails();
//                                            orderDetails.setStock(stock);
//                                            orderDetails.setOrder(order);
//                                            orderDetails.setQuantity(orderItemDTO.quantity());
//                                            orderDetailsList.add(orderDetails);
//                                        }
//                                        return orderDetailsRepository.saveAll(session, orderDetailsList);
//                                    })
//                                    .thenCompose(e -> stockService.updateStockQuantityByOrderItems(orderInput.items()))
//                                    .thenCompose(e -> cartDetailsRepository.deleteAllByIds(session, orderInput.items().stream().map(item -> new CartDetailsId(item.stockId(), item.cartId())).collect(Collectors.toList())))
//                                    .thenApply(e -> null);
//                        }).thenApply(e -> {
//                            notificationService.setNotification(new NotificationDTO(userDTO.getUsername(), "Order is placed", LocalDateTime.now()));
//                            return orderDetailsMapper.mapToOrderDetailsDTO(order);
//                        }))), shipmentService.create(shipmentOrder))
//                .map(objects-> objects.getT1());

        return shipmentService.create(shipmentOrder)
                .flatMap(e-> {

                    System.out.println(e.getErrorRemarks());
                    e.getOrderResult().getOrderFailDTOs().forEach(a->System.out.println(a.getRemarks()));

                    return Mono.empty();
                });

    }

    @Override
    public Mono<Pageable<OrderDTO>> findAll(OrderPageRequest pageRequest) {
        return ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
                .flatMap(jwt -> userService.findById(jwt.getSubject())).flatMap(userDTO -> {
                    CompletionStage<Pageable<OrderDTO>> pageableCompletionStage = sessionFactory.withSession(session -> orderRepository.findAllByUsername(session, pageRequest, userDTO.getUsername()))
                            .thenCombine(sessionFactory.withSession(session -> orderRepository.count(session, pageRequest, userDTO.getUsername())),
                                    (orders, totalElements) -> new Pageable<>(orders.stream().map(orderMapper::mapToOrderDTO).collect(Collectors.toList()), pageRequest.getPageSize(), pageRequest.getPage(), totalElements.intValue()));

                    if (userDTO.getRealmRoles().stream().anyMatch(roleDTO -> roleDTO.equals(Role.administrator.toString()))) {
                        pageableCompletionStage = sessionFactory.withSession(session -> orderRepository.findAll(session, pageRequest))
                                .thenCombine(sessionFactory.withSession(session -> orderRepository.count(session, pageRequest)),
                                        (orders, totalElements) -> new Pageable<>(orders.stream().map(orderMapper::mapToOrderDTO).collect(Collectors.toList()), pageRequest.getPageSize(), pageRequest.getPage(), totalElements.intValue()));
                    }

                    return Mono.fromCompletionStage(pageableCompletionStage);
                });
    }

    @Override
    public Mono<Pageable<OrderProductDTO>> findAllOrdersProduct(OrderPageRequest pageRequest) {
        return ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
                .flatMap(jwt -> userService.findById(jwt.getSubject())).flatMap(userDTO -> {
                    CompletionStage<Pageable<OrderProductDTO>> pageableCompletionStage = sessionFactory.withSession(session -> orderRepository.findAllOrdersProduct(session, pageRequest, userDTO.getUsername()))
                            .thenCombine(sessionFactory.withSession(session -> orderRepository.countOrdersProduct(session, pageRequest, userDTO.getUsername())),
                                    (orders, totalElements) -> new Pageable<>(orders.stream().map(orderDetailsMapper::mapToOrderProductDTO).collect(Collectors.toList()), pageRequest.getPageSize(), pageRequest.getPage(), totalElements.intValue()));

                    if (userDTO.getRealmRoles().stream().anyMatch(roleDTO -> roleDTO.equals(Role.administrator.toString()))) {
                        pageableCompletionStage = sessionFactory.withSession(session -> orderRepository.findAllOrdersProduct(session, pageRequest, ""))
                                .thenCombine(sessionFactory.withSession(session -> orderRepository.countOrdersProduct(session, pageRequest, "")),
                                        (orders, totalElements) -> {
                                            System.out.println(orders);
                                            return new Pageable<>(orders.stream().map(orderDetailsMapper::mapToOrderProductDTO).collect(Collectors.toList()), pageRequest.getPageSize(), pageRequest.getPage(), totalElements.intValue());
                                        });
                    }

                    return Mono.fromCompletionStage(pageableCompletionStage);
                });
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostAuthorize("returnObject.username == authentication.name || hasRole('ADMINISTRATOR')")
    @Override
    public Mono<OrderDetailsDTO> findById(Long id) {

        return Mono.fromCompletionStage(sessionFactory.withSession(session -> orderRepository.findById(session, id)
                .thenApply(orderDetailsMapper::mapToOrderDetailsDTO)));

//        return ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
//                .flatMap(jwt -> {
//
//                    return userService.findById(jwt.getSubject());
//                }).flatMap(userDTO -> {
//
//                    CompletionStage<OrderDetailsDTO> orderDetailsDTOCompletionStage = sessionFactory.withSession(session -> orderRepository.findByIdAndUsername(session, id, userDTO.getUsername())
//                            .thenApply(orderDetailsMapper::mapToOrderDetailsDTO));
//
//                    if (userDTO.getRoles().stream().anyMatch(roleDTO -> roleDTO.getName().equals(Role.ADMINISTRATOR.toString()))) {
//                        orderDetailsDTOCompletionStage = sessionFactory.withSession(session -> orderRepository.findById(session, id)
//                                .thenApply(orderDetailsMapper::mapToOrderDetailsDTO));
//                    }
//
//                    return Mono.fromCompletionStage(orderDetailsDTOCompletionStage);
//                });
    }

//    @PreAuthorize("hasRole("ADMINISTRATOR")")
    @Override
    public Mono<Void> partialUpdate(Long id, Map<String, Object> data) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> orderRepository.findById(session, id)
                .thenCompose(order -> {
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        switch (key) {
                            case "delivery_status" -> order.setDeliveryStatus(DeliveryStatus.valueOf((String) value));
                            case "id" -> order.setId((long) value);
                            case "subtotal" -> order.setSubtotal((BigDecimal) value);
                            case "shipping_fees" -> order.setShippingFees((BigDecimal) value);
                            case "payment_status" -> order.setPaymentStatus(PaymentStatus.valueOf((String) value));
                            case "username" -> order.setUsername((String) value);
                            case "created_at" -> order.setCreatedAt(LocalDateTime.parse((String) value));
                        }
                    }
                    return orderRepository.save(session, order);
                })));
    }

//    @PreAuthorize("hasRole('ADMINISTRATOR')")
//    @PostAuthorize("returnObject.username == authentication.name || hasRole('ADMINISTRATOR')")
    @Override
    public Mono<byte[]> downloadInvoiceById(Long id) {

        return
//                ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
//                .flatMap(jwt -> userService.findById(jwt.getSubject()))
//                .flatMap(userDTO -> {
//                    if (userDTO.getRoles().stream().anyMatch(roleDTO -> roleDTO.getName().equals(Role.ADMINISTRATOR.toString()))) {
//                        return Mono.fromCompletionStage(sessionFactory.withSession(session -> orderRepository.findById(session, id)
//                                .thenApply(orderDetailsMapper::mapToOrderDetailsDTO)));
//                    } else if (userDTO.getRoles().stream().anyMatch(roleDTO -> roleDTO.getName().equals(Role.CUSTOMER.toString()))) {
//                        return Mono.fromCompletionStage(sessionFactory.withSession(session -> orderRepository.findByIdAndUsername(session, id, userDTO.getUsername())
//                                .thenApply(orderDetailsMapper::mapToOrderDetailsDTO)));
//                    }
//                    return Mono.error(new AccessDeniedException("Access is denied"));
//                })
        Mono.fromCompletionStage(sessionFactory.withSession(session -> orderRepository.findById(session, id)
                .thenApply(orderDetailsMapper::mapToOrderDetailsDTO)))
                .flatMap(orderDetails -> {
                    List<OrderItemDTO> orderItems = orderDetails.items();
                    return pdfService.createFile(Map.of("items", orderItems
                            , "orderId", orderDetails.id()));
                });

//        return Mono.fromCompletionStage(sessionFactory.withSession(session-> orderRepository.findByIdAndUsername()))
//
//        return fileService.createFile();
    }

    @Override
    public Mono<byte[]> exportCsv(Long id) {
        return ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
                .flatMap(jwt -> userService.findById(jwt.getSubject()))
                .flatMap(userDTO -> {
                    if (userDTO.getRealmRoles().stream().anyMatch(roleDTO -> roleDTO.equals(Role.administrator.toString()))) {

                        OrderPageRequest orderPageRequest = new OrderPageRequest();
                        orderPageRequest.setPage(0);
                        orderPageRequest.setPageSize(Integer.MAX_VALUE);

                        return Mono.fromCompletionStage(sessionFactory.withSession(session -> orderRepository.findAll(session, orderPageRequest)
                                .thenApply(orders -> orders.stream().map(orderDetailsMapper::mapToOrderDetailsDTO).collect(Collectors.toList()))));
                    }
                    return Mono.error(new AccessDeniedException("Access is denied"));
                }).flatMap(csvService::exportOrders);
    }

    @Override
    public Mono<Long> count() {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> orderRepository.count(session, new OrderPageRequest())));
    }

    @Override
    public Mono<List<DailyOrderStat>> findDailyOrderCount(Integer days) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> orderRepository.countDailyOrderGroupByDays(session, days)));
    }

    @Override
    public Mono<List<DailyOrderStat>> findDailySales(Integer days) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> orderRepository.countDailySalesGroupByDays(session, days)));
    }

}
