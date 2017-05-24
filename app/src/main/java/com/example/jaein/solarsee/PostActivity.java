package com.example.jaein.solarsee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

/**
 * Created by jaein on 2017-05-23.
 */

public class PostActivity extends AppCompatActivity {

    Spinner spin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        init();

    }

    private void spinnerBtnClicked() {
        String str;

        switch(spin.getSelectedItemPosition()){
            case 0:
                str = "정동진";
                break;
            case 1:
                str = "서해";
                break;
            case 2:
                str = "동해";
                break;
            case 3:
                str = "코타키나발루";
                break;
        }

    }

    private void init() {
        spin = (Spinner)findViewById(R.id.spinner);
    }
}