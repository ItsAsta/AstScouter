package org.rspeer.price;

import com.acuitybotting.common.utils.ExecutorUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.rspeer.ui.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PriceCheck {

    private static Gson g = new Gson();
    private static Map<String, Integer> itemNameMapping = new HashMap<>();
    private static Map<Integer, ItemPrice> prices = new HashMap<>();
    private static int reloadMinutes = 30;
    private static boolean isReloadEnabled = true;

    private static ScheduledThreadPoolExecutor executor = ExecutorUtil.newScheduledExecutorPool(1, Throwable::printStackTrace);
    private static ScheduledFuture<?> task;

    public static ItemPrice getPrice(String name) {
        if(prices.size() == 0) {
            reload();
        }
        int id = itemNameMapping.getOrDefault(name.toLowerCase(), -1);
        return id == -1 ? null : getPrice(id);
    }

    public static ItemPrice getPrice(int id) {
        if(prices.size() == 0) {
            reload();
        }
        return prices.getOrDefault(id, null);
    }

    public static void reload() {
        if(!isReloadEnabled && prices.size() > 0) {
            return;
        }
        if(task == null && isReloadEnabled) {
            task = executor.scheduleAtFixedRate(PriceCheck::reload, reloadMinutes, reloadMinutes, TimeUnit.MINUTES);
        }
        try {
            HttpResponse<String> node = Unirest.get("https://storage.googleapis.com/osb-exchange/summary.json").asString();
            if(node.getStatus() != 200) {
                System.out.println(node.getBody());
                Log.severe("PriceCheck", "Failed to load prices. Result: " + node.getBody());
                return;
            }
            JsonObject o = g.fromJson(node.getBody(), JsonObject.class);
            for (String s : o.keySet()) {
                ItemPrice price = g.fromJson(o.get(s).getAsJsonObject(), ItemPrice.class);
                int id = Integer.parseInt(s);
                String name = price.name.toLowerCase();
                itemNameMapping.remove(name);
                itemNameMapping.put(name, id);
                prices.remove(id);
                prices.put(id, price);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            Log.severe(e);
        }
    }

    public static void dispose() {
        task.cancel(true);
        executor.shutdown();
    }

    public static void setShouldReload(boolean value) {
        isReloadEnabled = value;
    }
}
