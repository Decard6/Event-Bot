package action;

import net.dv8tion.jda.api.JDA;
import org.hibernate.SessionFactory;

import java.util.Queue;

public class ActionRunnable implements Runnable {
    private Queue<Action> actionQueue;
    private SessionFactory sessionFactory;
    private JDA jda;

    public ActionRunnable(Queue<Action> actionQueue, SessionFactory sessionFactory, JDA jda){
        this.actionQueue = actionQueue;
        this.sessionFactory = sessionFactory;
        this.jda = jda;
    }


    @Override
    public void run() {
        while(true){
            while(!actionQueue.isEmpty()){
                Action action = actionQueue.poll();
                action.execute(sessionFactory, jda);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
