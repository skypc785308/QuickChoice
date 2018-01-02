package com.mis.ncyu.quickchoice;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by UserMe on 2017/12/10.
 */

public class StoreInfo {
    @SerializedName("used_card_name")
    private String StoreName;
    @SerializedName("used_card_detail")
    private String StoreDetial;
    @SerializedName("end_date")
    private String EndDate;

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getStoreDetial() {
        return StoreDetial;
    }

    public void setStoreDetial(String storeDetial) {
        StoreDetial = storeDetial;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }
}
