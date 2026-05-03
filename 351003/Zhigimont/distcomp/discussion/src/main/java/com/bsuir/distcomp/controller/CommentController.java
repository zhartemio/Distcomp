package com.bsuir.distcomp.controller;

import com.bsuir.distcomp.dto.CommentResponseTo;
import com.bsuir.distcomp.entity.CommentKey;
import com.bsuir.distcomp.exception.EntityNotFoundException;
import com.bsuir.distcomp.mapper.CommentMapper;
import com.bsuir.distcomp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository repository;
    private final CommentMapper mapper;

    @GetMapping("/{id}")
    public CommentResponseTo getById(@PathVariable Long id,
                                     @RequestParam(required = false) Long topicId) {
        if (topicId != null) {
            CommentKey key = new CommentKey();
            key.setId(id);
            key.setTopicId(topicId);
            return repository.findById(key)
                    .map(mapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        }

        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }
}
