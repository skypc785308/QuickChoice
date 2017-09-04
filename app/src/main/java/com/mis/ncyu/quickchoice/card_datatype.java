package com.mis.ncyu.quickchoice;

import android.util.Log;

/**
 * Created by UserMe on 2017/7/31.
 */

public class card_datatype {

    private String no;
    private String bank;
    private String name;
    private String key;
    private Double value;
    private String store;

    public card_datatype(String bank,String name,String key,Double value) {
        this.bank = bank;
        this.name = name;
        this.key = key;
        this.value = value;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getStore() {
        return store;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getno(){
        return no;
    }
    public void setno(String no){
        this.no = no;
    }
    public String getname(){
        return name;
    }
    public void setname(String name){
        this.name = name;
    }
    public String getkey(){
        return key;
    }
    public void setKey(String time){
        this.key = time;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
