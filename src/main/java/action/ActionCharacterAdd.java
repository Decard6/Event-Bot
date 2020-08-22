package action;

import model.Event;
import net.dv8tion.jda.api.JDA;
import org.hibernate.SessionFactory;
import model.Character;
import service.Service;
import service.ServiceFactory;

public class ActionCharacterAdd implements Action {
    private Character character;

    public ActionCharacterAdd(String name, long discordId, String className, String spec){
        character = new Character(discordId, name, className, spec);
    }

    @Override
    public void execute(SessionFactory sessionFactory, JDA jda) {
        //initialize service
        ServiceFactory serviceFactory = new ServiceFactory(sessionFactory, daoType);
        Service<Character, String> characterService = serviceFactory.getCharacterService();
        characterService.persist(character);
    }
}
