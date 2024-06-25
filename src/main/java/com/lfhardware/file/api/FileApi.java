package com.lfhardware.file.api;

import com.lfhardware.file.dto.ImageDTO;
import com.lfhardware.file.service.FileService;
import com.lfhardware.file.service.ImageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class FileApi {

    private final FileService<Mono<MultiValueMap<String, Part>>,Flux<ImageDTO>> fileService;

    public FileApi(@Qualifier("imageService") FileService<Mono<MultiValueMap<String, Part>>,Flux<ImageDTO>> fileService){
        this.fileService = fileService;
    }

    public Mono<ServerResponse> uploadProductFile(ServerRequest serverRequest){
        return ServerResponse.ok().body(fileService.upload(serverRequest.multipartData()), ImageDTO.class);
    }

    public Mono<ServerResponse> uploadServiceProviderDocument(ServerRequest serverRequest){
        return ServerResponse.ok().body(fileService.uploadServiceProviderDocuments(serverRequest.multipartData()), ImageDTO.class);
    }

    public Mono<ServerResponse> uploadCompleteAppointmentEvidences(ServerRequest serverRequest){
        UUID id = UUID.fromString(serverRequest.pathVariable("id"));
        Long serviceId = Long.valueOf(serverRequest.pathVariable("serviceId"));
        String serviceProviderId = serverRequest.pathVariable("serviceProviderId");
        String customerId = serverRequest.pathVariable("customerId");
        LocalDateTime createdAt = LocalDateTime.parse(serverRequest.pathVariable("createdAt"));
        return ServerResponse.ok().body(fileService.uploadCompleteAppointmentEvidences(id, serviceId, serviceProviderId, customerId, createdAt, serverRequest.multipartData()), ImageDTO.class);
    }
}
