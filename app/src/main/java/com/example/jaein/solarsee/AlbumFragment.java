package com.example.jaein.solarsee;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.jaein.solarsee.LoginActivity.photoInfo;


public class AlbumFragment extends Fragment {
    GridLayout gl;
    static ArrayList<photo> photo_list;
    public GridAdapter gridAdapter;
    public GridView gridView;

    public AlbumFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridView = (GridView)getActivity().findViewById(R.id.gridview);
        Toast.makeText(getActivity(), "헤헤", Toast.LENGTH_SHORT).show();
        photo_list = new ArrayList<>();
        int img[] = {
                R.drawable.start_image, R.drawable.start_image,
                R.drawable.start_image, R.drawable.start_image
        };

        GridAdapter adapter = new GridAdapter(
                getActivity().getApplicationContext(),
                R.layout.image_row,
                img
        );

        gridView.setAdapter(adapter);

        setImageView();
    }

    public void setImageView(){
        Query photo_query = photoInfo.orderByChild("p_date");
        photo_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.getValue()!=null){
                        photo_list.clear();
                        photo p = data.getValue(photo.class);
                        photo_list.add(p);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
