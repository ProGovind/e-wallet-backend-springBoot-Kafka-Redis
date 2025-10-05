package com.example.wallet.wallet;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
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

    Properties getPProperties()
    {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        return properties;
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


}
