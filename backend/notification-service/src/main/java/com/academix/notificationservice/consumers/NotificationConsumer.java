package com.academix.notificationservice.consumers;

import com.academix.notificationservice.client.UserServiceClient;
import com.academix.notificationservice.client.dto.UserDTO;
import com.academix.notificationservice.consumers.events.HomeworkReminderEvent;
import com.academix.notificationservice.consumers.events.HomeworkReviewedEvent;
import com.academix.notificationservice.consumers.events.HomeworkSubmissionEvent;
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
    private final UserServiceClient userServiceClient;
    private final TwilioRestClient twilioRestClient;
    private final String twilioPhoneNumber;

    public NotificationConsumer(JavaMailSender mailSender, UserServiceClient userServiceClient, 
                               TwilioRestClient twilioRestClient, String twilioPhoneNumber) {
        this.mailSender = mailSender;
        this.userServiceClient = userServiceClient;
        this.twilioRestClient = twilioRestClient;
        this.twilioPhoneNumber = twilioPhoneNumber;
    }

    @KafkaListener(topics = "homework.submission", groupId = "notification-service")
    public void handleHomeworkSubmitted(String message) throws JSONException {
        JSONObject json = new JSONObject(message);
        Long studentId = json.getLong("studentId");
        Long homeworkId = json.getLong("homeworkId");

        String teacherEmail = userServiceClient.getTeacherEmailByLessonId(
                json.getLong("lessonId")
        );

        String subject = "Neue Hausaufgabe eingereicht";
        String text = String.format(
                "Student (ID: %d) hat die Hausaufgabe mit ID %d hochgeladen. Bitte prüfen Sie das Ergebnis.",
                studentId, homeworkId
        );

        sendEmail(teacherEmail, subject, text);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @KafkaListener(topics = "homework.reviewed", groupId = "notification-service")
    public void handleHomeworkReviewed(String message) throws JSONException {
        JSONObject json = new JSONObject(message);
        Long studentId = json.getLong("studentId");
        Long homeworkId = json.getLong("homeworkId");
        Long grade = json.optLong("grade", -1);

        UserDTO student = userServiceClient.getUserByIdAsDTO(studentId);
        
        if (student == null) {
            logger.error("Could not find user with ID: " + studentId + " for homework review notification");
            return;
        }

        String subject = "Ihre Hausaufgabe wurde bewertet";
        String body = String.format(
                "Hallo %s,\n\n" +
                        "Ihre Hausaufgabe (ID: %d) wurde bewertet.\n" +
                        "Note: %s\n\n" +
                        "Viele Grüße,\nIhr Academix-Team",
                student.firstName(), homeworkId, 
                grade >= 0 ? String.valueOf(grade) : "Noch nicht bewertet"
        );

        sendEmail(student.email(), subject, body);

        String smsText = String.format(
                "Hausaufgabe %d wurde bewertet. Note: %s", 
                homeworkId, 
                grade >= 0 ? String.valueOf(grade) : "Noch nicht bewertet"
        );
        sendSms(student.phoneNumber(), smsText);
    }

    @KafkaListener(topics = "homework.reminder", groupId = "notification-service")
    public void handleHomeworkReminder(String message) throws JSONException {
        JSONObject json = new JSONObject(message);
        Long studentId = json.getLong("studentId");
        Long homeworkId = json.getLong("homeworkId");
        String timeLeft = json.getString("timeLeft");

        UserDTO student = userServiceClient.getUserByIdAsDTO(studentId);
        
        if (student == null) {
            logger.error("Could not find user with ID: " + studentId + " for homework reminder notification");
            return;
        }

        String subject = "Erinnerung: Hausaufgabe fällig in " + timeLeft;
        String body = String.format(
                "Hallo %s,\n\n" +
                        "Sie haben noch %s Zeit, um Ihre Hausaufgabe (ID: %d) abzugeben.\n" +
                        "Bitte reichen Sie sie rechtzeitig ein.\n\n" +
                        "Viele Grüße,\nIhr Academix-Team",
                student.firstName(), timeLeft, homeworkId
        );

        sendEmail(student.email(), subject, body);

        String smsText = String.format(
                "Erinnerung: Hausaufgabe %d fällig in %s", homeworkId, timeLeft
        );
        sendSms(student.phoneNumber(), smsText);
    }

    private void sendSms(String to, String messageText) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(twilioPhoneNumber),
                messageText
        ).create(twilioRestClient);
    }

}
