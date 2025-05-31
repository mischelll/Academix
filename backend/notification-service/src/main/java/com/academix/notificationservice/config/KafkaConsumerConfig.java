package com.academix.notificationservice.config;

import com.academix.notificationservice.consumers.events.HomeworkReminderEvent;
import com.academix.notificationservice.consumers.events.HomeworkReviewedEvent;
import com.academix.notificationservice.consumers.events.HomeworkSubmissionEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, HomeworkSubmissionEvent> submissionConsumerFactory() {
        JsonDeserializer<HomeworkSubmissionEvent> deserializer = new JsonDeserializer<>(HomeworkSubmissionEvent.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(false);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092",
                        ConsumerConfig.GROUP_ID_CONFIG, "notification-service"
                ),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, HomeworkSubmissionEvent> submissionKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, HomeworkSubmissionEvent>();
        factory.setConsumerFactory(submissionConsumerFactory());
        return factory;
    }
}
