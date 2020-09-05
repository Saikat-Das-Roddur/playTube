package com.saikat.playtube.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.saikat.playtube.R;

public class MainActivity extends AppCompatActivity {
    
    //Views
    EditText editTextString;
    ImageView imageViewPlay;


    String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Initializing views
        editTextString = findViewById(R.id.stringEt);
        imageViewPlay = findViewById(R.id.playIv);

        imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String data = editTextString.getText().toString();

              Intent intent = new Intent(MainActivity.this,PlayListActivity.class);
              intent.putExtra("channelId",data);
              startActivity(intent);
            }
        });
    }

}
