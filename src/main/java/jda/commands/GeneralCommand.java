package jda.commands;

import action.Action;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Queue;

public class GeneralCommand implements Command{
    private Command commandEvent;
    private Command commandCharacter;
    private JDA jda;
    private Queue<Action> actionQueue;

    public GeneralCommand(JDA jda, Queue<Action> actionQueue){
        this.jda = jda;
        commandEvent = new EventCommand(jda, actionQueue);
        commandCharacter = new CharacterCommand(jda, actionQueue);
        this.actionQueue = actionQueue;
    }

    @Override
    public void execute(String commandString, GuildMessageReceivedEvent guildMessageReceivedEvent) {
        String eventIdentifier = "";
        String charIdentifier = "";
        if (commandString.length() >= 6)
            eventIdentifier = commandString.substring(0, 6);
        if (commandString.length() >= 5)
            charIdentifier = commandString.substring(0, 5);

        if(eventIdentifier.equalsIgnoreCase("event ")){
            String arguments = commandString.substring(6);
            commandEvent.execute(arguments, guildMessageReceivedEvent);
        } else if(charIdentifier.equalsIgnoreCase("char ")) {
            String arguments = commandString.substring(5);
            commandCharacter.execute(arguments, guildMessageReceivedEvent);
        } else {
            String author = guildMessageReceivedEvent.getAuthor().getAsMention();
            String error = "Not a command.";
            String msg = String.format("%s %s", error, author);
            guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
        }
    }
}
