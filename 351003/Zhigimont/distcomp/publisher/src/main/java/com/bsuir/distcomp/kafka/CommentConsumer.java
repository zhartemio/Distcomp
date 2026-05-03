package com.bsuir.distcomp.kafka;

import com.bsuir.distcomp.dto.CommentListResponseTo;
import com.bsuir.distcomp.dto.CommentResponseTo;
import com.bsuir.distcomp.service.ResponseHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CommentConsumer {

    @Autowired
    private ResponseHolder holder;

    @KafkaListener(topics = "OutTopic", groupId = "publisher-group")
    public void listen(Object response) {

        if (response instanceof CommentResponseTo single) {

            if (single.getCorrelationId() == null) {
                throw new RuntimeException("CorrelationId is null!");
            }

            holder.complete(single.getCorrelationId(), single);
        }

        else if (response instanceof CommentListResponseTo list) {

            if (list.getCorrelationId() == null) {
                throw new RuntimeException("CorrelationId is null!");
            }

            holder.complete(list.getCorrelationId(), list);
        }
    }
}