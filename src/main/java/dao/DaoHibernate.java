package dao;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class DaoHibernate<T, PK extends Serializable> implements Dao<T, PK> {
    private Class<T> type;
    private SessionFactory sessionFactory;
    private Session currentSession;
    private Transaction currentTransaction;

    @SuppressWarnings("unchecked")
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

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
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
        try{
            openSession();
            entity = (T) currentSession.get(type, pk);
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
        List<T> entities = currentSession.createQuery(criteria).getResultList();
        return entities;
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
