package com.lfhardware.product.api;

import com.lfhardware.configuration.KeycloakProperties;
import com.lfhardware.product.dto.ProductDTO;
import com.lfhardware.product.exception.ProductNotFoundException;
import com.lfhardware.product.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.Token;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private IProductService productService;

    @Autowired
    private KeycloakProperties keycloakProperties;

    private String accessToken;

    @BeforeEach
    public void setUp() {
        AccessTokenResponse token = getAccessToken("cheongkarwai","Cheongkarwai123");
        if(token != null){
            accessToken = token.getToken();
        }
    }

    private AccessTokenResponse getAccessToken(String username, String password) {
        WebClient webClient = WebClient.create(keycloakProperties.getServerUrl() + "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token");
        return webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .bodyValue("grant_type=password&client_id=" + keycloakProperties.getClientId() + "&client_secret=" + keycloakProperties.getClientSecret() + "&username="
                        + username + "&password=" + password)
                .retrieve()
                .bodyToMono(AccessTokenResponse.class)
                .block();  // Retrieve and parse the access token
    }

    @Test
    public void findById_OnMissingProduct_ReturnHttpStatusNotFound() {
        UUID uuid = UUID.randomUUID();
        given(productService.findById(any(UUID.class))).willThrow(new ProductNotFoundException("Product not found"));

        webTestClient.get().uri("/api/v1/products/{id}", uuid)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void findById_OnExistingProduct_ReturnHttpStatusOk() {
        UUID uuid = UUID.randomUUID();
        ProductDTO mockedProductDTO = new ProductDTO();
        mockedProductDTO.setId(uuid);
        given(productService.findById(any(UUID.class))).willReturn(Mono.just(mockedProductDTO));

        webTestClient.get().uri("/api/v1/products/{id}", uuid)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(uuid.toString());
    }

    @Test
    public void findById_OnExistingProductWithoutAccessToken_ReturnHttpStatusUnauthorized() {
        UUID uuid = UUID.randomUUID();
        ProductDTO mockedProductDTO = new ProductDTO();
        mockedProductDTO.setId(uuid);
        given(productService.findById(any(UUID.class))).willReturn(Mono.just(mockedProductDTO));

        webTestClient.get().uri("/api/v1/products/{id}", uuid)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void findAll_WithoutAccessToken_ReturnHttpStatusUnauthorized(){
        UUID uuid = UUID.randomUUID();
        ProductDTO mockedProductDTO = new ProductDTO();
        mockedProductDTO.setId(uuid);
        given(productService.findAll(any(UUID.class))).willReturn(Mono.just(mockedProductDTO));

        webTestClient.get().uri("/api/v1/products", uuid)
                .exchange()
                .expectStatus().isUnauthorized();
    }

}
