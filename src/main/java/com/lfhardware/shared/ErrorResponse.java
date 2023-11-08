package com.lfhardware.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String path;

    @JsonProperty("date_time")
    private LocalDateTime dateTime;

    private String message;

    @JsonProperty("request_id")
    private String requestId;

}
