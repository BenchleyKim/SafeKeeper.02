package com.example.safekeeper;

public class CctvMarker {
    private String cctv_call;
    private String cctv_settingdate;
    private String cctv_title;
    private double cctv_Lat;
    private double cctv_Lng;

    public CctvMarker(String cctv_call, String cctv_settingdate, String cctv_title, double cctv_Lat, double cctv_Lng) {
        this.cctv_call = cctv_call;
        this.cctv_settingdate = cctv_settingdate;
        this.cctv_title = cctv_title;
        this.cctv_Lat = cctv_Lat;
        this.cctv_Lng = cctv_Lng;
    }


    public CctvMarker(){}

    public String getCctv_call() {
        return cctv_call;
    }

    public void setCctv_call(String cctv_call) {
        this.cctv_call = cctv_call;
    }

    public String getCctv_settingdate() {
        return cctv_settingdate;
    }

    public void setCctv_settingdate(String cctv_settingdate) {
        this.cctv_settingdate = cctv_settingdate;
    }

    public String getCctv_title() {
        return cctv_title;
    }

    public void setCctv_title(String cctv_title) {
        this.cctv_title = cctv_title;
    }

    public double getCctv_Lat() {
        return cctv_Lat;
    }

    public void setCctv_Lat(double cctv_Lat) {
        this.cctv_Lat = cctv_Lat;
    }

    public double getCctv_Lng() {
        return cctv_Lng;
    }

    public void setCctv_Lng(double cctv_Lng) {
        this.cctv_Lng = cctv_Lng;
    }
}
