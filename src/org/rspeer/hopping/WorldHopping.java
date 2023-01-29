package org.rspeer.hopping;

import org.rspeer.AstScouter;
import org.rspeer.gui.Settings;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.WorldHopper;
import org.rspeer.runetek.providers.RSWorld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorldHopping {
    private static final List<Integer> notAllowedWorlds = new ArrayList<>();

    public static void worldHop() {
        RSWorld allowedWorlds = Worlds.get(x ->
                Settings.worldRange.contains(x.getId()) &&
                        !x.isBounty() &&
                        !x.isDeadman() &&
                        !x.isHighRisk() &&
                        !x.isPVP() &&
                        !x.isSkillTotal() &&
                        !x.isTournament() &&
                        !x.isLastManStanding() &&
                        !x.isSeasonDeadman());

        RSWorld[] invalidWorlds = Worlds.getLoaded(x ->
                x.isBounty() ||
                        x.isDeadman() ||
                        x.isHighRisk() ||
                        x.isPVP() ||
                        x.isSkillTotal() ||
                        x.isTournament() ||
                        x.isLastManStanding() ||
                        x.isSeasonDeadman());


        if (!WorldHopper.isOpen()) {
            WorldHopper.open();
            return;
        }
        if (Game.getState() == 30) {

            if (Settings.worldRange.isEmpty()) {
//                Log.fine("Reloading world list");
                Settings.worldRange = IntStream.rangeClosed(Settings.worldStart, Settings.worldEnd).boxed().collect(Collectors.toList());
                AstScouter.addedInvalidWorlds = false;
            }

            if (!AstScouter.addedInvalidWorlds) {
                for (RSWorld invalidWorld : invalidWorlds) {
//                    Log.fine("Adding not allowed world to list: " + world.getId());
                    notAllowedWorlds.add(invalidWorld.getId());
                }
                AstScouter.addedInvalidWorlds = true;
            } else {

                for (Iterator<Integer> it = notAllowedWorlds.iterator(); it.hasNext(); ) {
                    Integer nextWorld = it.next();
                    if (Settings.worldRange.contains(nextWorld)) {
                        Settings.worldRange.removeIf(l -> l.equals(nextWorld));
                        it.remove();
//                        Log.fine("Not Allowed - Removed World: " + hehe);
                    }
                }
            }



            if (WorldHopper.hopTo(allowedWorlds)) {
                if (Time.sleepUntil(() -> Settings.worldRange.contains(Worlds.getCurrent()), 20000)) {
//                    Log.fine("Removed World: " + Worlds.getCurrent());
                    Settings.worldRange.removeIf(l -> l == Worlds.getCurrent());
                }
            }
        }

//        Log.fine("World hopping to: " + Settings.worldRange.get(0));
//        if (WorldHopper.hopTo(allowedWorlds)) {
//            if (Time.sleepUntil(() -> Settings.worldRange.contains(Worlds.getCurrent()), 20000)) {
//                Settings.worldRange.removeIf(l -> l == Worlds.getCurrent());
//                return true;
//            }
//            return false;
//        }
//
//            Log.fine("Removing World: " + Settings.worldRange.get(0));
//            Settings.worldRange.remove(0);
//
//        return false;
    }

}
