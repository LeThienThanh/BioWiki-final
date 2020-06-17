package com.biowiki.biowiki;

public class UserShow {
    String name, image, email,createdAt,dec;
    Long Biocoin;


    public UserShow(String name, String image, String email, String createdAt, String dec, Long Biocoin) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.createdAt = createdAt;
        this.dec = dec;
        this.Biocoin = Biocoin;
    }

    public UserShow() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getBiocoin() {
        return Biocoin;
    }

    public void setBiooin(Long Biocoin) {
        this.Biocoin = Biocoin;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreateAT(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }
}
