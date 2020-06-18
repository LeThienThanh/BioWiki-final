package com.biowiki.biowiki.Models;

import java.io.Serializable;
import java.util.Comparator;

public class Animal implements Serializable, Comparator<Animal> {
    private String name, dec, image, source, userName, userId, animalId;
    private long vote;

    public Animal (String animalId, String name, String dec, String image, String source, String userName, String userId, long vote) {
        this.animalId = animalId;
        this.name = name;
        this.dec = dec;
        this.image = image;
        this.source = source;
        this.userName = userName;
        this.userId = userId;
        this.vote = vote;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) { this.image = image; }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Animal () {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getVote() {
        return vote;
    }

    public void setVote(long vote) {
        this.vote = vote;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    @Override
    public int compare(Animal o1, Animal o2) {
        return (int) (o2.getVote() - o1.getVote());
    }
}
