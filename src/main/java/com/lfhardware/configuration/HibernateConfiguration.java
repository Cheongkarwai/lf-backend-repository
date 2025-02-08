package com.lfhardware.configuration;

import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.reactive.provider.ReactivePersistenceProvider;
import org.hibernate.reactive.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class HibernateConfiguration {

//    private Stage.SessionFactory sessionFactory;
//
//    private EntityManager entityManager;

    private final Environment env;

    public HibernateConfiguration(Environment env){
        this.env = env;
    }

    @Bean
    public Mutiny.SessionFactory mutinySessionFactory(){
        EntityManagerFactory emf = new ReactivePersistenceProvider().createEntityManagerFactory("postgres", hibernateProperties());
        return emf.unwrap(Mutiny.SessionFactory.class);
    }

    @Bean
    public Stage.SessionFactory stageSessionFactory(){
        EntityManagerFactory emf = new ReactivePersistenceProvider().createEntityManagerFactory("postgres", hibernateProperties());
        return emf.unwrap(Stage.SessionFactory.class);
    }

    private Map<String, String> hibernateProperties(){
        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", env.getProperty("spring.datasource.url"));
        properties.put("jakarta.persistence.jdbc.user", env.getProperty("spring.datasource.username"));
        properties.put("jakarta.persistence.jdbc.password", env.getProperty("spring.datasource.password"));
        properties.put("hibernate.connection.pool_size", "10");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.highlight_sql", "true");
        return properties;
    }

//    @Bean
//    public EntityManager entityManager(){
//        try(EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgres", hibernateProperties())){
//            this.entityManager = entityManagerFactory.createEntityManager();
//            return entityManager;
//        }
//    }


//    @PreDestroy
//    public void destroy(){
//        this.sessionFactory.close();;
//        this.entityManager.close();
//    }

    @Bean
    public Vertx vertx(){
        return Vertx.vertx();
    }
}
