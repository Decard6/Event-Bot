package action;

import model.Event;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.RestAction;
import org.hibernate.SessionFactory;
import service.Service;
import service.ServiceFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class ActionEventAdd implements Action {
    private Event event;
    private static String emoteSignUp = null;
    private static String emoteTentative = null;
    private static String emoteOut = null;

    static {
        Properties properties = new Properties();
        String propertiesFileName = "config.properties";


        try(InputStream inputStream = ActionEventAdd.class.getClassLoader().getResourceAsStream(propertiesFileName))
        {
            if(inputStream != null)
                properties.load(inputStream);
            else
                throw new FileNotFoundException("Properties file not found.");
            emoteSignUp = properties.getProperty("emoteSignUp");
            emoteTentative = properties.getProperty("emoteTentative");
            emoteOut = properties.getProperty("emoteOut");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ActionEventAdd(String eventName, Date date, long channelId){
        event = new Event(eventName, date, channelId);
    }

    @Override
    public void execute(SessionFactory sessionFactory, JDA jda) {
        //initialize service
        ServiceFactory serviceFactory = new ServiceFactory(sessionFactory, daoType);
        Service<Event, Long> eventService = serviceFactory.getEventService();

        //create DB entity
        eventService.persist(event);

        //fetch collection
        event = eventService.findById(event.getID());

        //add message
        TextChannel eventChannel = jda.getTextChannelById(event.getChannelID());
        assert eventChannel != null;
        RestAction<Message> restAction = eventChannel.sendMessage("Placeholder");
        Message message = restAction.complete();
        long messageId = message.getIdLong();
        message.editMessage(event.toString()).queue();

        //add emotes
        message.addReaction(emoteSignUp).queue();
        message.addReaction(emoteTentative).queue();
        message.addReaction(emoteOut).queue();;

        //update messageId in db
        event.setMessageID(messageId);
        eventService.update(event);
    }
}
