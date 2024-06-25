package com.lfhardware.charges.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.repository.IAppointmentRepository;
import com.lfhardware.auth.repository.IUserRepository;
import com.lfhardware.order.domain.PaymentStatus;
import com.lfhardware.order.repository.IOrderRepository;
import com.lfhardware.order.service.IOrderService;
import com.lfhardware.product.repository.IProductRepository;
import com.lfhardware.product.repository.ProductRepository;
import com.lfhardware.provider.repository.IProviderRepository;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class EventService {

    private final Stage.SessionFactory sessionFactory;
    private final IProductRepository productRepository;

    private final StripeClient stripeClient;

    private final ObjectMapper objectMapper;

    private final IOrderRepository orderRepository;

    private final IUserRepository userRepository;

    private final IProviderRepository providerRepository;

    private final IAppointmentRepository appointmentRepository;
    public EventService(
            Stage.SessionFactory sessionFactory,
            IProductRepository productRepository,
            StripeClient stripeClient,
            ObjectMapper objectMapper,
            IOrderRepository orderRepository,
            IUserRepository userRepository,
            IProviderRepository providerRepository,
            IAppointmentRepository appointmentRepository) {
        this.sessionFactory = sessionFactory;
        this.productRepository = productRepository;
        this.stripeClient = stripeClient;
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.providerRepository = providerRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Mono<Void> handleWebhookEvent(Event event) {

        System.out.println(event.getType());

        if (event.getType().equals("account.updated")) {
            Account account = (Account) event.getData().getObject();
            System.out.println("Contains user_id" + account.getMetadata().containsKey("user_id"));
            if (account.getMetadata().containsKey("user_id")) {
                return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> providerRepository.findById(session, account.getMetadata().get("user_id"))
                        .thenCompose(provider -> {
                            provider.setStripeAccountId(account.getId());
                            return providerRepository.save(session, provider);
                        })));
            }
        }
        if(event.getType().equals("charge.succeeded")){

        }
        if(event.getType().equals("checkout.session.completed")){
            Session session = (Session) event.getData().getObject();
            System.out.println("Payment Intent" +session.getPaymentIntent());
            return Mono.fromCompletionStage(sessionFactory.withTransaction(session1 -> {
                AppointmentId appointmentId = new AppointmentId();

                String serviceId = session.getMetadata().get("service_id");
                String dateId = session.getMetadata().get("current_datetime");
                String customerId = session.getMetadata().get("customer_id");
                String serviceProviderId = session.getMetadata().get("service_provider_id");

                appointmentId.setServiceId(Long.valueOf(serviceId));
                appointmentId.setCreatedAt(LocalDateTime.parse(dateId));
                appointmentId.setCustomerId(customerId);
                appointmentId.setServiceProviderId(serviceProviderId);
                return appointmentRepository.findById(session1, appointmentId)
                        .thenCompose(appointment->{
                            appointment.setPaid(true);
                            return appointmentRepository.save(session1, appointment);
                        });
            }));

        }
        if(event.getType().equals("payment_intent.created")){

        }
        if(event.getType().equals("payment_intent.succeeded")){

        }
        if(event.getType().equals("payment_intent.created")){

        }

        return switch (event.getType()) {

            case "account.updated" -> {


                yield Mono.empty();
            }
            case "product.created" -> {
//                Product stripeProduct = (Product) event.getData().getObject();
//                Price price = null;
//                try {
//                    price = stripeClient.prices().retrieve(stripeProduct.getDefaultPrice());
//                } catch (StripeException e) {
//                    throw new RuntimeException(e);
//                }
//                com.lfhardware.product.domain.Product product = new com.lfhardware.product.domain.Product();
//                product.setId(stripeProduct.getId());
//                product.setName(stripeProduct.getName());
//                product.setPrice(price.getUnitAmountDecimal() != null ? price.getUnitAmountDecimal() : BigDecimal.ZERO);
//                product.setDescription(stripeProduct.getDescription());
//                yield Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> productRepository.save(session,product)))
//                        .doOnError(e-> {
//                            System.out.println(e.getMessage());
//                        })
//                        .then();
                yield Mono.empty();
            }
            case "price.created" -> {
//                Price price = (Price) event.getData().getObject();
//                yield Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> productRepository.findById(session,price.getProduct())
//                                .thenCompose(product->{
//                                    product.setPrice(price.getUnitAmountDecimal());
//                                    return productRepository.merge(session,product);
//                                })))
//                        .doOnError(e-> {
//                            System.out.println(e.getMessage());
//                        })
//                        .then();
                yield Mono.empty();
            }

            case "invoice.created" -> {
                Invoice invoice = (Invoice) event.getData().getObject();
                yield Mono.empty();
            }
            case "payment_intent.succeeded" -> {
                // Then define and call a function to handle the event payment_intent.succeeded
//                System.out.println("Payment Intent Succeeded");
//                yield Mono.fromCompletionStage(sessionFactory.withTransaction((session,transaction) -> orderRepository.findById(session, Long.valueOf("12"))
//                                .thenCompose(order -> {
//                                    System.out.println(order.getPaymentStatus());
//                                    order.setPaymentStatus(PaymentStatus.PAID);
//                                    return orderRepository.save(session, order);
//                                })))
//                        .then();
                PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
                yield Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> orderRepository.findById(session, Long.valueOf(paymentIntent.getMetadata().get("order_id")))
                        .thenCompose(order -> {
                            log.info("Order Status : {}", order.getPaymentStatus());
                            order.setPaymentStatus(PaymentStatus.PAID);
                            return orderRepository.save(session, order);
                        }))
                ).then();
            }
            case "charge.succeeded" -> {
                yield Mono.empty();
            }
            case "payment_intent.created" -> {
                yield Mono.empty();
            }
            // ... handle other event types
            default -> {
                System.out.println("Unhandled event type: " + event.getType());
                yield Mono.empty();
            }
        };
    }
}
