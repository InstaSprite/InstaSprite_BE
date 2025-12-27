package org.olaz.instasprite_be.global.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class MailConfig {


    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public JavaMailSender javaMailSender(MailProperties properties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        if (properties.getHost() != null) sender.setHost(properties.getHost());
        if (properties.getPort() != null) sender.setPort(properties.getPort());
        if (properties.getUsername() != null) sender.setUsername(properties.getUsername());
        if (properties.getPassword() != null) sender.setPassword(properties.getPassword());
        if (properties.getProtocol() != null) sender.setProtocol(properties.getProtocol());
        if (properties.getDefaultEncoding() != null) sender.setDefaultEncoding(properties.getDefaultEncoding().name());

        if (properties.getProperties() != null && !properties.getProperties().isEmpty()) {
            sender.getJavaMailProperties().putAll(properties.getProperties());
        }

        return sender;
    }
}


