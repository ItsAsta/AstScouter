package org.rspeer.price;

import com.google.gson.annotations.SerializedName;

public class ItemPrice {

    public String name;
    public boolean members;
    @SerializedName("buy_average")
    public int buyAverage;
    @SerializedName("sell_average")
    public int sellAverage;
    @SerializedName("overall_average")
    public int overallAverage;

    public String getName() {
        return name;
    }

    public boolean isMembers() {
        return members;
    }

    public int getBuyAverage() {
        return buyAverage;
    }

    public int getSellAverage() {
        return sellAverage;
    }

    public int getOverallAverage() {
        return overallAverage;
    }
}
