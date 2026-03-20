package com.bsuir.distcomp.repository.in_memory_impl;

import com.bsuir.distcomp.entity.Writer;
import com.bsuir.distcomp.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class WriterRepositoryImpl implements CrudRepository<Writer> {

    private final Map<Long, Writer> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Writer save(Writer writer) {
        long id = idGenerator.incrementAndGet();
        writer.setId(id);
        storage.put(id, writer);
        return writer;
    }

    @Override
    public Writer findById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Writer> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Writer update(Long id, Writer writer) {
        writer.setId(id);
        storage.put(id, writer);
        return writer;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}

