package uk.co.gyotools.selfmetrics.dao;

import java.util.List;

public interface CrudDao<T, ID> {
    ID save(T obj);

    void delete(ID id);

    T findById(ID id);

    List<T> findAll();
}
