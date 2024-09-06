package com.lfhardware.provider.api;

import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.form.domain.FormId;
import com.lfhardware.form.dto.FormDTO;
import com.lfhardware.form.dto.FormInput;
import com.lfhardware.form.service.IFormService;
import com.lfhardware.provider.domain.Status;
import com.lfhardware.provider.dto.*;
import com.lfhardware.provider.service.IProviderService;
import com.lfhardware.provider_business.dto.ServiceDTO;
import com.lfhardware.review.domain.ReviewInput;
import com.lfhardware.shared.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class ProvideApi {

    private final IProviderService providerService;

    private final IFormService formService;


    public ProvideApi(IProviderService providerService, IFormService formService) {
        this.providerService = providerService;
        this.formService = formService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {

        PageInfo pageRequest = PageQueryParameterBuilder.buildPageRequest(serverRequest);

        Double rating = null;
        Optional<String> ratingOptional = serverRequest.queryParam("rating");
        String serviceName = serverRequest.queryParam("service_name")
                .orElse(null);
        List<String> status = serverRequest.queryParams()
                .get("status");
        List<String> states = serverRequest.queryParams()
                .get("state");

        if (ratingOptional.isPresent()) {
            rating = Double.parseDouble(ratingOptional.get());
        }

        return providerService.findAll(pageRequest, status, states, rating, serviceName)
                .flatMap(serviceProvider -> ServerResponse.ok()
                        .bodyValue(serviceProvider));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAllDetails(ServerRequest request) {

        Search search = null;

        if (!request.queryParam("keyword")
                .isEmpty()) {
            search = new Search(request.queryParams()
                    .get("search"), request.queryParam("keyword")
                    .orElse(""));
        }

        ServiceProviderPageRequest serviceProviderPageRequest = new ServiceProviderPageRequest(Integer.parseInt(request.queryParam("page_size")
                .orElse("3")),
                Integer.parseInt(request.queryParam("page")
                        .orElse("0")),
                new Sort(request.queryParam("sort")
                        .orElse("")),
                search);

        Optional<String> minPrice = request.queryParam("min_price");
        Optional<String> maxPrice = request.queryParam("max_price");
        Optional<String> rating = request.queryParam("rating");
        Optional<String> serviceName = request.queryParam("service_name");
        Optional<String> status = request.queryParam("status");
        List<String> states = request.queryParams()
                .get("state");

        minPrice.ifPresent(s -> serviceProviderPageRequest.setMinPrice(new BigDecimal(s)));
        maxPrice.ifPresent(s -> serviceProviderPageRequest.setMaxPrice(new BigDecimal(s)));
        rating.ifPresent(s -> serviceProviderPageRequest.setRating(Double.valueOf(s)));
        serviceName.ifPresent(serviceProviderPageRequest::setServiceName);
        status.ifPresent(serviceProviderPageRequest::setStatus);
        if (states != null && !states.isEmpty()) {
            serviceProviderPageRequest.setStates(states);
        }

        return providerService.findAllDetails(serviceProviderPageRequest)
                .flatMap(serviceProvider -> ServerResponse.ok()
                        .bodyValue(serviceProvider));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(providerService.findDetailsById(serverRequest.pathVariable("id")), ServiceProviderDetailsDTO.class)
                .onErrorResume(throwable -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ErrorResponse(throwable.getMessage(), serverRequest.path())));
    }

    public Mono<ServerResponse> patch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .flatMap(data -> providerService.patch(serverRequest.pathVariable("id"), data))
                .then(Mono.defer(() -> ServerResponse.ok()
                        .build()));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> updateStatus(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(new ParameterizedTypeReference<Map<String, Status>>() {
                })
                .flatMap(status -> providerService.updateStatus(serverRequest.pathVariable("id"), status.get("status")))
                .then(Mono.defer(() -> ServerResponse.noContent()
                        .build()));
    }

//    public Mono<ServerResponse> createForm(ServerRequest serverRequest) {
//        return serverRequest.bodyToMono(FormInput.class)
//                .flatMap(e-> formService(e.getServiceId(), e))
//                .then(Mono.defer(() -> ServerResponse.noContent()
//                        .build()));
//
//    }

//    public Mono<ServerResponse> findProviderFormsByServiceId(ServerRequest serverRequest) {
//        return ServerResponse.ok()
//                .body(formService.findById(Long.valueOf(serverRequest.pathVariable("serviceId"))),
//                        FormDTO.class);
//    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findCurrentProviderBusinesses(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(providerService.findAllCurrentProviderServices(), ServiceDTO.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findCurrentProviderForm(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(providerService.findCurrentProviderForm(Long.valueOf(serverRequest.pathVariable("serviceId")))
                        , FormDTO.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findCurrentProviderAppointments(ServerRequest serverRequest) {

        //Handle Page
        PageInfo pageRequest = PageQueryParameterBuilder.buildPageRequest(serverRequest);

        return ServerResponse.ok()
                .body(providerService.findAllCurrentProviderAppointments(pageRequest, serverRequest.queryParams()
                        .get("status")), Pageable.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ServiceProviderOnboardInput.class)
                .flatMap(providerService::save)
                .then(Mono.defer(() -> ServerResponse.noContent()
                        .build()))
                .log();

    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findServiceProviderByCurrentUser(ServerRequest serverRequest) {
        return providerService.findCurrentServiceProviderByUserId()
                .flatMap(serviceProvider -> ServerResponse.ok()
                        .bodyValue(serviceProvider))
                .log();
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findServiceProviderReviewsById(ServerRequest serverRequest) {

        PageInfo pageRequest = PageQueryParameterBuilder.buildPageRequest(serverRequest);
        String rating = serverRequest.queryParam("rating")
                .orElse(null);
        return ServerResponse.ok()
                .body(providerService.findAllServiceProviderReviewsById(serverRequest.pathVariable("id"),
                                pageRequest,
                                Objects.nonNull(rating) ? Double.parseDouble(rating) : null),
                        Pageable.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> countServiceProviderReviewsById(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(providerService.countServiceProviderReviewsById(serverRequest.pathVariable("id")), Long.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> countCurrentServiceProviderReviews(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(providerService.countCurrentServiceProviderReviews(), List.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> countCurrentServiceProviderAppointment(ServerRequest serverRequest) {
        String status = serverRequest.queryParam("status")
                .orElse(null);
        String day = serverRequest.queryParam("day")
                .orElse(null);

        return ServerResponse.ok()
                .body(providerService.countCurrentServiceProviderAppointments(
                        Objects.nonNull(status) ? AppointmentStatus.valueOf(status) : null,
                        Objects.nonNull(day) ? Integer.valueOf(day) : null), List.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> countServiceProvider(ServerRequest serverRequest) {
        String day = serverRequest.queryParam("day")
                .orElse(null);

        return ServerResponse.ok()
                .body(providerService.countServiceProviders(Objects.nonNull(day) ? Integer.valueOf(day) : null), List.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findServiceProviderServicesById(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(providerService.findAllProviderServicesById(serverRequest.pathVariable("serviceProviderId")), ServiceDTO.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findPaymentAccountStatus(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(providerService.findPaymentAccountStatus(), Boolean.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> updateById(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ServiceProviderDetailsInput.class)
                .flatMap(serviceProviderDetailsInput ->
                        providerService.updateById(serverRequest.pathVariable("id"), serviceProviderDetailsInput))
                .then(ServerResponse.noContent()
                        .build());
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> createPaymentDetails(ServerRequest serverRequest) {
        return providerService.createPaymentDetails()
                .flatMap(link -> ServerResponse.ok()
                        .bodyValue(link));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> saveServiceProviderReview(ServerRequest serverRequest) {
        String serviceProviderId = serverRequest.pathVariable("serviceProviderId");
        Long serviceId = Long.parseLong(serverRequest.pathVariable("serviceId"));
        String customerId = serverRequest.pathVariable("customerId");
        LocalDateTime createdAt = LocalDateTime.parse(serverRequest.pathVariable("createdAt"));
        return serverRequest.bodyToMono(ServiceProviderReviewInput.class)
                .flatMap(serviceProviderReviewInput -> providerService.saveServiceProviderReview(serverRequest.pathVariable("id"),
                        new AppointmentId(serviceId, serviceProviderId, customerId, createdAt), serviceProviderReviewInput))
                .then(Mono.defer(() -> ServerResponse.ok()
                        .build()));
    }
}
