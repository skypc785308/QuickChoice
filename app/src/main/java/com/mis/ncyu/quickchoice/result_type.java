package com.mis.ncyu.quickchoice;

/**
 * Created by UserMe on 2017/8/6.
 */

public class result_type {
    private int id;
    private String name;
    private String keyword;
    private Integer key;
    private Boolean chk = Boolean.FALSE;
    public result_type(){}
    public void compute_key(){
           this.key =Integer.parseInt(this.keyword.replace("元／點",""));
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

    public Integer getKey() {
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

    public void setKey(Integer key) {
        this.key = key;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        compute_key();
    }

    public void setName(String name) {

        this.name = name;
    }
}
