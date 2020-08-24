package action;

import model.Character;
import model.Event;
import model.SignUp;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.hibernate.SessionFactory;
import service.Service;
import service.ServiceFactory;

public class ActionCharacterUpdate implements Action{
    private String name;
    private String className;
    private String spec;


    public ActionCharacterUpdate(String name, String className, String spec){
        this.name = name;
        this.className = className;
        this.spec = spec;
    }

    @Override
    public void execute(SessionFactory sessionFactory, JDA jda) {
        //initialize service
        ServiceFactory serviceFactory = new ServiceFactory(sessionFactory, daoType);
        Service<Character, String> characterService = serviceFactory.getCharacterService();
        Service<Event, Long> eventService = serviceFactory.getEventService();

        Character character = characterService.findById(name);
        if(character == null)
            return;

        character.setClassName(className);
        character.setSpec(spec);
        characterService.update(character);

        for(SignUp signUp : character.getSignUps()){
            //fetch collection
            Event event = eventService.findById(signUp.getID().getEventId());

            //update message
            TextChannel eventChannel = jda.getTextChannelById(event.getChannelId());
            String messageString = event.toString();
            long messageId = event.getMessageId();
            assert eventChannel != null;
            eventChannel.editMessageById(messageId, messageString).queue();
        }
    }
}
