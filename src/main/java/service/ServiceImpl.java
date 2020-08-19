package service;

import dao.Dao;
import dao.DaoFactory;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class ServiceImpl<T extends model.Entity<PK>, PK extends Serializable> implements Service<T, PK> {
    private Dao<T, PK> dao;

    public ServiceImpl(SessionFactory sessionFactory, String daoType, Class<T> type){
        DaoFactory<T, PK> daoFactory = new DaoFactory<>(sessionFactory, type);
        dao = daoFactory.getDao(daoType);
    }

    @Override
    public void persist(T entity) {
        T found = dao.findById(entity.getID());
        if(found == null)
            dao.persist(entity);
    }

    @Override
    public void update(T entity) {
        T found = dao.findById(entity.getID());
        if(found != null)
            dao.update(entity);
    }

    @Override
    public T findById(PK pk) {
        return dao.findById(pk);
    }

    @Override
    public void delete(PK pk) {
        T found = dao.findById(pk);
        if(found != null)
            dao.delete(found);
    }

    @Override
    public List<T> findAll() {
        return dao.findAll();
    }

    @Override
    public void deletaAll() {
        dao.deleteAll();
    }
}
