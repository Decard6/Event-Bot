
import model.Character;
import model.Event;
import model.SignUp;
import model.SignUpId;
import org.hibernate.SessionFactory;
import service.Service;
import service.ServiceFactory;

public class Main {

    public static void main(String[] args){
        SessionFactory sessionFactory = util.HibernateUtil.getSessionFactory();
        ServiceFactory serviceFactory = new ServiceFactory(sessionFactory, "HIBERNATE");
        Service<Character, String> serviceCharacter = serviceFactory.getCharacterService();
        Service<SignUp, SignUpId> signUpService = serviceFactory.getSignUpService();
        Service<Event,Long> eventService = serviceFactory.getEventService();
    }
}
