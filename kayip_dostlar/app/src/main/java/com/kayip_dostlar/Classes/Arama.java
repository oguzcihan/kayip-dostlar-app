package com.kayip_dostlar.Classes;

public class Arama {
    private String imageurl;
    private String isim;
    private String baslik;

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public Arama(){

    }

    public Arama(String imageurl) {
        this.imageurl = imageurl;

    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }
}
