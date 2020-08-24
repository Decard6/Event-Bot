package jda;

import action.Action;
import action.ActionRunnable;
import jda.commands.CommandListener;
import jda.reactions.ReactionSignUpListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

import javax.security.auth.login.LoginException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

public class Main {

    public static void main(String[] args) throws LoginException {
        String token = System.getenv("TOKEN");
        JDA jda = JDABuilder.create(token, GUILD_MESSAGES, GUILD_MESSAGE_REACTIONS, GUILD_MEMBERS).build();
        Queue<Action> actionQueue = new ConcurrentLinkedQueue<>();
        jda.addEventListener(new CommandListener(jda, actionQueue));
        jda.addEventListener(new ReactionSignUpListener(jda, actionQueue));
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Runnable actionRunnable = new ActionRunnable(actionQueue, sessionFactory, jda);
        Thread actionThread = new Thread(actionRunnable);
        actionThread.setDaemon(true);
        actionThread.start();
    }
}
