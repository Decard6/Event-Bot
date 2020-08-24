package jda.reactions;

import action.Action;
import action.ActionEventAdd;
import action.ActionSignUp;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Queue;
import java.util.regex.Pattern;

public class ReactionSignUpListener extends ListenerAdapter {
    private static String emoteSignUp = null;
    private static String emoteTentative = null;
    private static String emoteOut = null;
    private JDA jda;
    private Queue<Action> actionQueue;

    public ReactionSignUpListener(JDA jda, Queue<Action> actionQueue){
        super();
        this.jda = jda;
        this.actionQueue = actionQueue;
    }

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

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event){
        if(event.getUser().isBot())
            return;

        byte status;
        if(event.getReactionEmote().toString().substring(3).equals(emoteSignUp)) {
            status = (byte)1;
        } else if(event.getReactionEmote().toString().substring(3).equals(emoteTentative)) {
            status = (byte)2;
        } else if(event.getReactionEmote().toString().substring(3).equals(emoteOut)) {
            status = (byte)3;
        } else
            return;

        long messageId = event.getMessageIdLong();
        long userId = event.getUserIdLong();

        Action action = new ActionSignUp(messageId, userId, status);
        offer(action);
        removeReaction(event);

    }

    private void removeReaction(@Nonnull GuildMessageReactionAddEvent event){
        TextChannel channel = event.getChannel();
        long id = event.getMessageIdLong();
        String emoteString = event.getReactionEmote().toString().substring(3);

        channel.retrieveMessageById(id).queue((message) -> {
            message.removeReaction(emoteString, event.getUser()).queue();
        });
    }

    private boolean offer(Action action) {
        return actionQueue.offer(action);
    }
}
