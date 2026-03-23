package com.bsuir.distcomp.controller;

import com.bsuir.distcomp.dto.CommentRequestTo;
import com.bsuir.distcomp.dto.CommentResponseTo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.bsuir.distcomp.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)   // возвращаем 201
    public CommentResponseTo create(@RequestBody CommentRequestTo requestTo) {
        return service.create(requestTo);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseTo> getAll() {
        return service.getAll();
    }

    @GetMapping("/{topicId}")
    public List<CommentResponseTo> getByTopic(@PathVariable Long topicId) {
        return service.getByTopic(topicId);
    }

    @GetMapping("/{topicId}/{id}")
    public CommentResponseTo getById(@PathVariable Long topicId, @PathVariable Long id) {
        return service.getById(id);  // поиск по id без учёта topicId (можно и по составному ключу)
    }

    @PutMapping("/{id}")
    public CommentResponseTo update(@PathVariable Long id, @RequestBody CommentRequestTo requestTo) {
        requestTo.setId(id);          // убеждаемся, что ID взят из пути
        return service.update(requestTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}