package com.example.vasuchand.lbb_collagemaker;

/**
 * Created by Vasu Chand on 5/9/2017.
 */

public class Model {

    String Imageid,Imageurl;
    int height,width;
    int count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Model(String imageid, String imageurl, int height, int width, int count) {

        Imageid = imageid;
        Imageurl = imageurl;
        this.height = height;
        this.width = width;
        this.count = count;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getImageid() {

        return Imageid;
    }

    public void setImageid(String imageid) {
        Imageid = imageid;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }
}
