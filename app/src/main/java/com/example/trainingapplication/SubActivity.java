package com.example.trainingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        int item = intent.getIntExtra("picture", 0);

        ImageView imageView = findViewById(R.id.selected_photo);
        imageView.setImageResource(item);
    }
}
