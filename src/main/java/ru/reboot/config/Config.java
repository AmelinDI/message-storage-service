package ru.reboot.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.reboot.dao.entity.MessageEntity;

import java.util.Properties;

@Configuration
public class Config {

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean
    public Properties hibernateProperties() {

        Properties properties = new Properties();

        properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty(Environment.URL, url);
        properties.setProperty(Environment.USER, username);
        properties.setProperty(Environment.PASS, password);

        properties.setProperty(Environment.HBM2DDL_AUTO, "validate");
        properties.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.setProperty(Environment.SHOW_SQL, "true");

        return properties;
    }

    @Bean
    public SessionFactory sessionFactory() {

        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration.setProperties(hibernateProperties());
        configuration.addAnnotatedClass(MessageEntity.class);

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
}
