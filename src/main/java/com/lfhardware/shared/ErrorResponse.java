package com.lfhardware.shared;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String path;

    private LocalDateTime timestamp;

    private String message;

    private String code;

    public ErrorResponse(String message, String path){
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    public ErrorResponse(String message,String path, String code){
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
        this.code = code;
    }

}
