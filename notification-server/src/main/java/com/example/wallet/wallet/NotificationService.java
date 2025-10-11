package com.example.wallet.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class NotificationService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SimpleMailMessage simpleMailMessage;

    @Autowired
    JavaMailSender javaMailSender;

    @KafkaListener(topics = {CommonConstants.TRANSACTION_COMPLETED_Topic}, groupId = "grp123")
    public void sendNotification(String msg) throws ParseException
    {
        JSONObject data = null;
        try {
            data = (JSONObject) new JSONParser().parse(msg);
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }

        String email = (String)data.get("email");
        String emailMsg = (String) data.get("msg");

        simpleMailMessage.setFrom("lacawa7629@fintehs.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText(emailMsg);
        simpleMailMessage.setSubject("E-Wallet Payment Updates");

        javaMailSender.send(simpleMailMessage);
    }
}
