package dao;

import model.Fetchable;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class DaoHibernate<T extends model.Entity<PK>, PK extends Serializable> implements Dao<T, PK> {
    private Class<T> type;
    private SessionFactory sessionFactory;
    private Session currentSession;
    private Transaction currentTransaction;


    public DaoHibernate(SessionFactory sessionFactory, Class<T> type){
        this.sessionFactory = sessionFactory;
        this.type = type;
    }

    private Session openSession() {
        currentSession = sessionFactory.openSession();
        return currentSession;
    }

    private Session openSessionWithTransaction() {
        currentSession = sessionFactory.openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    private void closeSession() {
        currentSession.close();
    }

    private void closeSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    @Override
    public void persist(T entity) {
        try{
            openSessionWithTransaction();
            currentSession.save(entity);
        } finally {
            closeSessionwithTransaction();
        }
    }

    @Override
    public void update(T entity) {
        try {
            openSessionWithTransaction();
            currentSession.update(entity);
        } finally {
            closeSessionwithTransaction();
        }
    }

    @Override
    public T findById(PK pk) {
        T entity;
        try {
            openSession();
            entity = (T) currentSession.get(type, pk);
            if(entity instanceof Fetchable){
                Hibernate.initialize(((Fetchable) entity).fetch());
            }
        } finally {
            closeSession();
        }
        return entity;
    }

    private void deleteNoConnection(T entity){
        currentSession.delete(entity);
    }

    @Override
    public void delete(T entity) {
        try{
            openSessionWithTransaction();
            deleteNoConnection(entity);
        } finally {
            closeSessionwithTransaction();
        }
    }

    private List<T> findAllNoConnection(){
        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        return currentSession.createQuery(criteria).getResultList();
    }

    @Override
    public List<T> findAll() {
        openSession();
        List<T> entities = findAllNoConnection();
        closeSession();
        return entities;
    }

    @Override
    public void deleteAll() {
        try{
            openSessionWithTransaction();
            List<T> entities = findAllNoConnection();
            for(T entity : entities)
                deleteNoConnection(entity);
        } finally {
            closeSessionwithTransaction();
        }
    }
}
