package com.example.jaein.solarsee;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.jaein.solarsee.CustomMapFragment.location;
import static com.example.jaein.solarsee.LoginActivity.loginId;
import static com.example.jaein.solarsee.LoginActivity.photoInfo;


public class AlbumFragment extends Fragment {
    GridLayout gl;
    static ArrayList<photo> photo_list;
    static ArrayList<photo> homephoto_list;
    static ArrayList<photo> locaphoto_list;
    public GridAdapter gridAdapter;
    public GridView gridView;
    ImageView imageView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://solarsee-859f7.appspot.com/solarsee_images");

    static boolean locationTure;
    //////추가///////
    ImageButton home;

    public AlbumFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String myTag = getTag();
        ((MainActivity)getActivity()).setAlbumFragmentB(myTag);

        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(locationTure){
                locaphoto_list.clear();
                locationTure = false;
            }
            else{
                photo_list.clear();
                setImageView();
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        photo_list.clear();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridView = (GridView)getActivity().findViewById(R.id.gridview);
        imageView = (ImageView)getActivity().findViewById(R.id.imageView);

        ///////추가//////////
        home = (ImageButton)getActivity().findViewById(R.id.home);
        homephoto_list = new ArrayList<>();
        locaphoto_list = new ArrayList<>();

        Toast.makeText(getActivity(), "헤헤", Toast.LENGTH_SHORT).show();
        photo_list = new ArrayList<>();

        //setImageView();

        home.setOnClickListener(new View.OnClickListener() {    //사용자가 올린 사진만 보여준다.
            @Override
            public void onClick(View view) {
                final Query homephoto_query = photoInfo.orderByChild("p_writer").equalTo(loginId);
                homephoto_list.clear();
                homephoto_query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if(data.getValue()!=null){
                                photo p = data.getValue(photo.class);
                                homephoto_list.add(p);
                            }
                        }
                        ArrayList<StorageReference> images = new ArrayList<>();
                        for(int i=0 ; i<homephoto_list.size(); i++){
                            images.add(storageReference.child(homephoto_list.get(i).getP_date()+".png"));
                        }
                        GridAdapter g_adapter = new GridAdapter(
                                getActivity().getApplicationContext(),
                                R.layout.image_row,
                                images
                        );
                        gridView.setAdapter(g_adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void setImageView(){
        Query photo_query = photoInfo.orderByChild("p_date");

        photo_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.getValue()!=null){
                        photo p = data.getValue(photo.class);
                        photo_list.add(p);
                    }
                }
                ArrayList<StorageReference> images = new ArrayList<>();
                for(int i=0 ; i<photo_list.size(); i++){
                    images.add(storageReference.child(photo_list.get(i).getP_date()+".png"));
                }
                GridAdapter g_adapter = new GridAdapter(
                        getActivity().getApplicationContext(),
                        R.layout.image_row,
                        images
                );
                gridView.setAdapter(g_adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void locaImageList(){
        locationTure = true;
        locaphoto_list.clear();
        final Query homephoto_query = photoInfo.orderByChild("p_loca").equalTo(location);
        locaphoto_list.clear();
        homephoto_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.getValue()!=null){
                        photo p = data.getValue(photo.class);
                        locaphoto_list.add(p);
                    }
                }
                ArrayList<StorageReference> images = new ArrayList<>();
                for(int i=0 ; i<locaphoto_list.size(); i++){
                    images.add(storageReference.child(locaphoto_list.get(i).getP_date()+".png"));
                }
                GridAdapter g_adapter = new GridAdapter(
                        getActivity().getApplicationContext(),
                        R.layout.image_row,
                        images
                );
                gridView.setAdapter(g_adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
