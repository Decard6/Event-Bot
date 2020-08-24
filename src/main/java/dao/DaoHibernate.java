package dao;

import model.Fetchable;
import model.HasCustomSearch;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

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

    @Override
    public T findCustom(String field, long id) {
        if(HasCustomSearch.class.isAssignableFrom(type)){
            T entity;
            try {
                openSession();

                CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
                CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
                Root<T> root = criteriaQuery.from(type);
                criteriaQuery.select(root);

                ParameterExpression<Long> params = criteriaBuilder.parameter(Long.class);
                criteriaQuery.where(criteriaBuilder.equal(root.get(field), params));

                TypedQuery<T> query = currentSession.createQuery(criteriaQuery);
                query.setParameter(params, id);

                List<T> queryResult = query.getResultList();

                entity = null;

                if (!queryResult.isEmpty()) {
                    entity = queryResult.get(0);
                }


                if(entity instanceof Fetchable){
                    Hibernate.initialize(((Fetchable) entity).fetch());
                }
            } finally {
                closeSession();
            }
            return entity;
        }
        return null;
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
