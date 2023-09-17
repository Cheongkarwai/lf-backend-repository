package com.lf_prototype.lf_prototype.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.PostgresqlConnectionFactoryProvider;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

//@Configuration
//@EnableR2dbcRepositories
//public class R2DBCConfiguration extends AbstractR2dbcConfiguration {
//
//    private R2dbcConnectionProperties connection;
//
//    public R2DBCConfiguration(R2dbcConnectionProperties connection){
//        this.connection = connection;
//    }
//    @Override
//    public ConnectionFactory connectionFactory() {
//        return new PostgresqlConnectionFactory(
//                PostgresqlConnectionConfiguration.builder()
//                        .host("localhost")
//                        .port(5432)
//                        .database("clinic")
//                        .username("postgres")
//                        .password("root").build()
//        );
//    }
//}
