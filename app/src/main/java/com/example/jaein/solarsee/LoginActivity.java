package com.example.jaein.solarsee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button joinBtn;

    int JOIN_OK = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivityForResult(intent, JOIN_OK);
            }
        });
    }

    public void init(){
        joinBtn = (Button)findViewById(R.id.joinBtn);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == JOIN_OK){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "회원가입완료", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
