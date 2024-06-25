package com.lfhardware.file.service;

import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.domain.AppointmentImage;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.appointment.repository.IAppointmentImageRepository;
import com.lfhardware.appointment.repository.IAppointmentRepository;
import com.lfhardware.file.dto.ImageDTO;
import com.lfhardware.order.dto.OrderDetailsDTO;
import com.lfhardware.provider.repository.IProviderRepository;
import com.lfhardware.provider.repository.ProviderRepository;
import com.lfhardware.shared.CacheService;
import org.hibernate.reactive.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageService implements FileService<Mono<MultiValueMap<String, Part>>, Flux<ImageDTO>> {

    private final S3AsyncClient s3AsyncClient;

    @Value("${digitalocean.bucket}")
    private String bucket;

    @Value("${digitalocean.region}")
    private String region;

    private final Stage.SessionFactory sessionFactory;

    private final IProviderRepository providerRepository;

    private final IAppointmentRepository appointmentRepository;

    private final IAppointmentImageRepository appointmentImageRepository;

    private final  CacheService<AppointmentDTO>  appointmentCacheService;

    public ImageService(S3AsyncClient s3AsyncClient,
                        Stage.SessionFactory sessionFactory,
                        ProviderRepository providerRepository,
                        IAppointmentRepository appointmentRepository,
                        IAppointmentImageRepository appointmentImageRepository,
                        CacheService<AppointmentDTO> appointmentCacheService) {
        this.s3AsyncClient = s3AsyncClient;
        this.sessionFactory = sessionFactory;
        this.providerRepository = providerRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentImageRepository = appointmentImageRepository;
        this.appointmentCacheService = appointmentCacheService;
    }

    @Override
    public Mono<byte[]> exportOrders(List<OrderDetailsDTO> orders) {
        return null;
    }

    @Override
    public Mono<byte[]> createFile(Map<String, Object> content) {
        return null;
    }

    @Override
    public Flux<ImageDTO> upload(Mono<MultiValueMap<String, Part>> formData) {


        Flux<ImageDTO> productImageFlux = formData.map(map -> map.get("product_image"))
                .flatMapMany(Flux::fromIterable)
                .flatMap(filePart -> {

                    StringBuffer path = new StringBuffer("product/" + UUID.randomUUID()
                            .toString());

                    return filePart.content()
                            .map(dataBuffer -> dataBuffer.readableByteCount())
                            .reduce(Integer::sum)
                            .flatMap(totalBytes -> Mono.fromFuture(s3AsyncClient.putObject(PutObjectRequest.builder()
                                    .contentType(filePart.headers()
                                            .getContentType()
                                            .toString())
                                    .bucket(bucket)
                                    .acl(ObjectCannedACL.PUBLIC_READ)
                                    .contentLength(Long.valueOf(totalBytes))
                                    .key(path.toString())
                                    .build(), AsyncRequestBody.fromPublisher(filePart.content()
                                    .map(DataBuffer::asByteBuffer)))))
                            .map(e -> new ImageDTO("https://" + bucket + "." + region + "." + "digitaloceanspaces.com/" + path.toString(), "productImage"));

                });

        Flux<ImageDTO> productDetailsImagesFlux = formData.map(map -> map.get("product_details_images"))
                .flatMapMany(Flux::fromIterable)
                .flatMap(filePart -> {

                    StringBuffer path = new StringBuffer("product/" + UUID.randomUUID()
                            .toString());

                    return filePart.content()
                            .map(dataBuffer -> dataBuffer.readableByteCount())
                            .reduce(Integer::sum)
                            .flatMap(totalBytes -> Mono.fromFuture(s3AsyncClient.putObject(PutObjectRequest.builder()
                                    .contentType(filePart.headers()
                                            .getContentType()
                                            .toString())
                                    .bucket(bucket)
                                    .contentLength(Long.valueOf(totalBytes))
                                    .key(path.toString())
                                    .build(), AsyncRequestBody.fromPublisher(filePart.content()
                                    .map(DataBuffer::asByteBuffer)))))
                            .map(e -> new ImageDTO("https://" + bucket + "." + region + "." + "digitaloceanspaces.com/" + path.toString(), "productGroupImage"));

                });

        return productImageFlux.concatWith(productDetailsImagesFlux);
    }

    @Override
    public Flux<ImageDTO> uploadServiceProviderDocuments(Mono<MultiValueMap<String, Part>> formData) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMapMany(serviceProviderId -> {
                    Flux<ImageDTO> frontIdentityCardFlux = formData.map(map -> map.get("front_identity_card"))
                            .flatMapMany(Flux::fromIterable)
                            .flatMap(filePart -> {

                                StringBuffer path = new StringBuffer("service_provider/" + serviceProviderId + "/front-identity-card");

                                return filePart.content()
                                        .map(DataBuffer::readableByteCount)

                                        .reduce(Integer::sum)
                                        .flatMap(totalBytes -> Mono.fromFuture(s3AsyncClient.putObject(PutObjectRequest.builder()
                                                .contentType(filePart.headers()
                                                        .getContentType()
                                                        .toString())
                                                .bucket(bucket)
                                                .acl(ObjectCannedACL.PUBLIC_READ)
                                                .contentLength(Long.valueOf(totalBytes))
                                                .key(path.toString())
                                                .build(), AsyncRequestBody.fromPublisher(filePart.content()
                                                .map(DataBuffer::asByteBuffer)))))
                                        .map(e -> new ImageDTO("https://" + bucket + "." + region + "." + "digitaloceanspaces.com/" + path, "front_identity_card"));

                            });

                    Flux<ImageDTO> backIdentityCardFlux = formData.map(map -> map.get("back_identity_card"))
                            .flatMapMany(Flux::fromIterable)
                            .flatMap(filePart -> {

                                StringBuffer path = new StringBuffer("service_provider/" + serviceProviderId + "/back-identity-card");

                                return filePart.content()
                                        .map(DataBuffer::readableByteCount)
                                        .reduce(Integer::sum)
                                        .flatMap(totalBytes -> Mono.fromFuture(s3AsyncClient.putObject(PutObjectRequest.builder()
                                                .contentType(filePart.headers()
                                                        .getContentType()
                                                        .toString())
                                                .bucket(bucket)
                                                .contentLength(Long.valueOf(totalBytes))
                                                .key(path.toString())
                                                .build(), AsyncRequestBody.fromPublisher(filePart.content()
                                                .map(DataBuffer::asByteBuffer)))))
                                        .map(e -> new ImageDTO("https://" + bucket + "." + region + "." + "digitaloceanspaces.com/" + path.toString(), "back_identity_card"));

                            });

                    Flux<ImageDTO> ssmFlux = formData.map(map -> map.get("ssm"))
                            .flatMapMany(Flux::fromIterable)
                            .flatMap(filePart -> {

                                StringBuffer path = new StringBuffer("service_provider/" + serviceProviderId + "/ssm");

                                return filePart.content()
                                        .map(DataBuffer::readableByteCount)
                                        .reduce(Integer::sum)
                                        .flatMap(totalBytes -> Mono.fromFuture(s3AsyncClient.putObject(PutObjectRequest.builder()
                                                .bucket(bucket)
                                                .contentLength(Long.valueOf(totalBytes))
                                                .key(path.toString())
                                                .build(), AsyncRequestBody.fromPublisher(filePart.content()
                                                .map(DataBuffer::asByteBuffer)))))
                                        .map(e -> new ImageDTO("https://" + bucket + "." + region + "." + "digitaloceanspaces.com/" + path.toString(), "ssm"));

                            });

                    return Flux.concat(frontIdentityCardFlux, backIdentityCardFlux, ssmFlux)
                            .flatMap(imageDTO->{
                                if(imageDTO.getType().equals("ssm")){
                                    System.out.println(serviceProviderId);
                                    return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> providerRepository.findDetailsById(session, serviceProviderId)
                                            .thenCompose(serviceProvider -> {
                                                serviceProvider.setSsm(imageDTO.getPath());
                                                return providerRepository.save(session, serviceProvider);
                                            })));
                                }
                                if(imageDTO.getType().equals("front_identity_card")){
                                    return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> providerRepository.findDetailsById(session, serviceProviderId)
                                            .thenCompose(serviceProvider -> {

                                                serviceProvider.setFrontIdentityCard(imageDTO.getPath());
                                                return providerRepository.save(session, serviceProvider);

                                            })));
                                }
                                if(imageDTO.getType().equals("back_identity_card")){
                                    return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> providerRepository.findDetailsById(session, serviceProviderId)
                                            .thenCompose(serviceProvider -> {
                                                serviceProvider.setBackIdentityCard(imageDTO.getPath());
                                                return providerRepository.save(session, serviceProvider);
                                            })));
                                }
                                return Flux.empty()
                                        .then();
                            });
                }).flatMap(v-> Flux.just(new ImageDTO()));
    }

    @Override
    public Flux<ImageDTO> uploadCompleteAppointmentEvidences(UUID id, Long serviceId, String serviceProviderId, String customerId, LocalDateTime createdAt, Mono<MultiValueMap<String, Part>> formData) {
        AppointmentId appointmentId = new AppointmentId();
        appointmentId.setServiceId(serviceId);
        appointmentId.setServiceProviderId(serviceProviderId);
        appointmentId.setCustomerId(customerId);
        appointmentId.setCreatedAt(createdAt);
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMapMany(name -> {
                    Flux<ImageDTO> evidenceFlux = formData.map(map -> map.get("evidences"))
                            .flatMapMany(Flux::fromIterable)
                            .flatMap(filePart -> {
                                StringBuffer path = new StringBuffer("appointments/" + id + "/completion-evidences/" + UUID.randomUUID());

                                return filePart.content()
                                        .map(DataBuffer::readableByteCount)
                                        .reduce(Integer::sum)
                                        .flatMap(totalBytes -> Mono.fromFuture(s3AsyncClient.putObject(PutObjectRequest.builder()
                                                .contentType(filePart.headers()
                                                        .getContentType()
                                                        .toString())
                                                .bucket(bucket)
                                                .acl(ObjectCannedACL.PUBLIC_READ)
                                                .contentLength(Long.valueOf(totalBytes))
                                                .key(path.toString())
                                                .build(), AsyncRequestBody.fromPublisher(filePart.content()
                                                .map(DataBuffer::asByteBuffer)))))
                                        .map(e -> new ImageDTO("https://" + bucket + "." + region + "." + "digitaloceanspaces.com/" + path, "complete-evidences"));

                            });

                    return Flux.concat(evidenceFlux)
                            .flatMap(imageDTO-> {
                                if (imageDTO.getType()
                                        .equals("complete-evidences")) {
                                    return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> appointmentRepository.findById(session, appointmentId)
                                            .thenCompose(appointment -> {
                                                AppointmentImage appointmentImage = new AppointmentImage();
                                                appointmentImage.setAppointment(appointment);
                                                appointmentImage.setPath(imageDTO.getPath());
                                                appointment.getAppointmentImages().add(appointmentImage);
                                                appointment.setJobCompletionDatetime(LocalDateTime.now());
                                                appointment.setStatus(AppointmentStatus.WORK_COMPLETED);
                                                return appointmentRepository.save(session, appointment);
                                            }))).then(Mono.fromCallable(appointmentCacheService::removeAll));
                                }
                                return Flux.empty()
                                        .then();
                            });
                }).flatMap(v-> Flux.just(new ImageDTO()));
    }
}
