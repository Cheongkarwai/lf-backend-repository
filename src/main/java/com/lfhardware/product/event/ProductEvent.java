package com.lfhardware.product.event;

import com.lfhardware.product.dto.ProductDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.oauth2.jwt.Jwt;

@Data
@Builder
public class ProductEvent {

    private ProductDTO productDTO;

    private Jwt jwt;
}
