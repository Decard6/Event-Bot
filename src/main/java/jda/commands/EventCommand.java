package jda.commands;

import action.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.MyDateParser;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventCommand implements Command{
    private JDA jda;
    private Queue<Action> actionQueue;

    EventCommand(JDA jda, Queue<Action> actionQueue){
        this.jda = jda;
        this.actionQueue = actionQueue;
    }

    @Override
    public void execute(String string, GuildMessageReceivedEvent guildMessageReceivedEvent){
        String addOrHelp = "";
        String removeOrUpdate = "";
        String confirm = "";
        String confirmall = "";

        if(string.length() >= 4)
            addOrHelp = string.substring(0, 4);
        if(string.length() >= 7)
            removeOrUpdate = string.substring(0, 7);
        if(string.length() >= 8)
            confirm = string.substring(0, 8);
        if(string.length() >= 11)
            confirmall = string.substring(0, 11);

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
        } else if(confirm.equalsIgnoreCase("confirm ")){
            String arguments = string.substring(8);
            confirm(arguments, guildMessageReceivedEvent);
        } else if(confirmall.equalsIgnoreCase("confirmall ")){
            String arguments = string.substring(11);
            confirmAll(arguments, guildMessageReceivedEvent);
        } else {
            String author = guildMessageReceivedEvent.getAuthor().getAsMention();
            String error = "Not a command.";
            String msg = String.format("%s %s", error, author);
            guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
        }
    }

    private void add(String commandString, GuildMessageReceivedEvent guildMessageReceivedEvent){
        final Pattern pattern = Pattern.compile("n=\"(.+)\"\\s+d=\"(\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d)\"\\s+c=\"(.+)\"");
        Matcher matcher = pattern.matcher(commandString);
        String author = guildMessageReceivedEvent.getAuthor().getAsMention();

        if(matcher.matches()){
            String eventName = matcher.group(1);
            String dateString = matcher.group(2);
            String channelName = matcher.group(3);

            Date date;
            try {
                date = MyDateParser.parse(dateString);
            } catch (ParseException e) {
                String error = "Wrong date format.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                return;
            }

            List<TextChannel> textChannels = jda.getTextChannelsByName(channelName, true);
            Long channelId = null;

            if(!textChannels.isEmpty()){
                channelId = textChannels.get(0).getIdLong();
            }

            if(eventName == null || eventName.length() > 64){
                String error = "Null or too long event name.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
            } else if(channelId == null){
                String error = "Invalid Channel.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
            } else {
                Action action = new ActionEventAdd(eventName, date, channelId);
                if(!offer(action)){
                    String error = "Something went wrong. (Queue)";
                    String msg = String.format("%s, %s", error, author);
                    guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                }
            }
        } else {
            String error = "Something went wrong.";
            String msg = String.format("%s %s", error, author);
            guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
        }
    }

    private void remove(String commandString, GuildMessageReceivedEvent guildMessageReceivedEvent){
        final Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(commandString);
        String author = guildMessageReceivedEvent.getAuthor().getAsMention();

        if(matcher.matches()){
            String eventIdString = matcher.group(1);

            long eventId;
            try{
                eventId = Long.parseLong(eventIdString);
            } catch (NumberFormatException e) {
                String error = "Failed to parse event ID.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                return;
            }

            Action action = new ActionEventRemove(eventId);
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

    private void update(String commandString, GuildMessageReceivedEvent guildMessageReceivedEvent){
        final Pattern pattern = Pattern.compile("(\\d+)\\s+n=\"(.+)\"\\s+d=\"(\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d)\"");
        Matcher matcher = pattern.matcher(commandString);
        String author = guildMessageReceivedEvent.getAuthor().getAsMention();

        if(matcher.matches()){
            String eventIdString = matcher.group(1);
            String eventName = matcher.group(2);
            String dateString = matcher.group(3);

            long eventId;
            try{
                eventId = Long.parseLong(eventIdString);
            } catch (NumberFormatException e) {
                String error = "Failed to parse event ID.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                return;
            }

            Date date;
            try {
                date = MyDateParser.parse(dateString);
            } catch (ParseException e) {
                String error = "Wrong date format.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                return;
            }

            if(eventName == null || eventName.length() > 64){
                String error = "Null or too long event name.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
            } else {
                Action action = new ActionEventUpdate(eventId, eventName, date);
                if(!offer(action)){
                    String error = "Something went wrong. (Queue)";
                    String msg = String.format("%s, %s", error, author);
                    guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                }
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
        String eventAdd = prefix + "event add n=\"event name\" d=\"dd/mm/yyyy hh:mm\" c=\"channel name\"";
        String eventRemove = prefix + "event remove id";
        String eventUpdate = prefix + "event update id n=\"event name\" d=\"dd/mm/yyyy hh:mm\"";
        String confirm = prefix + "event confirm id n=\"char name\" status";
        String confirmAll = prefix + "event confirmall id";
        String helpMessage = String.format(  "%s\n```%s\n%s\n%s\n%s\n%s```", author, eventAdd, eventRemove, eventUpdate, confirm, confirmAll);
        guildMessageReceivedEvent.getChannel().sendMessage(helpMessage).queue();
    }

    private void confirm(String commandString, GuildMessageReceivedEvent guildMessageReceivedEvent){
        String author = guildMessageReceivedEvent.getAuthor().getAsMention();
        final Pattern pattern = Pattern.compile("(\\d+)\\s+n=\"(.+)\"\\s+(.+)");
        Matcher matcher = pattern.matcher(commandString);
        if(matcher.matches()) {
            String eventIdString = matcher.group(1);
            String characterName = matcher.group(2);
            String statusString = matcher.group(3);

            long eventId;
            try{
                eventId = Long.parseLong(eventIdString);
            } catch (NumberFormatException e) {
                String error = "Failed to parse event ID.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                return;
            }

            if(characterName == null){
                String error = "Null character name.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
            }

            byte status;
            if(statusString.equalsIgnoreCase("confirmed"))
                status = 1;
            else if(statusString.equalsIgnoreCase("standby"))
                status = 2;
            else if(statusString.equalsIgnoreCase("out"))
                status = 3;
            else{
                String error = "Wrong status.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                return;
            }

            Action action = new ActionConfirm(eventId, characterName, status);
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

    private void confirmAll(String commandString, GuildMessageReceivedEvent guildMessageReceivedEvent){
        final Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(commandString);
        String author = guildMessageReceivedEvent.getAuthor().getAsMention();
        if(matcher.matches()) {
            String eventIdString = matcher.group(1);

            long eventId;
            try{
                eventId = Long.parseLong(eventIdString);
            } catch (NumberFormatException e) {
                String error = "Failed to parse event ID.";
                String msg = String.format("%s %s", author, error);
                guildMessageReceivedEvent.getChannel().sendMessage(msg).queue();
                return;
            }

            Action action = new ActionConfirmAll(eventId);
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

    private boolean offer(Action action) {
        return actionQueue.offer(action);
    }
}
