package com.example.wallet.wallet;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Configuration
public class txnConfig {

    @Bean
    PasswordEncoder getPE(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }

    Properties getCProperties()
    {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        return properties;

    }

    Properties getPProperties()
    {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        return properties;
    }

    @Bean
    ConsumerFactory getConsumerFactory()
    {
        return new DefaultKafkaConsumerFactory(getCProperties());
    }

    ProducerFactory getProducerFactory()
    {
        return new DefaultKafkaProducerFactory(getPProperties());
    }

    @Bean
    KafkaTemplate<String,String> getKafkaTemplate()
    {
        return new KafkaTemplate<>(getProducerFactory());
    }

    @Bean
    JavaMailSender getMailSender()
    {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("lacawa7629@fintehs.com");
        javaMailSender.setPassword("Wlac@123");

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.debug", true);
        return javaMailSender;
    }


}
