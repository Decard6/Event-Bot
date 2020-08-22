package jda;

import jda.commands.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGE_REACTIONS;

public class Main {

    public static void main(String[] args) throws LoginException {
        String token = "NjUzMjg1NTc5NjMwMzEzNDgz.Xe0xmw.CFdmrOeENoxonJIaO-NIy9yaSMg";
        JDA jda = JDABuilder.create(token, GUILD_MESSAGES, GUILD_MESSAGE_REACTIONS).build();
        jda.addEventListener(new CommandListener(jda));
    }
}
