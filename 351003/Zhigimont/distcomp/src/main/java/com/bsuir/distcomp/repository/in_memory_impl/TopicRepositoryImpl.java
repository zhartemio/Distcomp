package com.bsuir.distcomp.repository.in_memory_impl;

import com.bsuir.distcomp.entity.Topic;
import com.bsuir.distcomp.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TopicRepositoryImpl implements CrudRepository<Topic> {

    private final Map<Long, Topic> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Topic save(Topic topic) {
        Long id = idGenerator.incrementAndGet();
        topic.setId(id);
        storage.put(id, topic);
        return topic;
    }

    @Override
    public Topic findById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Topic> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Topic update(Long id, Topic topic) {
        topic.setId(id);
        storage.put(id, topic);
        return topic;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
