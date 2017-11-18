package com.example.android.newsapp.models;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by jennifernghinguyen on 1/17/17.
 */

public class News {
    private String headLine;
    private String section;
    private Date publishedDate;
    private String trailText;
    private String webUrl;
    private String contributor;
    private Bitmap thumbnail;

    public News(String headLine, String section, Date publishedDate, String trailText, String webUrl, String contributor, Bitmap thumbnail) {
        this.headLine = headLine;
        this.section = section;
        this.publishedDate = publishedDate;
        this.trailText = trailText;
        this.webUrl = webUrl;
        this.contributor = contributor;
        this.thumbnail = thumbnail;
    }

    public News(String headLine, String section, Date publishedDate, String trailText, String webUrl, String contributor) {
        this.headLine = headLine;
        this.section = section;
        this.publishedDate = publishedDate;
        this.trailText = trailText;
        this.webUrl = webUrl;
        this.contributor = contributor;
        this.thumbnail = null;
    }

    public String getHeadLine() {
        return this.headLine;
    }

    public String getSection() {
        return this.section;
    }

    public Date getPublishedDate() {
        return this.publishedDate;
    }

    public String getTrailText() {
        return this.trailText;
    }

    public String getWebUrl() {
        return this.webUrl;
    }

    public String getContributor() {
        return this.contributor;
    }

    public Bitmap getThumbnail() {
        return this.thumbnail;
    }
}
