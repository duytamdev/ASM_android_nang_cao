package com.tamstudio.asm_duytam.model;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String description;
    private String resImg;
    private String linkNews;

    public News(String title, String description, String resImg, String linkNews) {
        this.title = title;
        this.description = description;
        this.resImg = resImg;
        this.linkNews = linkNews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResImg() {
        return resImg;
    }

    public void setResImg(String resImg) {
        this.resImg = resImg;
    }

    public String getLinkNews() {
        return linkNews;
    }

    public void setLinkNews(String linkNews) {
        this.linkNews = linkNews;
    }
}
