package com.biowiki.biowiki;

import java.io.Serializable;

public class User implements Serializable {
    private String Name;
    private String Email;
    private String Dec;
    private String Image;
    private String id;
    private long Biocoin;

    public User(){}

    public User(String name, String email, String dec, String image, String id, long Biocoin) {
        this.Name = name;
        this.Email = email;
        this.Dec = dec;
        this.Image = image;
        this.id = id;
        this.Biocoin = Biocoin;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getDec() {
        return Dec;
    }

    public String getImage() {
        return Image;
    }

    public String getId() {
        return id;
    }

    public long getCoin() { return Biocoin; }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setDec(String dec) {
        Dec = dec;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCoin(long Biocoin) {
        this.Biocoin = Biocoin;
    }

}
