package com.lfhardware.checkout.service;

import com.lfhardware.checkout.dto.CheckoutInput;
import com.lfhardware.provider.service.IProviderService;
import com.stripe.StripeClient;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CheckoutService implements ICheckoutService {

    private final StripeClient stripeClient;

   // private final IUserService userService;

    private final IProviderService providerService;


    public CheckoutService(StripeClient stripeClient,
                           IProviderService providerService) {
        this.stripeClient = stripeClient;
        this.providerService = providerService;
    }


    @Override
    public Mono<Session> createCheckoutSession(String transferGroup, CheckoutInput checkoutInput) {
//        return userService.findById(checkoutInput.getCustomerId())
//                .flatMap(user -> providerService.findDetailsById(checkoutInput.getServiceProviderId())
//                        .flatMap(serviceProviderDetailsDTO -> {
//                            return Mono.fromCallable(() -> {
//                                SessionCreateParams params =
//                                        SessionCreateParams.builder()
//                                                // .putAllMetadata(metadata)
//                                                .setCustomerEmail(user.getEmail())
//                                                .addAllLineItem(
//                                                        checkoutInput.getItems()
//                                                                .stream()
//                                                                .map(item -> {
//                                                                    return SessionCreateParams.LineItem.builder()
//                                                                            .setPriceData(
//                                                                                    SessionCreateParams.LineItem.PriceData.builder()
//                                                                                            .setCurrency(item.getCurrency()
//                                                                                                    .name())
//                                                                                            .setProductData(
//                                                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                                                                                            .setName(item.getServiceName())
//                                                                                                            .build()
//                                                                                            )
//                                                                                            .setUnitAmount(item.getPrice())
//                                                                                            .build()
//                                                                            )
//                                                                            .setQuantity(1L)
//                                                                            .build();
//                                                                })
//                                                                .collect(Collectors.toList())
//                                                )
//                                                .setPaymentIntentData(
//                                                        SessionCreateParams.PaymentIntentData.builder()
//                                                                .setTransferGroup(transferGroup)
//                                                                .build()
//                                                )
//                                                .setInvoiceCreation(
//                                                        SessionCreateParams.InvoiceCreation.builder()
//                                                                .setEnabled(true).build()
//                                                )
//                                                //.setExpiresAt(1719305299L)
////                                                .setAfterExpiration(
////                                                        SessionCreateParams.AfterExpiration.builder()
////                                                                .setRecovery(
////                                                                        SessionCreateParams.AfterExpiration.Recovery.builder()
////                                                                                .setEnabled(true)
////                                                                                .setAllowPromotionCodes(true)
////                                                                                .build()
////                                                                )
////                                                                .build()
////                                                )
//                                                .setCustomerCreation(SessionCreateParams.CustomerCreation.IF_REQUIRED)
//                                                .setMode(SessionCreateParams.Mode.PAYMENT)
//                                                .setSuccessUrl("http://localhost:8090?session_id={CHECKOUT_SESSION_ID}")
//                                                .build();
//
//                                Session session = stripeClient.checkout()
//                                        .sessions()
//                                        .create(params);
//
//
//                                return session;
//                            });
//                        }));
        return Mono.empty();
    }

    @Override
    public Mono<Session> findById(String id){
        return Mono.fromCallable(()->{
            Session session = stripeClient.checkout()
                    .sessions()
                    .retrieve(id);
            return session;
        });
    }

}
