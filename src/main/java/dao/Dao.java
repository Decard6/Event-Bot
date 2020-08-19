package dao;

import java.io.Serializable;
import java.util.List;
import model.Entity;

public interface Dao<T extends model.Entity<PK>, PK extends Serializable> {

    void persist(T entity);
    void update(T entity);
    T findById(PK pk);
    void delete(T entity);
    List<T> findAll();
    void deleteAll();

}
