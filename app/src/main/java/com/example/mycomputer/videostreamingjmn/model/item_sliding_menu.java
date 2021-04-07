package com.example.mycomputer.videostreamingjmn.model;

/**
 * Created by My Computer on 7/16/2016.
 */
public class item_sliding_menu {

    int imgid;
    String title;

    public item_sliding_menu(int image, String title) {
        imgid = image;
        this.title = title;
    }

    public item_sliding_menu(String title) {
        this.title = title;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgid() {
        return imgid;
    }

    public String getTitle() {
        return title;
    }
}
