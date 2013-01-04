package com.example.liberliber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.liberliber.audiobook.AudioBookViewByActivity;
import com.example.liberliber.book.BookViewByActivity;
import com.example.liberliber.music.MusicViewByActivity;

public class MainActivity extends Activity {
    
    public void btnBook(View v) {
        startActivity(new Intent(this, BookViewByActivity.class));
    }
    
    public void btnAudioBook(View v) {
        startActivity(new Intent(this, AudioBookViewByActivity.class));
    }
    
    public void btnMusic(View v) {
        startActivity(new Intent(this, MusicViewByActivity.class));
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
