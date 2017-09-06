package com.example.home.firstproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class View_friend_photo extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_photo);
        imageView = (ImageView) findViewById(R.id.imageView3);
        String ava_url;
        String name;
        name = getIntent().getStringExtra("name");
        setTitle(name);
        ava_url = getIntent().getStringExtra("ava_url");
        Picasso.with(getApplicationContext())
                .load(ava_url)
                .into(imageView);


    }
}
