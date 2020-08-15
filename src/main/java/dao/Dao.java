package dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, PK extends Serializable> {

    void persist(T entity);
    void update(T entity);
    T findById(PK pk);
    void delete(T entity);
    List<T> findAll();
    void deleteAll();

}
