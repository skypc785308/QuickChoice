package com.mis.ncyu.quickchoice;

import android.util.Log;

/**
 * Created by UserMe on 2017/7/31.
 */

public class card_datatype {

    private String no;
    private String name;
    private String key;
    private Integer value;

    public card_datatype(String no,String name,String key,Integer value) {
        this.no = no;
        this.name = name;
        this.key = key;
        this.value = value;
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

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
