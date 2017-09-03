package com.mis.ncyu.quickchoice;

/**
 * Created by UserMe on 2017/9/3.
 */

public class Total_data {

    private String card_name;
    private String card_bank;
    private String buy;
    private String red;
    private String cash,movie,oil_cash,card_offer,plane,store;
    public Total_data(String card_name,String card_bank,String buy,String red,String cash,String movie,
                      String oil_cash,String card_offer,String plane,String store){
        this.card_name = card_name;
        this.card_bank = card_bank;
        this.buy = buy;
        this.red = red;
        this.cash = cash;
        this.movie = movie;
        this.oil_cash = oil_cash;
        this.card_offer = card_offer;
        this.plane = plane;
        this.store = store;


    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getBuy() {
        return buy;
    }

    public String getCard_bank() {
        return card_bank;
    }

    public String getCard_name() {
        return card_name;
    }

    public String getCard_offer() {
        return card_offer;
    }

    public String getCash() {
        return cash;
    }

    public String getMovie() {
        return movie;
    }

    public String getOil_cash() {
        return oil_cash;
    }

    public String getPlane() {
        return plane;
    }

    public String getRed() {
        return red;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public void setCard_bank(String card_bank) {
        this.card_bank = card_bank;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public void setCard_offer(String card_offer) {
        this.card_offer = card_offer;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public void setOil_cash(String oil_cash) {
        this.oil_cash = oil_cash;
    }

    public void setPlane(String plane) {
        this.plane = plane;
    }

    public void setRed(String red) {
        this.red = red;
    }
}
