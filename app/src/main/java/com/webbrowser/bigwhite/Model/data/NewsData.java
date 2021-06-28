package com.webbrowser.bigwhite.Model.data;

import java.util.List;

public class NewsData {
    /*网页的网址*/
    private String address;
    /*网页标题*/
    private String title;
    /*作者*/
    private String author;
    /*网页内容*/
    private List<String> contents;
    /*网页内容对应的图片，没有则为null*/
    private List<String> images;

    public NewsData(String address, String title, String author, List<String> contents, List<String> images) {
        this.address = address;
        this.title = title;
        this.author = author;
        this.contents = contents;
        this.images = images;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}
