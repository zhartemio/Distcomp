package com.bsuir.distcomp.controller;

import com.bsuir.distcomp.dto.CommentListResponseTo;
import com.bsuir.distcomp.dto.CommentRequestTo;
import com.bsuir.distcomp.dto.CommentResponseTo;
import com.bsuir.distcomp.kafka.CommentProducer;
import com.bsuir.distcomp.service.ResponseHolder;
import com.bsuir.types.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1.0/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentProducer producer;
    private final ResponseHolder holder;

    @GetMapping
    public ResponseEntity<?> getAll() throws Exception {
        String correlationId = UUID.randomUUID().toString();

        CommentRequestTo request = new CommentRequestTo();
        request.setOperation(OperationType.GET_ALL);
        request.setCorrelationId(correlationId);

        CompletableFuture<CommentListResponseTo> future = holder.createList(correlationId);
        producer.send(request);

        CommentListResponseTo response = future.get(3, TimeUnit.SECONDS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id,
                                     @RequestParam Long topicId) throws Exception {

        String correlationId = UUID.randomUUID().toString();

        CommentRequestTo request = new CommentRequestTo();
        request.setId(id);
        request.setTopicId(topicId);
        request.setOperation(OperationType.GET_BY_ID);
        request.setCorrelationId(correlationId);

        CompletableFuture<CommentResponseTo> future = holder.createSingle(correlationId);
        producer.send(request);

        CommentResponseTo response = future.get(3, TimeUnit.SECONDS);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommentRequestTo dto) {
        dto.setOperation(OperationType.CREATE);
        producer.send(dto);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam Long topicId,
                                    @RequestBody CommentRequestTo dto) {
        dto.setId(id);
        dto.setTopicId(topicId);
        dto.setOperation(OperationType.UPDATE);
        producer.send(dto);
        return ResponseEntity.accepted().build();
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody CommentRequestTo dto) {
        dto.setOperation(OperationType.UPDATE);
        producer.send(dto);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestParam Long topicId) {
        CommentRequestTo dto = new CommentRequestTo();
        dto.setId(id);
        dto.setTopicId(topicId);
        dto.setOperation(OperationType.DELETE);
        producer.send(dto);
        return ResponseEntity.accepted().build();
    }
}
