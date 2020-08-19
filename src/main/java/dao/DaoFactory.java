package dao;

import org.hibernate.SessionFactory;

import java.io.Serializable;

public class DaoFactory<T extends model.Entity<PK>, PK extends Serializable> {
    private SessionFactory sessionFactory;
    private Class<T> type;

    public DaoFactory(SessionFactory sessionFactory, Class<T> type){
        this.sessionFactory = sessionFactory;
        this.type = type;
    }

    public Dao<T, PK> getDao(String daoType){
        if(daoType == null)
            return null;
        else if(daoType.equalsIgnoreCase("HIBERNATE"))
            return new DaoHibernate<>(sessionFactory, type);

        return null;
    }
}
