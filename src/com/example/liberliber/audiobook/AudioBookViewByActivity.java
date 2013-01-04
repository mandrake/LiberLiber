package com.example.liberliber.audiobook;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.example.liberliber.R;

public class AudioBookViewByActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiobook_view_by_activity);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_authors, menu);
        return true;
    }
}
