package com.bsuir.distcomp.repository.in_memory_impl;

import com.bsuir.distcomp.entity.Marker;
import com.bsuir.distcomp.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MarkerRepositoryImpl implements CrudRepository<Marker> {

    private final Map<Long, Marker> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Marker save(Marker marker) {
        Long id = idGenerator.incrementAndGet();
        marker.setId(id);
        storage.put(id, marker);
        return marker;
    }

    @Override
    public Marker findById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Marker> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Marker update(Long id, Marker marker) {
        marker.setId(id);
        storage.put(id, marker);
        return marker;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
