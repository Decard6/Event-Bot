package action;

import model.Event;
import model.SignUp;
import model.SignUpId;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.hibernate.SessionFactory;
import service.Service;
import service.ServiceFactory;

public class ActionConfirmAll implements Action {
    private long id;

    public ActionConfirmAll(long id){
        this.id = id;
    }

    @Override
    public void execute(SessionFactory sessionFactory, JDA jda) {
        //initialize service
        ServiceFactory serviceFactory = new ServiceFactory(sessionFactory, daoType);
        Service<Event, Long> eventService = serviceFactory.getEventService();
        Service<SignUp, SignUpId> signUpService = serviceFactory.getSignUpService();

        //find event
        Event event = eventService.findById(id);
        if(event == null)
            return;

        //confirm each signed up person
        for(SignUp signUp : event.getSignUps()){
            if(signUp.getSignUpStatus() == 1 && signUp.getConfirmStatus() == 0){
                signUp.setConfirmStatus((byte)1);
                signUpService.update(signUp);
            }
        }

        //refetch
        event = eventService.findById(id);

        //refresh event message
        TextChannel eventChannel = jda.getTextChannelById(event.getChannelId());
        String messageString = event.toString();
        long messageId = event.getMessageId();
        assert eventChannel != null;
        eventChannel.editMessageById(messageId, messageString).queue();
    }
}
