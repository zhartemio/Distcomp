package com.bsuir.distcomp.repository.in_memory_impl;

import com.bsuir.distcomp.entity.Comment;
import com.bsuir.distcomp.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CommentRepositoryImpl implements CrudRepository<Comment> {

    private final Map<Long, Comment> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Comment save(Comment comment) {
        Long id = idGenerator.incrementAndGet();
        comment.setId(id);
        storage.put(id, comment);
        return comment;
    }

    @Override
    public Comment findById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Comment> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Comment update(Long id, Comment comment) {
        comment.setId(id);
        storage.put(id, comment);
        return comment;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
