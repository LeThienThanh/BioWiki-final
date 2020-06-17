package com.biowiki.biowiki.models;

import java.io.Serializable;

public class Model implements Serializable {
    private String name, image, gltf;

    public Model() {}

    public Model(String name, String image, String gltf) {
        this.name = name;
        this.image = image;
        this.gltf = gltf;
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

    public String getGltf() {
        return gltf;
    }

    public void setGltf(String gltf) {
        this.gltf = gltf;
    }
}
