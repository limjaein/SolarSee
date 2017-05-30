package com.example.jaein.solarsee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.jaein.solarsee.LoginActivity.font;

public class JoinActivity extends AppCompatActivity {

    EditText joinID;
    EditText joinPw;
    EditText joinNickname;

    Button idBtn, nickBtn, joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        init();
    }

    public void init(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        joinID = (EditText)findViewById(R.id.joinID);
        joinNickname = (EditText)findViewById(R.id.joinNickname);
        idBtn = (Button)findViewById(R.id.idcheckBtn);
        nickBtn = (Button)findViewById(R.id.nickcheckBtn);
        joinBtn = (Button)findViewById(R.id.joinBtn);
        idBtn.setTypeface(font);
        nickBtn.setTypeface(font);
        joinBtn.setTypeface(font);
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
