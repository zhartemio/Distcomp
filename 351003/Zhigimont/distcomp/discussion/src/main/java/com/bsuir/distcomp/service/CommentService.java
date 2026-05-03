package com.bsuir.distcomp.service;

import com.bsuir.distcomp.dto.CommentListResponseTo;
import com.bsuir.distcomp.dto.CommentRequestTo;
import com.bsuir.distcomp.dto.CommentResponseTo;
import com.bsuir.distcomp.entity.Comment;
import com.bsuir.distcomp.entity.CommentKey;
import com.bsuir.distcomp.exception.EntityNotFoundException;
import com.bsuir.distcomp.kafka.CommentResponseProducer;
import com.bsuir.distcomp.mapper.CommentMapper;
import com.bsuir.distcomp.repository.CommentRepository;
import com.bsuir.types.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final CommentResponseProducer producer;

    private final AtomicLong idGenerator = new AtomicLong(System.currentTimeMillis());

    public void process(CommentRequestTo request) {

        switch (request.getOperation()) {

            case CREATE -> create(request);

            case UPDATE -> handleUpdate(request);

            case DELETE -> handleDelete(request);

            case GET_ALL -> handleGetAll(request);
            case GET_BY_ID -> handleGetById(request);
        }
    }


    private void handleGetAll(CommentRequestTo request) {

        List<CommentResponseTo> list = repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();

        CommentListResponseTo response = new CommentListResponseTo();
        response.setComments(list);
        response.setCorrelationId(request.getCorrelationId());

        producer.sendList(response);
    }


    private void handleGetById(CommentRequestTo request) {

        CommentKey key = new CommentKey();
        key.setTopicId(request.getTopicId());
        key.setId(request.getId());

        Comment comment = repository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        CommentResponseTo response = mapper.toDto(comment);
        response.setStatus(comment.getStatus());

        response.setCorrelationId(request.getCorrelationId());

        producer.send(response);
    }

    private void create(CommentRequestTo request) {
        Comment comment = mapper.toEntity(request);

        CommentKey key = new CommentKey();
        key.setTopicId(request.getTopicId());
        key.setId(idGenerator.incrementAndGet());

        comment.setKey(key);


        Status state = moderate(request.getContent());
        comment.setStatus(state);

        Comment saved = repository.save(comment);

        sendResponse(saved, request.getCorrelationId());
    }



    private void handleUpdate(CommentRequestTo request) {
        CommentKey key = new CommentKey();
        key.setTopicId(request.getTopicId());
        key.setId(request.getId());

        Comment existing = repository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        existing.setContent(request.getContent());


        Status state = moderate(request.getContent());
        existing.setStatus(state);

        Comment saved = repository.save(existing);

        sendResponse(saved, request.getCorrelationId());
    }


    private void handleDelete(CommentRequestTo request) {
        CommentKey key = new CommentKey();
        key.setTopicId(request.getTopicId());
        key.setId(request.getId());

        repository.deleteById(key);


        CommentResponseTo response = new CommentResponseTo();
        response.setId(request.getId());
        response.setTopicId(request.getTopicId());
        response.setStatus(Status.APPROVE);
        response.setCorrelationId(request.getCorrelationId());

        producer.send(response);
    }

    private Status moderate(String content) {
        if (content == null) return Status.DECLINE;


        if (content.toLowerCase().contains("spam") ||
                content.toLowerCase().contains("bad")) {
            return Status.DECLINE;
        }

        return Status.APPROVE;
    }

    private void sendResponse(Comment comment, String correlationId) {
        CommentResponseTo response = mapper.toDto(comment);
        response.setCorrelationId(correlationId);

        producer.send(response);
    }
}