package com.lf_prototype.lf_prototype.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "r2dbc.connection")
public class R2dbcConnectionProperties {

    private String username;

    private String password;

    private String host;

    private int port;
}
