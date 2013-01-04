package com.example.liberliber.audiobook;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.example.liberliber.R;

public class AudioBookViewListActivity extends ListActivity {
    private ProgressBar mProgressBar;
    private String mUrl, mAuthor;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiobook_view_list_activity);
        
        mProgressBar = (ProgressBar)findViewById(R.id.audiobook_view_list_progbar);
        mProgressBar.setVisibility(View.VISIBLE);
        
        mUrl = getIntent().getExtras().getString("url");
        mAuthor = getIntent().getExtras().getString("author");
        
        setTitle("Autore: " + mAuthor);
        
        ParseEntries pe = new ParseEntries();
        pe.execute(mUrl);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_books, menu);
        return true;
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        @SuppressWarnings("unchecked")
        HashMap<String, String> m = (HashMap<String, String>)l.getAdapter().getItem(position);        
        Intent i = null;
        i = new Intent(this, AudioBookViewEntryActivity.class);
        
        i.putExtra("author", m.get("author"));
        i.putExtra("title", m.get("title"));
        i.putExtra("html", m.get("html"));
        i.putExtra("baseurl", mUrl.substring(0, mUrl.lastIndexOf('/')));
    
        startActivity(i);
    }
    
    private class ParseEntries extends AsyncTask
        <String, Void, ArrayList<HashMap<String, String>>> {
        
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            ListAdapter la;
            
            super.onPostExecute(result);
            la = new SimpleAdapter(
                    AudioBookViewListActivity.this, result,
                    android.R.layout.simple_list_item_1, new String[] {"title"},
                    new int[] {android.R.id.text1});
            getListView().setAdapter(la);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            ArrayList<HashMap<String, String>> audioBooks = new ArrayList<HashMap<String, String>>();
            
            try {
                URL url = new URL(params[0]);
                Document doc = Jsoup.parse(url, 5000);
                
                Element e = doc.getElementById("riga02_colonna02");
                e = e.getElementsByClass("contenuto_cornice").get(1);
                e = e.getElementsByTag("tbody").first();
                e = e.getElementsByTag("tr").get(1);
                e = e.getElementsByTag("td").get(1);
                
                for (Element currbook : e.getElementsByTag("table")) {
                    HashMap<String, String> m = new HashMap<String, String>();
                    
                    Element content = currbook.getElementsByTag("tbody").first(); // tbody
                    
                    Element trTitle = content.getElementsByTag("tr").first();
                    Element trAudioBooks = content.getElementsByTag("tr").get(6);
                    
                    Element tdTitle = trTitle.getElementsByTag("td").get(2);
                    Element tdAudioBooks = trAudioBooks.getElementsByTag("td").get(1);
                    
                    m.put("title", tdTitle.text());
                    m.put("html", tdAudioBooks.html());
                    m.put("author", mAuthor);
                    
                    audioBooks.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return audioBooks;
        }
    }
    
}
