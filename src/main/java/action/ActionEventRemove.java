package action;

import model.Event;
import net.dv8tion.jda.api.JDA;
import org.hibernate.SessionFactory;
import service.Service;
import service.ServiceFactory;

public class ActionEventRemove implements Action {
    private long id;

    public ActionEventRemove(long id){
        this.id = id;
    }

    @Override
    public void execute(SessionFactory sessionFactory, JDA jda) {
        ServiceFactory serviceFactory = new ServiceFactory(sessionFactory, daoType);
        Service<Event, Long> eventService = serviceFactory.getEventService();
        eventService.delete(id);
    }
}
