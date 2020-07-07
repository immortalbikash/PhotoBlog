package com.example.photoblog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

public class NewPostActivity extends AppCompatActivity {

    private Toolbar newPostToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        newPostToolBar = findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolBar);
        getSupportActionBar().setTitle("Add New Post");
    }
}
