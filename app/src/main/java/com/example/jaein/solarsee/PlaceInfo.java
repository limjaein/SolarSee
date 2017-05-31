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

    public PlaceInfo(LatLng latlng, String placeName){
        this.p_latlng = latlng;
        this.p_placeName = placeName;
        p_sunRise = "";
        p_sunSet = "";
    }


    public void setInfo(String sunrise, String sunset){
        this.p_sunRise = sunrise;
        this.p_sunSet = sunset;
    }

    public LatLng getP_latlng(){
        return p_latlng;
    }
    public String getP_placeName(){
        return p_placeName;
    }
    public String getP_sunRise(){return p_sunRise;}
    public String getP_sunSet(){return p_sunSet;}
}
