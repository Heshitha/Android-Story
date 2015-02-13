package com.example.heshitha.story;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.Date;

/**
 * Created by Heshitha on 1/28/2015.
 */
public class Story_Bean {

    private int storyID;
    private Story_Category_Bean category;
    private Author_Bean author;
    private String title;
    private String shortStory;
    private String language;
    private double rate;
    private Date createdDate;
    private Date modifiedDate;
    private int status;
    private Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Story_Bean() {
        this.storyID = 0;
        this.category = new Story_Category_Bean();
        this.author = new Author_Bean();
        this.title = "";
        this.shortStory = "";
        this.language = "en";
        this.rate = 0;
        this.createdDate = null;
        this.modifiedDate = null;
        this.status = 0;
    }

    public Story_Bean(int storyID, Story_Category_Bean category, Author_Bean author, String title, String shortStory, String language, double rate, Date createdDate, Date modifiedDate, int status) {
        this.storyID = storyID;
        this.category = category;
        this.author = author;
        this.title = title;
        this.shortStory = shortStory;
        this.language = language;
        this.rate = rate;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.status = status;
    }

    public int getStoryID() {
        return storyID;
    }

    public void setStoryID(int storyID) {
        this.storyID = storyID;
    }

    public Story_Category_Bean getCategory() {
        return category;
    }

    public void setCategory(Story_Category_Bean category) {
        this.category = category;
    }

    public Author_Bean getAuthor() {
        return author;
    }

    public void setAuthor(Author_Bean author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortStory() {
        return shortStory;
    }

    public void setShortStory(String shortStory) {
        this.shortStory = shortStory;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
