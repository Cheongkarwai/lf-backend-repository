package com.lfhardware.logging.aspect;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.lfhardware.auth.dto.UserDTO;
import com.lfhardware.logging.domain.Audit;
import com.lfhardware.logging.repository.IAuditRepository;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    public LoggingAspect(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    @Pointcut("within(com.lfhardware..*..api..* && com.lfhardware..*..service..*)")
    public void logAllMethods(){}

    @Before(value = "logAllMethods()")
    public void logBefore(JoinPoint joinPoint){
        log.info("Before :{}", joinPoint.getSignature());
    }

    @After(value = "logAllMethods()")
    public void logAfter(JoinPoint joinPoint){
        log.info("After : {}", joinPoint.getSignature());
    }

}
