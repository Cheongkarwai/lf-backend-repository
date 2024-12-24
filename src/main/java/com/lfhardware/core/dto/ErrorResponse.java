package com.lfhardware.core.dto;

import lombok.*;

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
