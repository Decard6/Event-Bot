package dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T extends model.Entity<PK>, PK extends Serializable> {

    void persist(T entity);
    void update(T entity);
    T findById(PK pk);
    T findCustom(String field, long id);
    void delete(T entity);
    List<T> findAll();
    void deleteAll();

}
