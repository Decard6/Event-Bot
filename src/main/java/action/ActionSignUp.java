package action;

import model.Character;
import model.Event;
import model.SignUp;
import model.SignUpId;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.hibernate.SessionFactory;
import service.Service;
import service.ServiceFactory;

public class ActionSignUp implements Action {
    private long messageId;
    private long userId;
    private byte status;

    public ActionSignUp(long messageId, long userId, byte status){
        this.messageId = messageId;
        this.userId = userId;
        this.status = status;
    }

    @Override
    public void execute(SessionFactory sessionFactory, JDA jda) {
        //initialize service
        ServiceFactory serviceFactory = new ServiceFactory(sessionFactory, daoType);
        Service<Event, Long> eventService = serviceFactory.getEventService();
        Service<SignUp, SignUpId> signUpService = serviceFactory.getSignUpService();
        Service<Character, String> characterService = serviceFactory.getCharacterService();

        //find event and character
        Event event = eventService.findCustom(Event.getCustomId(), messageId);
        Character character = characterService.findCustom(Character.getCustomId(), userId);
        if(event == null || character == null)
            return;

        //find if sign up exists
        SignUpId signUpId = new SignUpId(event.getEventId(), character.getCharName());
        SignUp signUp = signUpService.findById(signUpId);
        if(signUp != null){
            signUp.setSignUpStatus(status);
            signUpService.update(signUp);
        } else {
            signUp = new SignUp(event, character, status);
            signUpService.persist(signUp);
        }

        //fetch collection and update message
        event = eventService.findById(signUp.getID().getEventId());
        TextChannel eventChannel = jda.getTextChannelById(event.getChannelId());
        String messageString = event.toString();
        long messageId = event.getMessageId();
        assert eventChannel != null;
        eventChannel.editMessageById(messageId, messageString).queue();

    }
}
