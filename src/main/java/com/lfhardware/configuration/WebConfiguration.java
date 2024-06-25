package com.lfhardware.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stripe.StripeClient;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;
import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.util.MimeType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Configuration
public class WebConfiguration implements WebFluxConfigurer {

    private final ObjectMapper objectMapper;

    public WebConfiguration(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }


//    @Bean
//    public XmlMapper xmlMapper(){
//        return XmlMapper.builder().addModule(new JavaTimeModule()).build();
//    }
//
//    @Override
//    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
//        configurer.defaultCodecs().jaxb2Encoder(new Jaxb2XmlEncoder());
//        configurer.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder());
//    }

    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(
                new Jackson2JsonEncoder(objectMapper)
        );

        configurer.defaultCodecs().jackson2JsonDecoder(
                new Jackson2JsonDecoder(objectMapper)
        );
    }

    //    @Bean
//    HandlerMethodArgumentResolver reactivePageableHandlerMethodArgumentResolver() {
//        return new ReactivePageableHandlerMethodArgumentResolver();
//    }

//    @Bean
//    CorsWebFilter corsWebFilter(){
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(List.of("*"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowedMethods(List.of("*"));
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return new CorsWebFilter(source);
//    }

    @Bean
    public Base32 base32(){
        return new Base32();
    }

    @Bean
    public Base64.Decoder base64Decoder(){
        return Base64.getDecoder();
    }

    @Bean
    public Base64.Encoder base64Encoder(){
        return Base64.getEncoder();
    }
//    @Bean
//    public Module datatypeHibernateModule() {
//        return new Hibernate6Module();
//    }

//    @Bean
//    WebExceptionHandler exceptionHandler(){
//        return (ServerWebExchange exchange,Throwable ex)->{
//
//            System.out.println(ex instanceof NoSuchElementException);
//
//          if(ex instanceof NoSuchElementException){
//              exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
//              return exchange.getResponse().setComplete();
//          }
//          return Mono.error(ex);
//        };
//    }


}
