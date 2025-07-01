package com.academix.homeworkservice.service.kafka;

import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class HomeworkEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public HomeworkEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishHomeworkSubmitted(JSONObject payload) {
        kafkaTemplate.send("homework.submission", UUID.randomUUID().toString(), payload.toString());
    }

    public void publishHomeworkReviewed(JSONObject payload) {
        kafkaTemplate.send("homework.reviewed", UUID.randomUUID().toString(), payload.toString());
    }
}
