package com.example.liberliber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button[] btns = new Button[] {
                (Button)findViewById(R.id.main_btn_books),
                (Button)findViewById(R.id.main_btn_audiobooks),
                (Button)findViewById(R.id.main_btn_music)/* ,
                (Button)findViewById(R.id.main_btn_video)*/
        };
        
        for (Button b : btns) {
            b.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, ViewAuthorsActivity.class);
                    switch(v.getId()) {
                    case R.id.main_btn_books:
                        i.putExtra("type", "book");
                        break;
                    case R.id.main_btn_audiobooks:
                        i.putExtra("type", "audiobook");
                        break;
                    case R.id.main_btn_music:
                        i.putExtra("type", "music");
                        break;
                    /* case R.id.main_btn_video:
                        i.putExtra("type", "video");
                        break; */
                    }
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
