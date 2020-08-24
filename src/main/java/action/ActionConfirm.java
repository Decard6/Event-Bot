package action;

import model.Event;
import model.SignUp;
import model.SignUpId;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.hibernate.SessionFactory;
import service.Service;
import service.ServiceFactory;

public class ActionConfirm implements Action {
    private long id;
    private String name;
    private byte status;

    public ActionConfirm(long id, String name, byte status){
        this.id = id;
        this.name = name;
        this.status = status;
    }

    @Override
    public void execute(SessionFactory sessionFactory, JDA jda) {
        //initialize service
        ServiceFactory serviceFactory = new ServiceFactory(sessionFactory, daoType);
        Service<Event, Long> eventService = serviceFactory.getEventService();
        Service<SignUp, SignUpId> signUpService = serviceFactory.getSignUpService();

        //find sign up and confirm
        SignUpId signUpId = new SignUpId(id, name);
        SignUp signUp = signUpService.findById(signUpId);
        if(signUp == null)
            return;
        signUp.setConfirmStatus(status);
        signUpService.update(signUp);

        //fetch event and refresh event message
        Event event = eventService.findById(id);
        TextChannel eventChannel = jda.getTextChannelById(event.getChannelId());
        String messageString = event.toString();
        long messageId = event.getMessageId();
        assert eventChannel != null;
        eventChannel.editMessageById(messageId, messageString).queue();

    }
}
