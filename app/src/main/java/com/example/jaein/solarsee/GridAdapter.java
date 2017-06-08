package com.example.jaein.solarsee;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.example.jaein.solarsee.LoginActivity.font;
import static com.example.jaein.solarsee.LoginActivity.loginId;
import static com.example.jaein.solarsee.LoginActivity.photoInfo;

/**
 * Created by jaein on 2017-06-05.
 */

public class GridAdapter extends BaseAdapter {
    Context context;
    int layout;
    LayoutInflater inf;
    ArrayList<StorageReference> img;
    AlertDialog.Builder dialog;

    public GridAdapter(Context context, int layout, ArrayList<StorageReference> img) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.size();
    }

    @Override
    public Object getItem(int i) {
        return img.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView=inf.inflate(R.layout.image_row,null);
        ImageView iv=(ImageView)convertView.findViewById(R.id.imageView);

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(img.get(position))
                .into(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                ImageView imgView = (ImageView)dialogView.findViewById(R.id.mainImage);
                final TextView tv_name = (TextView)dialogView.findViewById(R.id.tv_name);
                final TextView tv_content = (TextView)dialogView.findViewById(R.id.tv_content);
                final TextView like_num = (TextView)dialogView.findViewById(R.id.like_num);
                final ImageButton like_btn = (ImageButton)dialogView.findViewById(R.id.likeBtn);

                String imgName =  img.get(position).toString().substring(48);
                imgName = new StringTokenizer(imgName).nextToken(".");

                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(img.get(position))
                        .into(imgView);

                dialog = new AlertDialog.Builder(context);
                dialog.setView(dialogView);

                //속성
                tv_name.setTypeface(font);
                tv_content.setTypeface(font);
                like_num.setTypeface(font);


                //db에서 가져오기
                Query photo_query = photoInfo.orderByChild("p_date").equalTo(imgName);

                photo_query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if(data.getValue()!=null){
                                final photo p = data.getValue(photo.class);
                                final String like = p.getP_like();
                                String[] like_people = like.split(",");
                                final ArrayList<String> like_peopleName = new ArrayList<String>();
                                for(int i=0; i< like_people.length; i++) {
                                    like_peopleName.add(like_people[i]);
                                }

                                int count = 0;

                                for(String str : like_people){
                                    if(str.equals("")){
                                        count = 0;
                                    }
                                    else{
                                        if(str.equals(loginId)){
                                            like_btn.setImageResource(R.drawable.like_btn_click);
                                        }
                                        else{
                                            like_btn.setImageResource(R.drawable.like_btn);
                                        }
                                        count++;
                                    }
                                }

                                //닉네임 글 넣어두기
                                tv_name.setText("Made By "+p.getP_name());

                                String content = p.getP_content();
                                tv_content.setText(content);
                                like_num.setText(count+"");

                                final int changeCount = count;

                                like_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Drawable temp = like_btn.getDrawable();
                                        Drawable temp1 = context.getResources().getDrawable(R.drawable.like_btn);

                                        Bitmap tmpBitmap = ((BitmapDrawable)temp).getBitmap();  //like_btn에 적용되어 있는 이미지
                                        Bitmap tmpBitmap1 = ((BitmapDrawable)temp1).getBitmap();
                                        if(tmpBitmap.equals(tmpBitmap1))    //빈 버튼일때
                                        {
                                            like_btn.setImageResource(R.drawable.like_btn_click);
                                            like_num.setText(changeCount+1+"");
                                            if(p.getP_like().equals("")){
                                                p.setP_like(loginId);
                                            }
                                            else{
                                                p.setP_like(p.getP_like()+","+loginId);
                                            }
                                            photoInfo.child(p.getP_date()).setValue(p);
                                        }
                                        else{
                                            like_btn.setImageResource(R.drawable.like_btn);
                                            like_num.setText(changeCount-1+"");
                                            for (int a = 0; a<like_peopleName.size(); a++){
                                                if(loginId.equals(like_peopleName.get(a))){
                                                    like_peopleName.remove(a);
                                                }
                                            }
                                            String likeId = "";
                                            for(int i= 0; i<like_peopleName.size(); i++){
                                                if(i==like_peopleName.size()-1){
                                                    likeId += like_peopleName.get(i);
                                                }
                                                else{
                                                    likeId += like_peopleName.get(i)+",";
                                                }
                                            }
                                            p.setP_like(likeId);
                                            photoInfo.child(p.getP_date()).setValue(p);

                                        }

                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog.show();

            }
        });
        return convertView;

    }
}
