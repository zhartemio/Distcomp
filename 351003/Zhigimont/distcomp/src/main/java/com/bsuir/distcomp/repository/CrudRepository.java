package com.bsuir.distcomp.repository;

import java.util.List;

public interface CrudRepository<T> {

    T save(T entity);

    T findById(Long id);

    List<T> findAll();

    T update(Long id, T entity);

    void delete(Long id);

}

