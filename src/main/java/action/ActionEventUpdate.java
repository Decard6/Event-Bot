package action;

import model.Event;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.hibernate.SessionFactory;
import service.Service;
import service.ServiceFactory;

import java.util.Date;

public class ActionEventUpdate implements Action {
    private long id;
    private String name;
    private Date date;

    public ActionEventUpdate(long id, String name, Date date){
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Override
    public void execute(SessionFactory sessionFactory, JDA jda) {
        //initialize service
        ServiceFactory serviceFactory = new ServiceFactory(sessionFactory, daoType);
        Service<Event, Long> eventService = serviceFactory.getEventService();

        //find event
        Event event = eventService.findById(id);
        if(event == null)
            return;

        //update values
        event.setEventName(name);
        event.setEventDate(date);

        //update event
        eventService.update(event);

        //refresh event message
        TextChannel eventChannel = jda.getTextChannelById(event.getChannelID());
        String messageString = event.toString();
        long messageId = event.getMessageID();
        assert eventChannel != null;
        eventChannel.editMessageById(messageId, messageString).queue();
    }
}
