package com.github.dangelcrack.model.dao;

import java.io.Closeable;
import java.util.List;
public interface DAO<T, K> extends Closeable {
    T save(T entity);
    T delete(T entity);
    T findByName(K key);
    List<T> findAll();
}