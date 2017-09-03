package com.mis.ncyu.quickchoice;

/**
 * Created by UserMe on 2017/8/6.
 */

public class result_type {
    private int id;
    private String name;
    private String keyword;
    private Double key;
    private Boolean chk = Boolean.FALSE;
    public result_type(){}
    public void compute_key(String replace){
           this.key =Double.parseDouble(this.keyword.replace(replace,""));
    }

    public Boolean getChk() {
        return chk;
    }

    public void setChk(Boolean chk) {
        this.chk = chk;
    }

    public int getId() {
        return id;
    }

    public Double getKey() {
        return key;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKey(Double key) {
        this.key = key;
    }

    public void setKeyword(String keyword ,String replace) {
        this.keyword = keyword;
        compute_key(replace);
    }

    public void setName(String name) {

        this.name = name;
    }
}
