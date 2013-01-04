package com.example.liberliber.music;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.liberliber.R;

public class MusicViewEntryActivity extends Activity {
    private TextView mTextTitle, mTextAuthor;
    private ListView mList;
    private ProgressBar mProgressBar;
    
    private String author, title, html, baseUrl;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiobook_view_entry_activity);
        
        mProgressBar = (ProgressBar)findViewById(R.id.audiobook_view_entry_progbar);
        mProgressBar.setVisibility(View.VISIBLE);
        
        mTextTitle = (TextView)findViewById(R.id.audiobook_view_entry_title);
        mTextAuthor = (TextView)findViewById(R.id.audiobook_view_entry_author);
        
        mList = (ListView)findViewById(R.id.audiobook_view_entry_list);
        
        Bundle extras = getIntent().getExtras();
        
        author = extras.getString("author");
        title = extras.getString("title");
        html = extras.getString("html");
        baseUrl = extras.getString("baseurl");
        
        mTextTitle.setText(title);
        mTextAuthor.setText("di " + author);

        ParseLinks pl = new ParseLinks();
        pl.execute(html);
        
        mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> listview, View v, int position,
                    long id) {
                @SuppressWarnings("unchecked")
                HashMap<String, String> m = (HashMap<String, String>)mList.getAdapter().getItem(position);
                
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse(m.get("url")));
                startActivity(i);
            }
        });
    }

    private class ParseLinks extends AsyncTask <String, Void, ArrayList<HashMap<String, String>>> {
        
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            super.onPostExecute(result);
            
            Log.w("ASDFASFW", "size: " + result.size());
            
            MusicViewListAdapter a;
            a = new MusicViewListAdapter(MusicViewEntryActivity.this, result);
            
            mList.setAdapter(a);
            /* mList.setAdapter(
                    new SimpleAdapter(AudioBookViewEntryActivity.this, result,
                            android.R.layout.simple_list_item_2,
                            new String[] {"text", "type"}, new int[] {android.R.id.text1, android.R.id.text2}
                            )); */
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
            
            try {
                Document d = Jsoup.parseBodyFragment(params[0]);     
                Log.w("£", d.html());
                for (Element e : d.body().getElementsByTag("ul")) {
                    for (Element e2 : e.getElementsByTag("li")) {
                        Element link = e2.getElementsByTag("a").first();
                        // Takes only the list items with an anchor inside
                        if (link != null) {
                            HashMap<String, String> m = new HashMap<String, String>();
                            
                            if (e.hasClass("lm_mp3")) {
                                m.put("type", "mp3");
                            }
                            else if (e.hasClass("lm_ogg")) {
                                m.put("type", "ogg");
                            }
                            else if (e.hasClass("lm_ipod")) {
                                m.put("type", "ipod");
                            }
                            else {
                                m.put("type", "unknown");
                            }
                            m.put("text", link.text());
                            m.put("url", baseUrl + '/' + link.attr("href"));
                            
                            result.add(m);
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
            return result;
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_audio_book, menu);
        return true;
    }

    
}
