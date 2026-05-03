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

        return ResponseEntity.ok(future.get(2, TimeUnit.SECONDS));
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

        return ResponseEntity.ok(future.get(2, TimeUnit.SECONDS));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommentRequestTo dto) throws Exception {
        String correlationId = UUID.randomUUID().toString();
        dto.setOperation(OperationType.CREATE);
        dto.setCorrelationId(correlationId);
        CompletableFuture<CommentResponseTo> future = holder.createSingle(correlationId);
        producer.send(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(future.get(2, TimeUnit.SECONDS));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody CommentRequestTo dto) throws Exception {
        String correlationId = UUID.randomUUID().toString();
        dto.setOperation(OperationType.UPDATE);
        dto.setCorrelationId(correlationId);
        CompletableFuture<CommentResponseTo> future = holder.createSingle(correlationId);
        producer.send(dto);
        return ResponseEntity.ok(future.get(2, TimeUnit.SECONDS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestParam Long topicId) throws Exception {
        CommentRequestTo dto = new CommentRequestTo();
        dto.setId(id);
        dto.setTopicId(topicId);
        dto.setOperation(OperationType.DELETE);
        dto.setCorrelationId(UUID.randomUUID().toString());

        CompletableFuture<CommentResponseTo> future = holder.createSingle(dto.getCorrelationId());
        producer.send(dto);
        future.get(2, TimeUnit.SECONDS);
        return ResponseEntity.noContent().build();
    }
}
