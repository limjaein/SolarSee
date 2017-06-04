package com.example.jaein.solarsee;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button joinBtn;

    int JOIN_OK = 100;
    static Typeface font;
    Button btn1, btn2;

    DatabaseReference DBtable;
    DatabaseReference t_member; // 전부다 저장해두기
    static DatabaseReference t_photo; // 입력받는거 만들어야함
    EditText et_id, et_pw;
    String str_id, str_pw;
    static String loginId; // 지금 로긩한 아이디!


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initDB();
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivityForResult(intent, JOIN_OK);
            }
        });
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        et_id = (EditText) findViewById(R.id.loginId);
        et_pw = (EditText) findViewById(R.id.loginPw);

        font = Typeface.createFromAsset(getAssets(), "font.ttf");
        joinBtn = (Button) findViewById(R.id.joinBtn);
        btn1 = (Button) findViewById(R.id.loginBtn);
        btn2 = (Button) findViewById(R.id.joinBtn);
        btn1.setTypeface(font);
        btn2.setTypeface(font);

    }

    private void initDB() {
        DBtable = FirebaseDatabase.getInstance().getReference("SolarSee");
        t_member = DBtable.child("MEMBER_INFO");
        t_photo = DBtable.child("PHOTO_INFO");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JOIN_OK) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "회원가입완료", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void joinBtnClick(View view) {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    public void loginBtnClick(View view) {
        str_id = et_id.getText().toString();
        str_pw = et_pw.getText().toString();

        Query query = t_member.orderByKey().equalTo(str_id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    member m = data.getValue(member.class);
                    if(str_pw.equals(m.getM_pw())){ // 비밀번호 일치
                        Toast.makeText(LoginActivity.this, "일치", Toast.LENGTH_SHORT).show();
                        loginId = str_id;
                        intentToMain();
                    }
                    else{
                        // editText 초기화
                        et_id.setText("");
                        et_pw.setText("");
                        Toast.makeText(LoginActivity.this, "일치하는 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void intentToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
