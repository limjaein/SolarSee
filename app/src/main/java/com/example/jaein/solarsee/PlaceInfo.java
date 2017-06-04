package com.example.jaein.solarsee;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by 조은미 on 2017-05-31.
 */

public class PlaceInfo {
    private LatLng p_latlng;
    private String p_placeName;
    public String p_sunRise;
    public String p_sunSet;
    public String p_rtemp, p_stemp;
    public String p_rcloud, p_scloud;
    public int p_x;
    public int p_y;

    public PlaceInfo(LatLng latlng, String placeName, int x, int y){
        this.p_latlng = latlng;
        this.p_placeName = placeName;
        p_sunRise = "";
        p_sunSet = "";
        this.p_x = x;
        this.p_y = y;
    }


    public void setSunInfo(String sunrise, String sunset){
        this.p_sunRise = sunrise;
        this.p_sunSet = sunset;
    }

    public void setWeatherInfo(String r_temper, String r_cloud, String s_temper, String s_cloud){
        this.p_rtemp = r_temper;
        this.p_rcloud = r_cloud;
        this.p_stemp = s_temper;
        this.p_scloud = s_cloud;
    }

    public LatLng getP_latlng(){
        return p_latlng;
    }
    public String getP_placeName(){
        return p_placeName;
    }
    public String getP_sunRise(){return p_sunRise;}
    public String getP_sunSet(){return p_sunSet;}
    public String getP_rtemperature(){return p_rtemp;}
    public String getP_rcloud(){return p_rcloud;}
    public String getP_stemperature(){return p_stemp;}
    public String getP_scloud(){return p_scloud;}
    public int getP_x(){return  p_x;}
    public int getP_y(){return p_y;}
}
