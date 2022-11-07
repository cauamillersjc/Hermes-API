package com.hermes.hermesapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hermes.hermesapi.models.WhatsAppMessageModel;

import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageSenderService {

    @Async
    public void sendWhatsAppMessage(WhatsAppMessageModel whatsAppMessage) {

        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String payload = ow.writeValueAsString(whatsAppMessage);

            StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost("http://20.206.120.95:8090/send-message");
            request.setHeader("Authorization", "Basic Y3Vwb216YXA6aXRhMTIzNA==");
            request.setEntity(entity);

            httpClient.execute(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
