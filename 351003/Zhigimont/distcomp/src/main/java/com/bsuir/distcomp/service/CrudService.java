package com.bsuir.distcomp.service;


import java.math.BigInteger;
import java.util.List;

public interface CrudService<T, R> {

    void create(T t);
    List<R> getAll();
    void delete(T t);
    R update(T t);
    R getById(BigInteger id);
}
