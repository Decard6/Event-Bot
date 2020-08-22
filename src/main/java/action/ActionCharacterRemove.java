package action;

import model.Character;
import net.dv8tion.jda.api.JDA;
import org.hibernate.SessionFactory;
import service.Service;
import service.ServiceFactory;

public class ActionCharacterRemove implements Action {
    private String name;

    public ActionCharacterRemove(String name){
        this.name = name;
    }

    @Override
    public void execute(SessionFactory sessionFactory, JDA jda) {
        //initialize service
        ServiceFactory serviceFactory = new ServiceFactory(sessionFactory, daoType);
        Service<Character, String> characterService = serviceFactory.getCharacterService();

        characterService.delete(name);
    }
}
