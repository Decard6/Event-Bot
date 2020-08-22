package action;


import net.dv8tion.jda.api.JDA;
import org.hibernate.SessionFactory;

public interface Action {
    String daoType = "HIBERNATE";
    void execute(SessionFactory sessionFactory, JDA jda);
}
