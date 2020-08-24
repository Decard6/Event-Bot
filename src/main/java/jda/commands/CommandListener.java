package jda.commands;

import action.Action;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Queue;

public class CommandListener extends ListenerAdapter {
    private char prefix = '!';
    private JDA jda;
    private Command command;
    private Queue<Action> actionQueue;

    public CommandListener(JDA jda, Queue<Action> actionQueue){
        super();
        this.jda = jda;
        this.actionQueue = actionQueue;
        command = new GeneralCommand(jda, actionQueue);
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent guildMessageReceivedEvent){
        String message = guildMessageReceivedEvent.getMessage().getContentRaw();

        if(guildMessageReceivedEvent.getAuthor().isBot())
            return;

        if(!(guildMessageReceivedEvent.getChannel().getName().equalsIgnoreCase(System.getenv("COMMAND_CHANNEL"))))
            return;

        Guild guild = guildMessageReceivedEvent.getGuild();
        User user = guildMessageReceivedEvent.getAuthor();
        Member member = guild.getMember(user);
        Role role = guild.getRolesByName("Event Bot Moderator", true).get(0);
        List<Member> members = guild.getMembersWithRoles(role);
        if(!members.contains(member))
            return;

        if(message.charAt(0) == prefix){
            String commandString = message.substring(1);
            command.execute(commandString, guildMessageReceivedEvent);
        }

    }
}
