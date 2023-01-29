package org.rspeer.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.rspeer.gui.Settings;
import org.rspeer.io.LeechFile;
import org.rspeer.script.Script;
import org.rspeer.target.TargetHandler;
import org.rspeer.target.TargetInfo;
import org.rspeer.ui.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Commands extends ListenerAdapter {

	private static final String COMMANDS =
			"``` \n" +
					"!addleech USAGE: -leech RSN \n" +
					"!removeleech USAGE: !remove RSN" +
					"!list" +
					"```";

	public static String withSuffix(long count) {
		if (count < 1000) return "" + count;
		int exp = (int) (Math.log(count) / Math.log(1000));
		return String.format("%.2f%c",
				count / Math.pow(1000, exp),
				"kMBTPE".charAt(exp - 1));
	}

	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		String[] args = e.getMessage().getContentRaw().split("\\s+", 2);

		if (args[0].equalsIgnoreCase(Settings.prefix + "help")) {
			e.getChannel().sendTyping().queue();
			e.getChannel().sendMessage(COMMANDS).queue();
		}

		if (args[0].equalsIgnoreCase(Settings.prefix + "test")) {
			EmbedBuilder info = new EmbedBuilder();
			info.addField("RSN", TargetInfo.rsn, true);
			info.addField("World", String.valueOf(TargetInfo.world), true);
			info.addField("Combat", String.valueOf(TargetInfo.combat), true);
			info.addField("Worth", String.valueOf(withSuffix(TargetInfo.worth)), true);
			info.addField("Leech", String.valueOf(TargetInfo.isLeech), true);
			info.addBlankField(false);
			info.addField("Head", TargetInfo.head, true);
			info.addField("Cape", TargetInfo.cape, true);
			info.addField("Neck", TargetInfo.neck, true);
			info.addField("Weapon", TargetInfo.weapon, true);
			info.addField("Body", TargetInfo.body, true);
			info.addField("Shield", TargetInfo.shield, true);
			info.addField("Legs", TargetInfo.legs, true);
			info.addField("Gloves", TargetInfo.gloves, true);
			info.addField("Feet", TargetInfo.feet, true);
			info.setColor(Color.RED);
			e.getChannel().sendTyping().queue();
			e.getChannel().sendMessage(info.build()).queue();
			info.clear();
		}

		if (args[0].equalsIgnoreCase(Settings.prefix + "addleech")) {
			TargetHandler.leechSet.add(args[1]);
			EmbedBuilder info = new EmbedBuilder();
			info.setTitle("👓  Scouter  👓");
			info.addField("ADDED NEW LEECH (RSN)", args[1], false);

			try {
				LeechFile.writeLeech(args[1]);
			} catch (IOException ex) {
				Log.severe(ex);
			}

			info.setColor(Color.YELLOW);

			e.getChannel().sendTyping().queue();
			e.getChannel().sendMessage(info.build()).queue();
			info.clear();
		}

		if (args[0].equalsIgnoreCase(Settings.prefix + "removeleech")) {
			EmbedBuilder info = new EmbedBuilder();
			info.setTitle("👓  Scouter  👓");
			info.addField("REMOVED LEECH (RSN)", args[1], false);

			try {
				LeechFile.removeLeech(args[1]);
			} catch (IOException ex) {
				Log.severe(ex);
			}

			info.setColor(Color.RED);

			e.getChannel().sendTyping().queue();
			e.getChannel().sendMessage(info.build()).queue();
			info.clear();
		}

		if (args[0].equalsIgnoreCase(Settings.prefix + "list")) {
			EmbedBuilder info = new EmbedBuilder();
			info.setTitle("👓  Scouter  👓");
			info.addField("📜 Leech List (RSN)", "", false);
			for (String leech : TargetHandler.leechSet) {
				info.addField(leech, "", true);
			}

			info.setColor(Color.YELLOW);

			e.getChannel().sendTyping().queue();
			e.getChannel().sendMessage(info.build()).queue();
			info.clear();
		}

		if (args[0].equalsIgnoreCase(Settings.prefix + "i")) {
			EmbedBuilder info = new EmbedBuilder();

			info.setImage("https://www.w3schools.com/w3css/img_lights.jpg");
			BufferedImage bufferedImage = new BufferedImage(170, 30,
					BufferedImage.TYPE_INT_RGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawString("Image testing", 10, 25);
			try {
				ImageIO.write(bufferedImage, "jpg", new File(Script.getDataDirectory().toAbsolutePath() + "img"));
			} catch (IOException ex) {
				Log.severe(ex);
			}


			e.getChannel().sendMessage(info.build()).queue();
		}
	}
}
