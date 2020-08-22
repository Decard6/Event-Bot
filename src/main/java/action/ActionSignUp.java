package action;

import model.SignUp;
import net.dv8tion.jda.api.JDA;
import org.hibernate.SessionFactory;

public class ActionSignUp implements Action {
    private long eventId;
    private String characterName;
    private byte status;

    public ActionSignUp(long eventIdd, String characterName, byte status){
    }

    @Override
    public void execute(SessionFactory sessionFactory, JDA jda) {

    }
}
