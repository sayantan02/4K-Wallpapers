package com.technicalsayan.a4kwallpapers.Models;

public class Model {
    String image, key, tag, source;
    int downloaded, popularity;

    Model() {

    }



    public Model(String image, int downloaded, String key, String tag, int popularity, String source) {
        this.image = image;
        this.downloaded = downloaded;
        this.key = key;
        this.tag = tag;
        this.popularity = popularity;
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

}
