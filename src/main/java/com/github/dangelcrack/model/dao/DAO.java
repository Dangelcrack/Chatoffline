package com.github.dangelcrack.model.dao;

import java.io.Closeable;
import java.util.List;

/**
 * DAO Interface to manage basic CRUD operations.
 *
 * @param <T> the type of the entity
 * @param <K> the type of the entity's identifier
 */
public interface DAO<T, K> extends Closeable {

    /**
     * Save an entity.
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    T save(T entity);

    /**
     * Delete an entity.
     *
     * @param entity the entity to delete
     * @return the deleted entity
     */
    T delete(T entity);

    /**
     * Find an entity by its name.
     *
     * @param key the name of the entity
     * @return the found entity
     */
    T findByName(K key);

    /**
     * Find all entities.
     *
     * @return the list of all entities
     */
    List<T> findAll();
}
