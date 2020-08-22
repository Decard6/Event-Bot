package jda.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class CommandListener extends ListenerAdapter {
    private char prefix = '!';
    private JDA jda;
    private Command command;

    public CommandListener(JDA jda){
        super();
        this.jda = jda;
        command = new GeneralCommand(jda);
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent guildMessageReceivedEvent){
        String message = guildMessageReceivedEvent.getMessage().getContentRaw();

        //TODO - Security

        if(message.charAt(0) == prefix){
            String commandString = message.substring(1);
            command.execute(commandString, guildMessageReceivedEvent);
        }

    }
}
