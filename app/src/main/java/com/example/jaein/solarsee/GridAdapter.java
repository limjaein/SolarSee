package com.example.jaein.solarsee;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView=inf.inflate(R.layout.image_row,null);
        ImageView iv=(ImageView)convertView.findViewById(R.id.imageView);
        Log.i("png", img.get(position).toString());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(img.get(position))
                .into(iv);

        return convertView;

    }
}
