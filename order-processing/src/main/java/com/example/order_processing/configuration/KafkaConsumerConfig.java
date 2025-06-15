package com.example.order_processing.configuration;

import com.example.order_processing.incoming.PaymentCompleted;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.order_processing.incoming.OrderPlacedEvent;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeHeaders(true);
        jsonDeserializer.setRemoveTypeHeaders(false);

        jsonDeserializer.setTypeFunction(typeIdMapper());

        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "order-group");
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", jsonDeserializer);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
    }

    private BiFunction<byte[], Headers, JavaType> typeIdMapper() {
        return (data, headers) -> {
            String typeId = null;
            if (headers != null && headers.lastHeader("__TypeId__") != null) {
                typeId = new String(headers.lastHeader("__TypeId__").value());
            }

            Class<?> targetClass;
            if ("com.example.cart_management.outgoing.OrderPlacedEvent".equals(typeId)) {
                targetClass = OrderPlacedEvent.class;
            } else if("com.example.payment_simulation.dto.PaymentCompleted".equals(typeId)) {
                targetClass = PaymentCompleted.class;
            }else {
                throw new IllegalArgumentException("Unknown __TypeId__: " + typeId);
            }

            return TypeFactory.defaultInstance().constructType(targetClass);
        };
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
