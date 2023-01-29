package org.rspeer;

import org.rspeer.bot.Commands;
import org.rspeer.gui.GUI;
import org.rspeer.hopping.WorldHopping;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.target.TargetHandler;
import org.rspeer.target.TargetInfo;

import java.awt.*;

@ScriptMeta(name = "AstScouter", developer = "Asta", desc = "Scans nearby players wealth", version = 1.05)
public class AstScouter extends Script implements RenderListener {

    public static boolean start, ready;
    public static boolean addedInvalidWorlds;
    private final StopWatch stopWatch = StopWatch.start();
    private GUI gui;

    @Override
    public void onStart() {
        gui = new GUI();
        gui.loadSettings();
        gui.setVisible(true);
    }

    @Override
    public void onStop() {
        gui.setVisible(false);
        GUI.jda.shutdown();
    }


    @Override
    public int loop() {

        if (start && ready) {
            TargetHandler.getTargetWealth();
            WorldHopping.worldHop();
        }
        return 1000;
    }

    @Override
    public void notify(RenderEvent renderEvent) {
        Graphics g = renderEvent.getSource();

        Graphics2D g2 =(Graphics2D) g;

        Color mainColor = new Color(0, 150, 255);

        g.setColor(mainColor);
        g2.setStroke(new BasicStroke(3));
        g.drawRect(5, 200, 200, 120);
        g.setColor(new Color(50, 50, 50, 200));
        g.fillRect(5, 200, 200, 120);

        g.setColor(Color.WHITE);

        g.drawString("Version: " + getMeta().version(), 10, 215);
        g.drawString("Time Ran: " + stopWatch.toElapsedString(), 10, 230);
        g.drawString("Total Scouted: " + TargetHandler.scoutCounter, 10, 245);
        g.setColor(mainColor);
        g2.setStroke(new BasicStroke(2));
        g.drawLine(5, 253, 205, 253);
        g.setColor(Color.WHITE);
        g.drawString("Last Scouted RSN: " + TargetInfo.rsn, 10, 270);
        g.drawString("Last Scouted WORLD: " + TargetInfo.world, 10, 285);
        g.drawString("Last Scouted WORTH: " + Commands.withSuffix(TargetInfo.worth), 10, 300);
        g.drawString("Last Scouted LEECH: " + String.valueOf(TargetInfo.isLeech).toUpperCase(), 10, 315);
    }
}
