package com.example.jaein.solarsee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    DatabaseReference DBtable;
    DatabaseReference t_member; // 전부다 저장해두기
    DatabaseReference t_photo; // 입력받는거 만들어야함
    private FirebaseAuth mAuth;
    EditText et_id, et_pw;
    String str_id, str_pw;
    int JOIN_OK = -100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        initDB();
    }

    private void init() {
        et_id = (EditText)findViewById(R.id.et_id);
        et_pw = (EditText)findViewById(R.id.et_pw);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("dlawodls96@naver.com", "ss9436022");
    }
    private void initDB() {
        DBtable = FirebaseDatabase.getInstance().getReference("SolarSee");
        t_member = DBtable.child("MEMBER_INFO");
        t_photo = DBtable.child("PHOTO_INFO");
    }

    public void joinBtnClick(View view) {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == JOIN_OK){
            if(resultCode == RESULT_OK){
            }
        }
    }

    public void loginBtnClick(View view){
        //DB와 비교
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
