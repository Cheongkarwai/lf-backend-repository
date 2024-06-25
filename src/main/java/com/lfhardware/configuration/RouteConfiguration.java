package com.lfhardware.configuration;

import com.lfhardware.account.api.AccountApi;
import com.lfhardware.appointment.api.AppointmentApi;
import com.lfhardware.auth.api.AuthHandler;
import com.lfhardware.customer.api.CustomerApi;
import com.lfhardware.auth.api.PermissionApi;
import com.lfhardware.auth.api.UserApi;
import com.lfhardware.cart.api.CartHandler;
import com.lfhardware.charges.api.PaymentHandler;
import com.lfhardware.checkout.api.CheckoutApi;
import com.lfhardware.city.api.CityHandler;
import com.lfhardware.country.api.CountryHandler;
import com.lfhardware.faq.api.FaqApi;
import com.lfhardware.file.api.FileApi;
import com.lfhardware.form.api.FormApi;
import com.lfhardware.invoice.api.InvoiceHandler;
import com.lfhardware.notification.api.NotificationApi;
import com.lfhardware.order.api.OrderApi;
import com.lfhardware.product.api.ProductApi;
import com.lfhardware.provider.api.ProvideApi;
import com.lfhardware.provider_business.api.ProviderBusinessHandler;
import com.lfhardware.report.api.ReportApi;
import com.lfhardware.shipment.api.ShipmentApi;
import com.lfhardware.state.api.StateHandler;
import com.lfhardware.transaction.handler.TransactionHandler;
import com.lfhardware.transfer.api.TransferApi;
import com.sun.nio.sctp.NotificationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouteConfiguration {
    @Bean
    public RouterFunction<ServerResponse> productRouter(ProductApi productHandler) {
        return RouterFunctions.route()
                .path("/api/v1/products",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA),
                                route -> route
                                        .GET("/name/{name}", productHandler::findByName)
                                        .GET("/categories", productHandler::findAllProductCategory)
                                        .GET("/brands", productHandler::findAllProductBrand)
                                        .GET("/{id}", productHandler::findById)
                                        .GET(productHandler::findAll)
                                        .POST(productHandler::save)
                                        .PUT("/{id}", RequestPredicates.contentType(MediaType.MULTIPART_FORM_DATA), productHandler::updateById)
                                        .DELETE("/{id}", productHandler::deleteById)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> checkoutRouter(CheckoutApi checkoutApi) {
        return RouterFunctions.route()
                .path("/api/v1/checkout",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .POST("/sessions", checkoutApi::createCheckoutSession)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> transferRouter(TransferApi transferApi) {
        return RouterFunctions.route()
                .path("/api/v1/transfers",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .POST(transferApi::transferPayment)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> paymentRouter(PaymentHandler paymentHandler) {
        return RouterFunctions.route()
                .path("/api/v1/payments",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .POST("/checkout", paymentHandler::createCheckoutSession)
                                        .POST("/webhook", paymentHandler::stripeWebhook)
                                        .POST("/payment-intents", paymentHandler::createPaymentIntent)
//                                        .POST(paymentHandler::charge)
                                        .GET("/supported-payment-methods", paymentHandler::findAllPaymentMethods)
                                        .GET("/{paymentMethod}/supported-banks", paymentHandler::findAllAvailableBank)
                                        .PATCH("/payment-intents/{id}/metadata", paymentHandler::addPaymentIntentMetadata)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> transactionRouter(TransactionHandler transactionHandler) {
        return RouterFunctions.route()
                .path("/api/v1/transactions",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .GET("/count", transactionHandler::count)
                                        .GET("/{id}", transactionHandler::findById)
                                        .GET(transactionHandler::findAll)
                        ))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> cartRouter(CartHandler cartHandler) {
        return RouterFunctions.route()
                .path("/api/v1/carts",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route ->
                                        route.GET(cartHandler::findCart)
                                                .POST("/items", cartHandler::saveCartItem)
                                                .PUT("/items/{cartId}/{stockId}", cartHandler::updateCartItemQuantity)
                                                .DELETE("/items/{cartId}/{stockId}", cartHandler::deleteCartItem)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> authRouter(AuthHandler authHandler, PermissionApi permissionApi) {
        return RouterFunctions.route()
                .path("/api/v1/auth",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route ->
                                        route
                                                .POST("/permissions/orders", permissionApi::createPermission)
                                                .POST("/login", authHandler::login)
                                                .POST("/register", authHandler::register)
                                                .POST("/account-recovery-email", authHandler::sendAccountRecoveryEmail)
                                                .POST("/change-password", authHandler::changePassword)
//                                        .POST("/otp-login",authHandler::otpLogin)
                        ))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> cityRouter(CityHandler cityHandler) {
        return RouterFunctions.route()
                .path("/api/v1/cities",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route.GET(cityHandler::findAll)
                        ))

                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> countryRouter(CountryHandler countryHandler) {
        return RouterFunctions.route()
                .path("/api/v1/countries",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route.GET(countryHandler::findAll)
                        ))

                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> stateRouter(StateHandler stateHandler) {
        return RouterFunctions.route()
                .path("/api/v1/states",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route.GET(stateHandler::findAll)
                        ))

                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> accountRouter(AccountApi accountApi) {
        return RouterFunctions.route()
                .path("/api/v1/accounts",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .PUT("/reset-password", accountApi::resetPassword)
                                        .GET("/test", accountApi::test)
                                        .GET("/me", accountApi::findCurrentlyLoggedInUserAccount)
                                        .GET("/otp/qr-code", accountApi::generateOtpQrCode)
                                        .GET("/credentials", accountApi::findAllUserCredentials)
                                        .POST("/otp", accountApi::setupOtpVerification)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> customerRouter(CustomerApi customerApi) {
        return RouterFunctions.route()
                .path("/api/v1/customers",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .GET("/me/appointments/{serviceId}/{serviceProviderId}/{createdAt}", customerApi::findCurrentCustomerAppointmentById)
                                        .GET("/me/appointments", customerApi::findCurrentCustomerAppointments)
                                        .GET("/{id}/appointments", customerApi::findAppointmentsByCustomerId)
                                        .GET("/me", customerApi::findCurrentCustomer)
                                        .GET("/count", customerApi::count)
                                        .GET("/{id}", customerApi::findById)
                                        .GET(customerApi::findAll)
                                        .POST(customerApi::save)
                                        .PUT("/{id}", customerApi::update)
                        ))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userRouter(UserApi userHandler) {
        return RouterFunctions.route()
                .path("/api/v1/users",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .GET("/account/me", userHandler::findCurrentlyLoggedInUser)
                                        .GET("/roles/me", userHandler::findCurrentlyLoggedInUserRoles)
                                        .GET("/phone-number/{phoneNumber}", userHandler::findUserByPhoneNumber)
                                        .GET("/{username}/account", userHandler::findUserAccountByUsername)
                                        .GET("/{username}/roles", userHandler::findUserRole)
                                        .GET("/daily-user-count", userHandler::findDailyUserCount)
                                        .GET("/count", userHandler::count)
                                        .GET("/{username}", userHandler::findUser)
                                        .GET(userHandler::findAll)
                                        .POST("/verify-email", userHandler::verifyEmail)
                                        .POST("/account-recovery-email", userHandler::sendAccountRecoveryEmail)
                                        .POST("/roles/service-provider", userHandler::registerServiceProvider)
                                        .POST("/register", userHandler::register)
                                        .PUT("/{username}", userHandler::updateUser)
                        ))

                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> serviceRouter(ProviderBusinessHandler providerBusinessHandler) {
        return RouterFunctions.route()
                .path("/api/v1/services",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .GET("/{id}/details", providerBusinessHandler::findDetailsById)
                                        .GET("/details", providerBusinessHandler::findAllServices)
                                        .GET(providerBusinessHandler::findAllServices)
                        ))

                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> formRouter(FormApi formApi) {
        return RouterFunctions.route()
                .path("/api/v1/forms",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .POST("/quote-form", formApi::save)
                                        .GET("/quote-form/service-providers/{service_provider_id}/services/{service_id}", formApi::findByFormId)
                                        .GET("/quote-form", formApi::findAll)
                        ))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> appointmentRouter(AppointmentApi appointmentApi) {
        return RouterFunctions.route()
                .path("/api/v1/appointments",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .GET("/{serviceId}/{serviceProviderId}/{customerId}/{createdAt}", appointmentApi::findById)
                                        .GET("/count", appointmentApi::count)
                                        .GET(appointmentApi::findAll)
                                        .POST("/{serviceId}/{serviceProviderId}/{customerId}/{createdAt}/fees", appointmentApi::payAppointmentFees)
                                        .POST(appointmentApi::createAppointment)
                                        .PUT("/status", appointmentApi::updateAppointmentStatus)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> providerRouter(ProvideApi providerHandler) {
        return RouterFunctions.route()
                .path("/api/v1/service-providers",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route
                                        .GET("/{serviceProviderId}/services/{serviceId}/forms", providerHandler::findProviderFormsByServiceId)
                                        .GET("/services/{serviceId}/forms", providerHandler::findCurrentProviderForm)
                                        .GET("/me/appointments/count", providerHandler::countCurrentServiceProviderAppointment)
                                        .GET("/me/reviews/count", providerHandler::countCurrentServiceProviderReviews)
                                        .GET("/{id}/reviews/count", providerHandler::countServiceProviderReviewsById)
                                        .GET("/details/{id}", providerHandler::findById)
                                        .GET("/{id}/reviews", providerHandler::findServiceProviderReviewsById)
                                        .GET("/{serviceProviderId}/services", providerHandler::findServiceProviderServicesById)
                                        .GET("/me/payment-accounts/status", providerHandler::findPaymentAccountStatus)
                                        .GET("/me", providerHandler::findServiceProviderByCurrentUser)
                                        .GET("/appointments", providerHandler::findCurrentProviderAppointments)
                                        .GET("/details", providerHandler::findAllDetails)
                                        .GET("/services", providerHandler::findCurrentProviderBusinesses)
                                        .GET("/count", providerHandler::countServiceProvider)
                                        .GET(providerHandler::findAll)
                                        .POST("/{service-provider}/services/{service}/forms", providerHandler::createForm)
                                        .POST("/me/payment-accounts", providerHandler::createPaymentDetails)
                                        .POST(providerHandler::save)
                                        .PUT("/{id}/status", providerHandler::updateStatus)
                                        .PUT("/{id}", providerHandler::updateById)
                                        .PATCH("/{id}", providerHandler::patch)
                        ))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderRouter(OrderApi orderApi) {
        return RouterFunctions.route()
                .path("/api/v1/orders",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                        route -> route
                                                .POST("/{id}/pdf", orderApi::downloadInvoiceById))
                                .POST("/csv", orderApi::exportCsv)
                                .POST(orderApi::create)
                                .GET("/daily-order-count", orderApi::findDailyOrderCount)
                                .GET("/daily-sales", orderApi::findDailySales)
                                .GET("/count", orderApi::count)
                                .GET("/products", orderApi::findAllOrdersProduct)
                                .GET("/{id}", orderApi::findById)
                                .GET(orderApi::findAllOrders)
                                .PATCH("/{id}", orderApi::partialUpdate)

                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> invoiceRouter(InvoiceHandler invoiceHandler) {
        return RouterFunctions.route()
                .path("/api/v1/invoices",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route.POST(invoiceHandler::createInvoice)

                        ))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> shipmentRouter(ShipmentApi shipmentApi) {
        return RouterFunctions.route()
                .path("/api/v1/shipments",
                        builder -> builder
                                .POST("/orders", shipmentApi::createOrder)
                                .POST(shipmentApi::getAllAvailableShipments))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> reportRouter(ReportApi reportApi) {
        return RouterFunctions.route()
                .path("/api/v1/reports",
                        builder -> builder.GET("/monthly-sales", reportApi::findSales))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fileRouter(FileApi fileApi) {
        return RouterFunctions.route()
                .path("/api/v1/files",

                        builder ->
                                builder.POST("/products/upload", fileApi::uploadProductFile)
                                        .POST("/appointments/{id}/{serviceId}/{serviceProviderId}/{customerId}/{createdAt}/evidences/upload", fileApi::uploadCompleteAppointmentEvidences)
                                        .POST("/service-providers/documents/upload", fileApi::uploadServiceProviderDocument)
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> faqRouter(FaqApi faqApi) {
        return RouterFunctions.route()
                .path("/api/v1/faqs",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route -> route.GET(faqApi::findAll)
                                        .POST(faqApi::save)
                                        .DELETE("/{id}", faqApi::deleteById)
                                        .PUT("/{id}", faqApi::updateById)
                        ))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationRouter(NotificationApi notificationApi) {
        return RouterFunctions.route()
                .path("/api/v1/notifications", builder ->
                        builder.GET(RequestPredicates.accept(MediaType.TEXT_EVENT_STREAM),
                                notificationApi::findAll))
                .POST(RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                        notificationApi::save)
                .build();

    }

}
