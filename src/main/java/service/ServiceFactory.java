package service;

import model.Character;
import model.Event;
import model.SignUp;
import model.SignUpId;
import org.hibernate.SessionFactory;

public class ServiceFactory {
    private String daoType;
    private SessionFactory sessionFactory;

    public ServiceFactory(SessionFactory sessionFactory, String daoType){
        this.sessionFactory = sessionFactory;
        this.daoType = daoType;
    }

    public ServiceImpl<model.Character, String> getCharacterService(){
        return new ServiceImpl<>(sessionFactory, "HIBERNATE", Character.class);
    }

    public ServiceImpl<Event, Long> getEventService(){
        return new ServiceImpl<>(sessionFactory, "HIBERNATE", Event.class);
    }

    public ServiceImpl<SignUp, SignUpId> getSignUpService(){
        return new ServiceImpl<>(sessionFactory, "HIBERNATE", SignUp.class);
    }
}
