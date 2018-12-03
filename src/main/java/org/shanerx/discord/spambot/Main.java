package org.shanerx.discord.spambot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Main extends ListenerAdapter {
	
	public static void main(String[] args) throws IOException, LoginException, RateLimitedException, ParseException {
		File config = new File("spambot_conf.json");
		if (!config.exists()) {
			config.createNewFile();
			PrintWriter pw = new PrintWriter(config);
			pw.println("{");
			pw.println("    \"token\" : \"insert-token\",");
			pw.println("}");
			pw.close();
			return;
		}
		JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(config));
		JDA api = new JDABuilder(AccountType.CLIENT).setToken((String) obj.get("token")).setAutoReconnect(true).addEventListener(new Main()).buildAsync();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String[] msg = event.getMessage().getContent().split(" ");
		MessageChannel mc = event.getChannel();
		User user = event.getAuthor();
		
		if (!msg[0].startsWith("/spam") || msg.length < 3) {
			return;
		} else if (user.getIdLong() != 108989469687840768L) {
			return;
		}
		
		try {
			int count = Integer.parseInt(msg[1]);
			String message = event.getMessage().getContent().replace(msg[0] + " ", "").replace(msg[1] + " ", "");
			for (int i = 0; i < count; i++) {
				mc.sendMessage(message).queue();
			}
		} catch (NumberFormatException e) {
			return;
		}
	}
}