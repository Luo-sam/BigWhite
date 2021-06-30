package com.webbrowser.bigwhite.Model.data;

public class VideoData {
    private String videoUrl;
    private String title;
    private String author;


    public VideoData(String videoUrl, String title, String author) {
        this.videoUrl = videoUrl;
        this.title = title;
        this.author = author;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
