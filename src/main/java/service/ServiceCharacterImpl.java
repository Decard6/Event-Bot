package service;

import dao.Dao;
import dao.DaoFactory;
import org.hibernate.SessionFactory;
import model.Character;

import java.util.List;

public class ServiceCharacterImpl implements ServiceCharacter {
    private Dao<Character,String> dao;

    @SuppressWarnings("unchecked")
    public ServiceCharacterImpl(SessionFactory sessionFactory, String daoType){
        DaoFactory daoFactory = new DaoFactory<Character, String>(sessionFactory, Character.class);
        dao = (Dao<Character,String>)daoFactory.getDao(daoType);
    }

    @Override
    public void persist(Character entity) {
        Character found = dao.findById(entity.getCharName());
        if(found == null)
            dao.persist(entity);
    }

    @Override
    public void update(Character entity) {
        Character found = dao.findById(entity.getCharName());
        if(found != null)
            dao.update(entity);
    }

    @Override
    public Character findById(String name) {
        return dao.findById(name);
    }

    @Override
    public void delete(String name) {
        Character found = dao.findById(name);
        if(found != null)
            dao.delete(found);
    }

    @Override
    public List<Character> findAll() {
        List<Character> entities = dao.findAll();
        return entities;
    }

    @Override
    public void deletaAll() {
        dao.deleteAll();
    }
}
