package com.saikat.playtube.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.saikat.playtube.R;

public class PlayListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        String chanelId = getIntent().getStringExtra("chanelId");
    }
}
