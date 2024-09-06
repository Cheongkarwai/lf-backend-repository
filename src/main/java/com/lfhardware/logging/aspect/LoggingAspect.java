package com.lfhardware.logging.aspect;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

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
