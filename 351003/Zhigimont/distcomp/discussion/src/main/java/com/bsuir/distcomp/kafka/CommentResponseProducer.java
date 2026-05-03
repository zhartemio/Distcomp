package com.bsuir.distcomp.kafka;

import com.bsuir.distcomp.dto.CommentListResponseTo;
import com.bsuir.distcomp.dto.CommentResponseTo;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommentResponseProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CommentResponseProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CommentResponseTo response) {
        kafkaTemplate.send("OutTopic", response.getCorrelationId(), response);
    }

    public void sendList(CommentListResponseTo response) {
        kafkaTemplate.send("OutTopic", response.getCorrelationId(), response);
    }
}