package com.example.jaein.solarsee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinActivity extends AppCompatActivity {

    EditText joinID;
    EditText joinPw;
    EditText joinNickname;
    DatabaseReference memberInfo;
    DatabaseReference photoInfo;
    FirebaseDatabase fDatabase;

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
        joinPw = (EditText)findViewById(R.id.joinPW);
        joinNickname = (EditText)findViewById(R.id.joinNickname);

        fDatabase = FirebaseDatabase.getInstance();
        memberInfo = fDatabase.getReference("SolarSee/MEMBER_INFO");
        photoInfo = fDatabase.getReference("SolarSee/PHOTO_INFO");
    }

    public void joinBtnClick(View view) {
        switch(view.getId()){
            case R.id.idcheckBtn:
                idCheck();
                break;
            case R.id.nickcheckBtn:
                nickCkeck();
                break;
            case R.id.completeJoin:
                join();
                break;
        }
    }

    public void idCheck(){

        ////////디비 아이디들이랑 비교/////////
        final String j_id = joinID.getText().toString();
        //select *from customer where customer id='banana';
        Query query = memberInfo.orderByChild("m_id").equalTo(j_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    Toast.makeText(JoinActivity.this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        member item = data.getValue(member.class);
                        if(j_id.equals(item.getM_id())){
                            Toast.makeText(JoinActivity.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void nickCkeck(){
        final String j_name = joinNickname.getText().toString();
        Query query = memberInfo.orderByChild("m_name").equalTo(j_name);
        ////////디비 닉네임들이랑 비교//////////
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    Toast.makeText(JoinActivity.this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                       member item = data.getValue(member.class);
                        if(j_name.equals(item.getM_name())){
                            Toast.makeText(JoinActivity.this, "이미 사용중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void join(){
        String id = joinID.getText().toString();
        String pw = joinPw.getText().toString();
        String nick = joinNickname.getText().toString();



        /////////////디비에 저장/////////////////
        member member = new member(id, nick, pw);
        memberInfo.child(id).setValue(member);
        Toast.makeText(this, "디비저장 완료", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
