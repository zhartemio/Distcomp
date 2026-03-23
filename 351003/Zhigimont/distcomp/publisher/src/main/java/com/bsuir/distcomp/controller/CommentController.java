package com.bsuir.distcomp.controller;

import com.bsuir.distcomp.dto.CommentRequestTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/v1.0/comments")
public class CommentController {

    private final WebClient webClient;

    public CommentController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:24130/api/v1.0").build();
    }

    // GET /comments
    @GetMapping
    public ResponseEntity<?> getAllComments(@RequestParam(required = false) Long topicId) {
        if (topicId != null) {
            return webClient.get()
                    .uri("/comments/{topicId}", topicId)
                    .retrieve()
                    .toEntity(Object.class)
                    .block();
        } else {
            return webClient.get()
                    .uri("/comments")
                    .retrieve()
                    .toEntity(Object.class)
                    .block();
        }
    }

    // GET /comments/{topicId}  (для совместимости с тестами, которые идут без /comments)
    @GetMapping("/{topicId}")
    public ResponseEntity<?> getCommentsByTopic(@PathVariable Long topicId) {
        return webClient.get()
                .uri("/comments/{topicId}", topicId)
                .retrieve()
                .toEntity(Object.class)
                .block();
    }

    // GET /comments/{topicId}/{id} (если нужно получить конкретный комментарий)
    @GetMapping("/{topicId}/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long topicId, @PathVariable Long id) {
        return webClient.get()
                .uri("/comments/{topicId}/{id}", topicId, id)
                .retrieve()
                .toEntity(Object.class)
                .block();
    }

    // POST /comments
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createComment(@RequestBody CommentRequestTo request) {
        return webClient.post()
                .uri("/comments")
                .bodyValue(request)
                .retrieve()
                .toEntity(Object.class)
                .block();
    }

    // PUT /comments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestTo request) {
        request.setId(id);
        return webClient.put()
                .uri("/comments/{id}", id)
                .bodyValue(request)
                .retrieve()
                .toEntity(Object.class)
                .block();
    }

    // DELETE /comments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        return webClient.delete()
                .uri("/comments/{id}", id)
                .retrieve()
                .toEntity(Object.class)
                .block();
    }
}