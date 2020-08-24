package jda.commands;

import action.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Objects;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterCommand implements Command {
    private JDA jda;
    private Queue<Action> actionQueue;

    CharacterCommand(JDA jda, Queue<Action> actionQueue){
        this.jda = jda;
        this.actionQueue = actionQueue;
    }

    @Override
    public void execute(String string, GuildMessageReceivedEvent guildMessageReceivedEvent) {
        String addOrHelp = "";
        String removeOrUpdate = "";

        if(string.length() >= 4)
            addOrHelp = string.substring(0, 4);
        if(string.length() >= 7)
            removeOrUpdate = string.substring(0, 7);

        if(addOrHelp.equalsIgnoreCase("add ")){
            String arguments = string.substring(4);
            add(arguments, guildMessageReceivedEvent);
        } else if(removeOrUpdate.equalsIgnoreCase("remove ")){
            String arguments = string.substring(7);
            remove(arguments, guildMessageReceivedEvent);
        } else if(removeOrUpdate.equalsIgnoreCase("update ")){
            String arguments = string.substring(7);
            update(arguments, guildMessageReceivedEvent);
        } else if(addOrHelp.equalsIgnoreCase("help")){
            help(guildMessageReceivedEvent);
        } else {
            String author = guildMessageReceivedEvent.getAuthor().getAsMention();
            String error = "Not a command.";
            String msg = String.format("%s %s", error, author);
            guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
        }
    }

    private void add(String arguments, GuildMessageReceivedEvent guildMessageReceivedEvent){
        final Pattern pattern = Pattern.compile("n=\"(.+)\"\\s+d=\"(.+#\\d+)\"\\s+c=\"(.+)\"\\s+s=\"(.+)\"");
        Matcher matcher = pattern.matcher(arguments);
        String author = guildMessageReceivedEvent.getAuthor().getAsMention();

        if(matcher.matches()){
            String name = matcher.group(1);
            String discordTag = matcher.group(2);
            String className = matcher.group(3);
            String spec = matcher.group(4);

            long discordId = Objects.requireNonNull(jda.getUserByTag(discordTag)).getIdLong();

            if(name == null || name.length() > 12){
                String error = "Null or too long character name.";
                String msg = String.format("%s %s", error, author);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                return;
            } if (className == null || className.length() > 20){
                String error = "Null or too long class name.";
                String msg = String.format("%s %s", error, author);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                return;
            } if (spec == null || spec.length() > 32){
                String error = "Null or too long spec name.";
                String msg = String.format("%s %s", error, author);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                return;
            }

            Action action = new ActionCharacterAdd(name, discordId, className, spec);
            if(!offer(action)){
                String error = "Something went wrong. (Queue)";
                String msg = String.format("%s, %s", error, author);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
            }

        } else {
            String error = "Something went wrong.";
            String msg = String.format("%s %s", error, author);
            guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
        }

    }

    private void remove(String arguments, GuildMessageReceivedEvent guildMessageReceivedEvent){
        final Pattern pattern = Pattern.compile("n=\"(.+)\"");
        Matcher matcher = pattern.matcher(arguments);
        String author = guildMessageReceivedEvent.getAuthor().getAsMention();

        if(matcher.matches()){
            String name = matcher.group(1);
            if(name == null)
                return;

            Action action = new ActionCharacterRemove(name);
            if(!offer(action)){
                String error = "Something went wrong. (Queue)";
                String msg = String.format("%s, %s", error, author);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
            }

        } else {
            String error = "Something went wrong.";
            String msg = String.format("%s %s", error, author);
            guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
        }
    }

    private void update(String arguments, GuildMessageReceivedEvent guildMessageReceivedEvent){
        final Pattern pattern = Pattern.compile("n=\"(.+)\"\\s+c=\"(.+)\"\\s+s=\"(.+)\"");
        Matcher matcher = pattern.matcher(arguments);
        String author = guildMessageReceivedEvent.getAuthor().getAsMention();

        if(matcher.matches()){
            String name = matcher.group(1);
            String className = matcher.group(2);
            String spec = matcher.group(3);

            if(name == null || className == null || spec == null)
                return;

            Action action = new ActionCharacterUpdate(name, className, spec);
            if(!offer(action)){
                String error = "Something went wrong. (Queue)";
                String msg = String.format("%s, %s", error, author);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
            }

        } else {
            String error = "Something went wrong.";
            String msg = String.format("%s %s", error, author);
            guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
        }
    }

    private void help(GuildMessageReceivedEvent guildMessageReceivedEvent){
        String author = guildMessageReceivedEvent.getAuthor().getAsMention();
        char prefix = guildMessageReceivedEvent.getMessage().getContentRaw().charAt(0);
        String charAdd = prefix + "char add n=\"character name\" d=\"discord tag\" c=\"class\" s=\"spec\"" ;
        String charRemove = prefix + "char remove n=\"character name\"";
        String charUpdate = prefix + "char update n=\"character name\" c=\"class\" s=\"spec\"";
        String helpMessage = String.format(  "%s\n```%s\n%s\n%s```", author, charAdd, charRemove, charUpdate);
        guildMessageReceivedEvent.getChannel().sendMessage(helpMessage).queue();
    }

    private boolean offer(Action action) {
        return actionQueue.offer(action);
    }
}
