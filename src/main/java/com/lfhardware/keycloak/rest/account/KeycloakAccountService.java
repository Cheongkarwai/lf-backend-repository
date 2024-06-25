package com.lfhardware.keycloak.rest.account;

import com.lfhardware.configuration.KeycloakProperties;
import com.lfhardware.keycloak.rest.account.dto.OTPQrCodeDTO;
import com.lfhardware.keycloak.rest.account.dto.OtpDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@EnableConfigurationProperties({KeycloakProperties.class})
public class KeycloakAccountService{

    private final WebClient webClient;

    private final KeycloakProperties keycloakProperties;

    public KeycloakAccountService(KeycloakProperties keycloakProperties){
        this.keycloakProperties = keycloakProperties;
        this.webClient = WebClient.builder()
                .baseUrl(keycloakProperties.getServerUrl() + "/realms/{realms}/user-account")
                .defaultUriVariables(Map.of("realms", keycloakProperties.getRealm()))
                .build();
    }

    public Mono<OTPQrCodeDTO> generateOtpQrCode(String accessToken){
        return webClient.get().uri("/otp/qr-code")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().is2xxSuccessful()){
                        return clientResponse.bodyToMono(OTPQrCodeDTO.class);
                    }
                    return clientResponse.createError();
                });
    }

    public Mono<Void> setupOTPVerification(String tokenValue, OtpDTO otpDTO) {
        return webClient.post().uri("/otp")
                .bodyValue(otpDTO)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(tokenValue))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().is2xxSuccessful()){
                        return clientResponse.bodyToMono(Void.class);
                    }
                    return clientResponse.createError();
                });
    }
}
