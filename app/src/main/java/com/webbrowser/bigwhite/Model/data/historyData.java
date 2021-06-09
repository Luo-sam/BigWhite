package com.webbrowser.bigwhite.Model.data;

public class historyData {
    /*网页的网址*/
    private String address;
    /*网页名*/
    private String name;
    /*构造函数*/
    public historyData(String name, String address){
        this.name = name;
        this.address = address;
    }

    /*getter和setter方法*/
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

}
