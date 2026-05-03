package com.bsuir.distcomp.kafka;

import com.bsuir.distcomp.dto.CommentRequestTo;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommentProducer {

    private final KafkaTemplate<String, CommentRequestTo> kafkaTemplate;

    public CommentProducer(KafkaTemplate<String, CommentRequestTo> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CommentRequestTo dto) {
        String key = dto.getTopicId() == null ? "1" : dto.getTopicId().toString();

        kafkaTemplate.send("InTopic", key, dto);
    }
}