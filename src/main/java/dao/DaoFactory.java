package dao;

import org.hibernate.SessionFactory;

import java.io.Serializable;

public class DaoFactory<T, PK extends Serializable> {
    private SessionFactory sessionFactory;
    private Class<T> type;

    public DaoFactory(SessionFactory sessionFactory, Class<T> type){
        this.sessionFactory = sessionFactory;
        this.type = type;
    }

    public Dao getDao(String daoType){
        if(daoType == null)
            return null;
        else if(daoType.equalsIgnoreCase("HIBERNATE"))
            return new DaoHibernate<T, PK>(sessionFactory, type);

        return null;
    }
}
