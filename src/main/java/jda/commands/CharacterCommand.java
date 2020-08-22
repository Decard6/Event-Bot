package jda.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CharacterCommand implements Command {
    private JDA jda;

    CharacterCommand(JDA jda){
        this.jda = jda;
    }

    @Override
    public void execute(String commandString, GuildMessageReceivedEvent event) {

    }
}
