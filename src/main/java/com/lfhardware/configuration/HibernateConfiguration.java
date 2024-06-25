package com.lfhardware.configuration;

import io.vertx.pgclient.PgConnectOptions;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import org.hibernate.reactive.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class HibernateConfiguration {

    private Stage.SessionFactory sessionFactory;

    private EntityManager entityManager;

    @Bean
    public Stage.SessionFactory sessionFactory(){
        this.sessionFactory = Persistence.createEntityManagerFactory("postgres").unwrap(Stage.SessionFactory.class);
        return sessionFactory;
    }

    @Bean
    public EntityManager entityManager(){
        this.entityManager = Persistence.createEntityManagerFactory("postgres").createEntityManager();
        return entityManager;
    }

    @PreDestroy
    public void destroy(){
        this.sessionFactory.close();;
        this.entityManager.close();
    }

}
