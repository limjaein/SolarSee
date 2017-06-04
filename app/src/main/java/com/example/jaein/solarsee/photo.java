package com.example.jaein.solarsee;

/**
 * Created by jaein on 2017-05-30.
 */

public class photo {
    private String p_date;
    private String p_writer;
    private String p_content;
    private String p_loca;
    private String p_like;

    public photo(){
        p_date = "";
        p_writer = "";
        p_content = "";
        p_loca = "";
        p_like = "";
    }
    public photo(String date, String writer, String content, String location, String like){
        p_date = date;
        p_writer = writer;
        p_content = content;
        p_loca = location;
        p_like = like;
    }

    public String getP_date(){ return p_date;}
    public String getP_writer(){ return p_writer;}
    public String getP_content(){ return p_content;}
    public String getP_loca(){ return p_loca;}
    public String getP_like(){ return p_like;}

    public void setP_date(String date){ p_date = date; }
    public void setP_writer(String writer){ p_writer = writer;}
    public void setP_content(String content){ p_content = content;}
    public void setP_loca(String location){ p_loca = location;}
    public void setP_like(String like){ p_like = like; }

}
