package com.mis.ncyu.quickchoice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by UserMe on 2017/12/17.
 */

public class ShowHistory {

    @SerializedName("card_id")
    private String CardName;
    @SerializedName("total")
    private int TotalCost = 0;
    @SerializedName("max_cost")
    private int CostLimit = 0;
    @SerializedName("red")
    private int RedGet = 0;

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public int getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(int totalCost) {
        TotalCost = totalCost;
    }

    public int getCostLimit() {
        return CostLimit;
    }

    public void setCostLimit(int costLimit) {
        CostLimit = costLimit;
    }

    public int getRedGet() {
        return RedGet;
    }

    public void setRedGet(int redGet) {
        RedGet = redGet;
    }
}
