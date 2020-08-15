package service;

import model.Character;

import java.util.List;

public interface Service<T, PK> {
    void persist(T entity);
    void update(T entity);
    Character findById(PK pk);
    void delete(PK pk);
    List<Character> findAll();
    void deletaAll();
}
