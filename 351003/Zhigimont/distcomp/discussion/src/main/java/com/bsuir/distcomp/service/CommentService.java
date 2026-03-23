package com.bsuir.distcomp.service;

import com.bsuir.distcomp.dto.CommentRequestTo;
import com.bsuir.distcomp.dto.CommentResponseTo;
import com.bsuir.distcomp.entity.Comment;
import com.bsuir.distcomp.entity.CommentKey;
import com.bsuir.distcomp.exception.EntityNotFoundException;
import com.bsuir.distcomp.mapper.CommentMapper;
import com.bsuir.distcomp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;

    // Простой генератор ID для демонстрации (в реальном приложении используйте последовательности или UUID)
    private final AtomicLong idGenerator = new AtomicLong(System.currentTimeMillis());

    public CommentResponseTo create(CommentRequestTo requestTo) {
        Comment comment = mapper.toEntity(requestTo);
        CommentKey key = new CommentKey();
        key.setTopicId(requestTo.getTopicId());
        key.setId(idGenerator.incrementAndGet());   // генерируем числовой ID
        comment.setKey(key);
        return mapper.toDto(repository.save(comment));
    }

    public List<CommentResponseTo> getByTopic(Long topicId) {
        return repository.findByKeyTopicId(topicId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long topicId, Long id) {
        CommentKey key = new CommentKey();
        key.setTopicId(topicId);
        key.setId(id);
        repository.deleteById(key);
    }

    public List<CommentResponseTo> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public CommentResponseTo getById(Long id) {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        return mapper.toDto(comment);
    }

    public CommentResponseTo update(CommentRequestTo requestTo) {
        Long id = requestTo.getId();
        Long topicId = requestTo.getTopicId();
        CommentKey key = new CommentKey();
        key.setTopicId(topicId);
        key.setId(id);
        Comment existing = repository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        existing.setContent(requestTo.getContent());
        return mapper.toDto(repository.save(existing));
    }

    public void deleteById(Long id) {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        delete(comment.getKey().getTopicId(), id);
    }
}