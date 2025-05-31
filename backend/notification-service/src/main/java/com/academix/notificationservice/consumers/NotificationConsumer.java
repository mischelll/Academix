package com.academix.notificationservice.consumers;

import com.academix.notificationservice.consumers.events.HomeworkReminderEvent;
import com.academix.notificationservice.consumers.events.HomeworkReviewedEvent;
import com.academix.notificationservice.consumers.events.HomeworkSubmissionEvent;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @KafkaListener(topics = "homework.submission", groupId = "notification-service")
    public void handleHomeworkSubmitted(String message) throws JSONException {
        JSONObject json = new JSONObject(message);
        String studentId = json.getString("studentId");
        String homeworkId = json.getString("homeworkId");
        System.out.println("📬 Notify teacher: student " + studentId + " submitted homework " + homeworkId);
    }

    @KafkaListener(topics = "homework.reviewed", groupId = "notification-service")
    public void handleHomeworkReviewed(String message) throws JSONException {
        JSONObject json = new JSONObject(message);
        System.out.println("📬 Notify student " + json.getString("studentId") + ": graded " + json.getString("homeworkId"));
    }

    @KafkaListener(topics = "homework.reminder", groupId = "notification-service")
    public void handleHomeworkReminder(String message) throws JSONException {
        JSONObject json = new JSONObject(message);
        System.out.println("🔔 Reminder for student " + json.getString("studentId") + ": " + json.getString("timeLeft") + " left.");
    }
}
