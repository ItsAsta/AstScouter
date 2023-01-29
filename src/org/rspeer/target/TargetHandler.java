package org.rspeer.target;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.rspeer.bot.Commands;
import org.rspeer.bot.Constants;
import org.rspeer.gui.GUI;
import org.rspeer.gui.Settings;
import org.rspeer.price.PriceCheck;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.Definitions;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.providers.RSItemDefinition;
import org.rspeer.ui.Log;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class TargetHandler {

    private static final Set<String> scannedTargetSet = new HashSet<>();
    public static Set<Integer> playersLoaded = new HashSet<>();
    public static Set<String> leechSet = new HashSet<>();
    public static int scoutCounter = 0;
    private static int totalWorth;

    public static void getTargetWealth() {
        Player[] targets = Players.getLoaded(x -> !scannedTargetSet.contains(x.getName().replace('\u00A0', ' ')));
//        Player[] targets = Players.getLoaded(x -> x.getName().replace('\u00A0', ' ').equalsIgnoreCase("z0rana eagle"));
        TextChannel channel = GUI.jda.getTextChannelById(Settings.channelId);
        playersLoaded.add(targets.length);

        if (channel == null) {
            Log.fine("Connecting to channel...");
            return;
        }

        for (Player target : targets) {

            if (target != null && Game.getState() == 30) {

                int[] equipId = target.getAppearance().getEquipmentIds();


                for (int id : equipId) {
                    RSItemDefinition def = Definitions.getItem(id - 512);

                    if (def != null && def.isTradable()) {
//                    Log.fine("Item: " + def.getName() + " Price: " + Commands.withSuffix(PriceCheck.getPrice(id - 512).buyAverage));
                        if (PriceCheck.getPrice(id - 512) != null) {
                            totalWorth = PriceCheck.getPrice(id - 512).buyAverage + totalWorth;
                        }
                    }
                }


                if (target.getName() != null) scannedTargetSet.add(target.getName().replace('\u00A0', ' '));

                for (int value : Settings.valueRange) {

                    if (totalWorth >= (value * 1_000_000)) {
                        scoutCounter += 1;

//                        Log.fine("Final Worth: " + totalWorth + " RSN: " + target.getName());

                        TargetInfo.head = "N/A";
                        TargetInfo.cape = "N/A";
                        TargetInfo.neck = "N/A";
                        TargetInfo.weapon = "N/A";
                        TargetInfo.body = "N/A";
                        TargetInfo.shield = "N/A";
                        TargetInfo.legs = "N/A";
                        TargetInfo.gloves = "N/A";
                        TargetInfo.feet = "N/A";
                        TargetInfo.isLeech = false;

                        if (target.getName() == null) {
                            return;
                        }

                        TargetInfo.rsn = target.getName().replace('\u00A0', ' ');
                        TargetInfo.world = Worlds.getCurrent();
                        TargetInfo.combat = target.getCombatLevel();
                        TargetInfo.worth = totalWorth;
                        TargetInfo.isLeech = leechSet.contains(TargetInfo.rsn);

                        RSItemDefinition head = Definitions.getItem(equipId[0] - 512);
                        if (head != null) TargetInfo.head = head.getName();

                        RSItemDefinition cape = Definitions.getItem(equipId[1] - 512);
                        if (cape != null) TargetInfo.cape = cape.getName();

                        RSItemDefinition neck = Definitions.getItem(equipId[2] - 512);
                        if (neck != null) TargetInfo.neck = neck.getName();

                        RSItemDefinition weapon = Definitions.getItem(equipId[3] - 512);
                        if (weapon != null) TargetInfo.weapon = weapon.getName();

                        RSItemDefinition body = Definitions.getItem(equipId[4] - 512);
                        if (body != null) TargetInfo.body = body.getName();

                        RSItemDefinition shield = Definitions.getItem(equipId[5] - 512);
                        if (shield != null) TargetInfo.shield = shield.getName();

                        RSItemDefinition legs = Definitions.getItem(equipId[7] - 512);
                        if (legs != null) TargetInfo.legs = legs.getName();

                        RSItemDefinition gloves = Definitions.getItem(equipId[9] - 512);
                        if (gloves != null) TargetInfo.gloves = gloves.getName();

                        RSItemDefinition feet = Definitions.getItem(equipId[10] - 512);
                        if (feet != null) TargetInfo.feet = feet.getName();

                        passInfo();
                    }
                    totalWorth = 0;
                }
            }
        }
        playersLoaded.clear();
    }

    private static void passInfo() {
        TextChannel channel = GUI.jda.getTextChannelById(Settings.channelId);
        EmbedBuilder info = new EmbedBuilder();

        if (channel == null) {
            return;
        }

//        Log.fine("Sending info to discord");

        if (TargetInfo.worth < Constants.M100) {
            info.setColor(Color.PINK);
        }

        if (TargetInfo.worth >= Constants.M100 && TargetInfo.worth <= Constants.M200) {
            info.setColor(Color.GREEN);
        }

        if (TargetInfo.worth > Constants.M200 && TargetInfo.worth <= Constants.M300) {
            info.setColor(Color.BLUE);
        }

        if (TargetInfo.worth > Constants.M300 && TargetInfo.worth <= Constants.M500) {
            info.setColor(Color.CYAN);
        }

        if (TargetInfo.worth > Constants.M500) {
            info.setColor(Color.MAGENTA);
        }

        info.setTitle("👓  SCOUTED  👓");

        info.addField("RSN", TargetInfo.rsn, true);
        info.addField("World", String.valueOf(TargetInfo.world), true);
        info.addField("Combat", String.valueOf(TargetInfo.combat), true);
        info.addField("Worth", String.valueOf(Commands.withSuffix(TargetInfo.worth)), true);
        info.addField("Leech", String.valueOf(TargetInfo.isLeech).toUpperCase(), true);
        info.addField("", "", true);
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
        channel.sendTyping().queue();
        channel.sendMessage(info.build()).queue();
        info.clear();
    }
}
