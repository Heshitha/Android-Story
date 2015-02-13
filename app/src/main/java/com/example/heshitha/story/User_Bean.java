package com.example.heshitha.story;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Heshitha on 2/6/2015.
 */
public class User_Bean {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private Date createdDate;
    private Date modifiedDate;
    private String imageName;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    private Bitmap image;
    private int status;

    public User_Bean(){
        this.id = 0;
        this.name = "";
        this.email = "";
        this.phoneNumber = "";
        this.password = "";
        this.createdDate = null;
        this.modifiedDate = null;
        this.status = 0;
    }

    public User_Bean(int id, String name, String email, String phoneNumber, String password, Date createdDate, Date modifiedDate, int status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
