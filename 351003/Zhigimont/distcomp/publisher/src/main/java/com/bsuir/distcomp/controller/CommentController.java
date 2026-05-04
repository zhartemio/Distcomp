package com.bsuir.distcomp.controller;

import com.bsuir.distcomp.dto.CommentListResponseTo;
import com.bsuir.distcomp.dto.CommentRequestTo;
import com.bsuir.distcomp.dto.CommentResponseTo;
import com.bsuir.distcomp.kafka.CommentProducer;
import com.bsuir.distcomp.service.ResponseHolder;
import com.bsuir.types.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentProducer producer;
    private final ResponseHolder holder;

    @GetMapping
    public ResponseEntity<?> getAll() throws Exception {
        log.info("🟢 [STEP 1] GET /comments - Start");

        String correlationId = UUID.randomUUID().toString();
        log.info("🟢 [STEP 1] Generated correlationId: {}", correlationId);

        CommentRequestTo request = new CommentRequestTo();
        request.setOperation(OperationType.GET_ALL);
        request.setCorrelationId(correlationId);

        log.info("🟢 [STEP 1] Created request: operation={}, correlationId={}",
                request.getOperation(), request.getCorrelationId());

        CompletableFuture<CommentListResponseTo> future = holder.createList(correlationId);
        log.info("🟢 [STEP 1] Created future in ResponseHolder");

        producer.send(request);
        log.info("🟢 [STEP 1] Request sent to Kafka via producer");

        try {
            log.info("🟢 [STEP 1] Waiting for response (10 sec timeout)...");
            CommentListResponseTo response = future.get(10, TimeUnit.SECONDS);
            log.info("🟢 [STEP 1] Response received: {} comments",
                    response.getComments() != null ? response.getComments().size() : 0);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("🔴 [STEP 1] Timeout or error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
        log.info("🟢 GET /comments/{}", id);

        String correlationId = UUID.randomUUID().toString();

        CommentRequestTo request = new CommentRequestTo();
        request.setId(id);

        request.setOperation(OperationType.GET_BY_ID);
        request.setCorrelationId(correlationId);

        CompletableFuture<CommentResponseTo> future = holder.createSingle(correlationId);
        producer.send(request);

        try {
            CommentResponseTo response = future.get(10, TimeUnit.SECONDS);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommentRequestTo dto) throws Exception {
        String correlationId = UUID.randomUUID().toString();
        dto.setOperation(OperationType.CREATE);
        dto.setCorrelationId(correlationId);

        log.info("🟢 POST /comments - correlationId={}, topicId={}", correlationId, dto.getTopicId());


        CompletableFuture<CommentResponseTo> future = holder.createSingle(correlationId);


        producer.send(dto);


        try {
            CommentResponseTo response = future.get(10, TimeUnit.SECONDS);
            log.info("✅ Comment created: id={}, status={}", response.getId(), response.getStatus());

            // Возвращаем 201 с телом ответа
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (TimeoutException e) {
            log.error("🔴 Timeout waiting for moderation result");
            // Возвращаем 202 - принято, но еще не обработано
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
    }
    @PutMapping
    public ResponseEntity<?> update(@RequestBody CommentRequestTo dto) throws Exception {
        log.info("🟢 PUT /comments - id={}, topicId={}", dto.getId(), dto.getTopicId());

        String correlationId = UUID.randomUUID().toString();
        dto.setOperation(OperationType.UPDATE);
        dto.setCorrelationId(correlationId);

        CompletableFuture<CommentResponseTo> future = holder.createSingle(correlationId);

        producer.send(dto);

        try {
            CommentResponseTo response = future.get(10, TimeUnit.SECONDS);
            log.info("✅ PUT completed: id={}, status={}", response.getId(), response.getStatus());

            return ResponseEntity.ok(response);

        } catch (TimeoutException e) {
            log.error("🔴 Timeout on PUT");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        String correlationId = UUID.randomUUID().toString();

        CommentRequestTo dto = new CommentRequestTo();
        dto.setId(id);
        // Пробуем найти topicId, если знаем
        dto.setOperation(OperationType.DELETE);
        dto.setCorrelationId(correlationId);

        log.info("🟢 DELETE /comments/{}", id);

        CompletableFuture<CommentResponseTo> future = holder.createSingle(correlationId);
        producer.send(dto);

        try {
            future.get(10, TimeUnit.SECONDS);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting: {}", e.getMessage());
            return ResponseEntity.noContent().build(); // Всё равно возвращаем 204
        }
    }
}