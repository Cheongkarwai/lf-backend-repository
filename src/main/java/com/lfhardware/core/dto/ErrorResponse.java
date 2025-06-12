package com.lfhardware.core.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorResponse {

    private String path;

    private OffsetDateTime timestamp;

    private String message;

    private String code;


}
