package com.bsuir.distcomp.kafka;

import com.bsuir.distcomp.dto.CommentRequestTo;
import com.bsuir.distcomp.service.CommentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CommentResponseConsumer {

    private final CommentService service;

    public CommentResponseConsumer(CommentService service) {
        this.service = service;
    }

    @KafkaListener(topics = "InTopic", groupId = "discussion-group")
    public void listen(CommentRequestTo dto) {
        service.process(dto);
    }
}