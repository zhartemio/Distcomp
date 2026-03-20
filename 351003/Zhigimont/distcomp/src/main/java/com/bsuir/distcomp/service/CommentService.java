package com.bsuir.distcomp.service;

import com.bsuir.distcomp.dto.CommentRequestTo;
import com.bsuir.distcomp.dto.CommentResponseTo;
import com.bsuir.distcomp.entity.Comment;
import com.bsuir.distcomp.entity.Topic;
import com.bsuir.distcomp.exception.EntityNotFoundException;
import com.bsuir.distcomp.mapper.CommentMapper;
import com.bsuir.distcomp.repository.CommentRepository;
import com.bsuir.distcomp.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;
    private final TopicRepository topicRepository;
    private final CommentMapper mapper;

    public CommentResponseTo create(CommentRequestTo dto) {

        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        Comment comment = mapper.toEntity(dto);
        comment.setTopic(topic);

        Comment saved = repository.save(comment);

        return mapper.toDto(saved);
    }

    public List<CommentResponseTo> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public CommentResponseTo getById(Long id) {
        Comment comment = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Comment not found with id " + id));

        return mapper.toDto(comment);
    }

    public CommentResponseTo update(Long id, CommentRequestTo dto) {

        Comment existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Comment not found with id " + id));

        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        existing.setContent(dto.getContent());
        existing.setTopic(topic);

        return mapper.toDto(repository.save(existing));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Comment not found with id " + id);
        }

        repository.deleteById(id);
    }
}
