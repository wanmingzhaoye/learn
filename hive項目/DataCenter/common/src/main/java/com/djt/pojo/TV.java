package com.djt.pojo;

import java.util.ArrayList;

/**
 * @author dajiangtai
 * @create 2020-04-09-16:05
 */
public class TV {
    private String cardNum;
    private String stbNum;
    private String date;
    private String pageWidgetVersion;
    private ArrayList<UserLog> list;


    public ArrayList<UserLog> getList() {
        return list;
    }

    public void setList(ArrayList<UserLog> list) {
        this.list = list;
    }

    public String getStbNum() {
        return stbNum;
    }

    public void setStbNum(String stbNum) {
        this.stbNum = stbNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPageWidgetVersion() {
        return pageWidgetVersion;
    }

    public void setPageWidgetVersion(String pageWidgetVersion) {
        this.pageWidgetVersion = pageWidgetVersion;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }
}
