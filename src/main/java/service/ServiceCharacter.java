package service;

import model.Character;
import java.util.List;

public interface ServiceCharacter {
    void persist(Character entity);
    void update(Character entity);
    Character findById(String name);
    void delete(String name);
    List<Character> findAll();
    void deletaAll();
}
