package service;

import model.Character;

import java.io.Serializable;
import java.util.List;

public interface Service<T extends model.Entity, PK extends Serializable> {
    void persist(T entity);
    void update(T entity);
    T findById(PK pk);
    void delete(PK pk);
    List<T> findAll();
    void deletaAll();
}
