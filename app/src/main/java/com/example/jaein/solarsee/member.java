package com.example.jaein.solarsee;

/**
 * Created by jaein on 2017-05-30.
 */

public class member {
    private String m_id; // 사용자 id
    private String m_pw; // 사용자 passwd
    private String m_name; // 닉네임
    public member(){
        m_id = "";
        m_pw = "";
        m_name = "";
    }
    public member(String id, String pw, String name){
        this.m_id = id;
        this.m_pw = pw;
        this.m_name = name;
    }
    public String getM_id(){return m_id;}
    public String getM_pw(){return m_pw;}
    public String getM_name(){return m_name;}

    public void setM_id(String id){m_id = id;}
    public void setM_pw(String pw){m_pw = pw;}
    public void setM_name(String name){m_name = name;}
}
