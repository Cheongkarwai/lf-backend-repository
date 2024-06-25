package com.lfhardware.checkout.service;

import com.lfhardware.appointment.repository.IAppointmentRepository;
import com.lfhardware.appointment.service.IAppointmentService;
import com.lfhardware.auth.service.IUserService;
import com.lfhardware.checkout.dto.CheckoutInput;
import com.lfhardware.provider.service.IProviderService;
import com.stripe.StripeClient;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckoutService implements ICheckoutService {

    private final StripeClient stripeClient;

    private final IUserService userService;

    private final IProviderService providerService;


    public CheckoutService(StripeClient stripeClient, IUserService userService,
                           IProviderService providerService) {
        this.stripeClient = stripeClient;
        this.userService = userService;
        this.providerService = providerService;
    }

    @Override
    public Mono<String> createCheckoutSession(CheckoutInput checkoutInput) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    return userService.findById(authentication.getName())
                            .flatMap(user -> {
                                return providerService.findDetailsById(checkoutInput.getServiceProviderId())
                                        .flatMap(serviceProviderDetailsDTO -> {
                                            return Mono.fromCallable(() -> {
                                                SessionCreateParams params =
                                                        SessionCreateParams.builder()
                                                                // .putAllMetadata(metadata)
                                                                .setCustomerEmail(user.getEmail())
                                                                .addAllLineItem(
                                                                        checkoutInput.getItems()
                                                                                .stream()
                                                                                .map(item -> {
                                                                                    return SessionCreateParams.LineItem.builder()
                                                                                            .setPriceData(
                                                                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                                                                            .setCurrency(item.getCurrency()
                                                                                                                    .name())
                                                                                                            .setProductData(
                                                                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                                                                            .setName(item.getServiceName())
                                                                                                                            .build()
                                                                                                            )
                                                                                                            .setUnitAmountDecimal(item.getPrice())
                                                                                                            .build()
                                                                                            )
                                                                                            .setQuantity(1L)
                                                                                            .build();
                                                                                })
                                                                                .collect(Collectors.toList())
                                                                )
                                                                .setPaymentIntentData(
                                                                        SessionCreateParams.PaymentIntentData.builder()
                                                                                //.setApplicationFeeAmount(checkoutInput.getProcessingFees()
                                                                                    //    .longValue())
                                                                                        .setTransferGroup(checkoutInput.getServiceProviderId()).build()
                                                                )
//                                                                .setPaymentIntentData(
//                                                                        SessionCreateParams.PaymentIntentData
//                                                                                .builder()
//                                                                                .setApplicationFeeAmount(checkoutInput.getProcessingFees()
//                                                                                        .longValue())
//                                                                                .setTransferData(
//                                                                                        SessionCreateParams.PaymentIntentData.TransferData.builder()
//                                                                                                .setDestination(serviceProviderDetailsDTO.stripeAccountId())
//                                                                                                .build()
//                                                                                )
//                                                                                .build()
//                                                                )
                                                                .setMode(SessionCreateParams.Mode.PAYMENT)
                                                                .setSuccessUrl("http://localhost:8090?session_id={CHECKOUT_SESSION_ID}")
                                                                .build();

                                                Session session = stripeClient.checkout()
                                                        .sessions()
                                                        .create(params);

                                                return session.getUrl();
                                            });
                                        });
                            });

                });
    }
}
