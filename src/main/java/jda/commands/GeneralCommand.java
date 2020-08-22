package jda.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GeneralCommand implements Command{
    private Command commandEvent;
    private Command commandCharacter;
    private JDA jda;

    public GeneralCommand(JDA jda){
        this.jda = jda;
        commandEvent = new EventCommand(jda);
        commandCharacter = new CharacterCommand(jda);
    }

    @Override
    public void execute(String commandString, GuildMessageReceivedEvent guildMessageReceivedEvent) {
        String eventIdentifier = commandString.substring(0, 6);
        String charIdentifier = commandString.substring(0, 5);

        if(eventIdentifier.equalsIgnoreCase("event ")){
            String arguments = commandString.substring(6);
            commandEvent.execute(arguments, guildMessageReceivedEvent);
        } else if(charIdentifier.equalsIgnoreCase("char ")) {
            System.out.println("character command");
        } else {
            System.out.println("not a command");
        }
    }
}
