package com.academix.notificationservice.consumers;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    private final JavaMailSender mailSender;
    private final TwilioRestClient twilioRestClient;
    private final String twilioPhoneNumber;

    public NotificationConsumer(JavaMailSender mailSender,
                               TwilioRestClient twilioRestClient, String twilioPhoneNumber) {
        this.mailSender = mailSender;
        this.twilioRestClient = twilioRestClient;
        this.twilioPhoneNumber = twilioPhoneNumber;
    }

    @KafkaListener(topics = "homework.submission", groupId = "notification-service")
    public void handleHomeworkSubmitted(String message) throws JSONException {
        JSONObject json = new JSONObject(message);
        Long studentId = json.getLong("studentId");
        Long homeworkId = json.getLong("homeworkId");
        String teacherEmail = json.optString("teacherEmail", null);
        String teacherName = json.optString("teacherName", null);
        String studentName = json.optString("studentName", null);

        if (teacherEmail == null || teacherEmail.isBlank()) {
            logger.warn("No teacher email found for homework submission. HomeworkId: {}, StudentId: {}", homeworkId, studentId);
            return;
        }

        String subject = "Neue Hausaufgabe eingereicht";
        String text = String.format(
                "Student %s (ID: %d) hat die Hausaufgabe mit ID %d hochgeladen. Bitte prüfen Sie das Ergebnis.",
                studentName != null ? studentName : "Unknown", studentId, homeworkId
        );
        sendEmail(teacherEmail, subject, text);
    }

    private void sendEmail(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            logger.error("Attempted to send email to null or empty address. Subject: {}, Body: {}", subject, body);
            return;
        }
        
        // Check if mail credentials are properly configured
        if (mailSender instanceof org.springframework.mail.javamail.JavaMailSenderImpl) {
            org.springframework.mail.javamail.JavaMailSenderImpl javaMailSender = (org.springframework.mail.javamail.JavaMailSenderImpl) mailSender;
            String username = javaMailSender.getUsername();
            String password = javaMailSender.getPassword();
            String host = javaMailSender.getHost();
            int port = javaMailSender.getPort();
            
            logger.info("Mail configuration - Host: {}, Port: {}, Username: {}", host, port, username);
            
            if (username == null || username.equals("your-email@gmail.com")) {
                logger.warn("Mail credentials not configured. Skipping email to: {}, Subject: {}", to, subject);
                return;
            }
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            logger.info("Email sent successfully to: {}, Subject: {}", to, subject);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}, Subject: {}. Error: {}", to, subject, e.getMessage());
            // Don't re-throw the exception to prevent Kafka consumer from crashing
        }
    }

    @KafkaListener(topics = "homework.reviewed", groupId = "notification-service")
    public void handleHomeworkReviewed(String message) throws JSONException {
        JSONObject json = new JSONObject(message);
        Long studentId = json.getLong("studentId");
        Long homeworkId = json.getLong("homeworkId");
        Long grade = json.optLong("grade", -1);
        String studentEmail = json.optString("studentEmail", null);
        String studentName = json.optString("studentName", null);
        String studentPhone = json.optString("studentPhone", null);

        if (studentEmail == null || studentEmail.isBlank()) {
            logger.error("No student email found for homework review notification. HomeworkId: {}, StudentId: {}", homeworkId, studentId);
            return;
        }

        String subject = "Ihre Hausaufgabe wurde bewertet";
        String body = String.format(
                "Hallo %s,\n\n" +
                        "Ihre Hausaufgabe (ID: %d) wurde bewertet.\n" +
                        "Note: %s\n\n" +
                        "Viele Grüße,\nIhr Academix-Team",
                studentName != null ? studentName : "Student", homeworkId, 
                grade >= 0 ? String.valueOf(grade) : "Noch nicht bewertet"
        );

        sendEmail(studentEmail, subject, body);

        if (studentPhone != null && !studentPhone.isBlank()) {
            String smsText = String.format(
                    "Hausaufgabe %d wurde bewertet. Note: %s", 
                    homeworkId, 
                    grade >= 0 ? String.valueOf(grade) : "Noch nicht bewertet"
            );
            sendSms(studentPhone, smsText);
        }
    }

    @KafkaListener(topics = "homework.reminder", groupId = "notification-service")
    public void handleHomeworkReminder(String message) throws JSONException {
        JSONObject json = new JSONObject(message);
        Long studentId = json.getLong("studentId");
        Long homeworkId = json.getLong("homeworkId");
        String timeLeft = json.getString("timeLeft");
        String studentEmail = json.optString("studentEmail", null);
        String studentName = json.optString("studentName", null);
        String studentPhone = json.optString("studentPhone", null);

        if (studentEmail == null || studentEmail.isBlank()) {
            logger.error("No student email found for homework reminder notification. HomeworkId: {}, StudentId: {}", homeworkId, studentId);
            return;
        }

        String subject = "Erinnerung: Hausaufgabe fällig in " + timeLeft;
        String body = String.format(
                "Hallo %s,\n\n" +
                        "Sie haben noch %s Zeit, um Ihre Hausaufgabe (ID: %d) abzugeben.\n" +
                        "Bitte reichen Sie sie rechtzeitig ein.\n\n" +
                        "Viele Grüße,\nIhr Academix-Team",
                studentName != null ? studentName : "Student", timeLeft, homeworkId
        );

        sendEmail(studentEmail, subject, body);

        if (studentPhone != null && !studentPhone.isBlank()) {
            String smsText = String.format(
                    "Erinnerung: Hausaufgabe %d fällig in %s", homeworkId, timeLeft
            );
            sendSms(studentPhone, smsText);
        }
    }

    private void sendSms(String to, String messageText) {
        try {
            Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(twilioPhoneNumber),
                    messageText
            ).create(twilioRestClient);
            logger.info("SMS sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send SMS to: {}. Error: {}", to, e.getMessage());
            // Don't re-throw the exception to prevent Kafka consumer from crashing
        }
    }
}
