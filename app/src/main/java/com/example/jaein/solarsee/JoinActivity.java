package com.example.jaein.solarsee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class JoinActivity extends AppCompatActivity {

    EditText joinID;
    EditText joinPw;
    EditText joinNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        init();
    }

    public void init(){
        joinID = (EditText)findViewById(R.id.joinID);
        joinNickname = (EditText)findViewById(R.id.joinNickname);
    }

    public void joinBtnClick(View view) {
        switch(view.getId()){
            case R.id.idcheckBtn:
                idCheck();
                break;
            case R.id.nickcheckBtn:
                nickCkeck();
                break;
            case R.id.joinBtn:
                join();
                break;
        }
    }

    public void idCheck(){
        String idTxt = joinID.getText().toString();

        ////////디비 아이디들이랑 비교/////////
    }
    public void nickCkeck(){
        String nickTxt = joinNickname.getText().toString();

        ////////디비 닉네임들이랑 비교//////////
    }
    public void join(){
        String id = joinID.getText().toString();
        String pw = joinPw.getText().toString();
        String nick = joinNickname.getText().toString();

        /////////////디비에 저장/////////////////
    }
}
