package com.kayip_dostlar.Classes;

public class Veri {
    private String imageurl;
    private String isim;

    public String getBulundumu() {
        return bulundumu;
    }

    public void setBulundumu(String bulundumu) {
        this.bulundumu = bulundumu;
    }

    private String bulundumu;


    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public Veri(){

    }

    public Veri(String imageurl) {
        this.imageurl = imageurl;

    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
