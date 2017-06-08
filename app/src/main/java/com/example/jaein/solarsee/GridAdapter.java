package com.example.jaein.solarsee;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.jaein.solarsee.AlbumFragment.photo_list;
import static com.example.jaein.solarsee.LoginActivity.font;
import static com.example.jaein.solarsee.LoginActivity.loginName;

/**
 * Created by jaein on 2017-06-05.
 */

public class GridAdapter extends BaseAdapter {
    Context context;
    int layout;
    //ArrayList<DrawableTypeRequest> img;
    LayoutInflater inf;
    ArrayList<StorageReference> img;

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
        Log.i("png", img.get(position).toString());

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(img.get(position))
                .into(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                ImageView imgView = (ImageView)dialogView.findViewById(R.id.mainImage);
                TextView tv_name = (TextView)dialogView.findViewById(R.id.tv_name);
                TextView tv_content = (TextView)dialogView.findViewById(R.id.tv_content);
                TextView like_num = (TextView)dialogView.findViewById(R.id.like_num);

                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(img.get(position))
                        .into(imgView);

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setView(dialogView);

                //속성
                tv_name.setTypeface(font);
                tv_content.setTypeface(font);
                like_num.setTypeface(font);

                //좋아요 수 세기
                String like = photo_list.get(position).getP_like();

                String[] like_people = like.split(",");

                int count = 0;

                for(String str : like_people){
                    if(str.equals("")){
                        count = 0;
                    }
                    else{
                        count++;
                    }
                }

                //닉네임 글 넣어두기
                tv_name.setText("Made By "+loginName);

                String content = photo_list.get(position).getP_content();
                tv_content.setText(content);
                like_num.setText(count+"");
                
                dialog.show();

            }
        });
        return convertView;

    }
}
