package com.example.liberliber;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
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

public class ViewEntriesActivity extends ListActivity {
    private ProgressBar mProgressBar;
    private String mType, mUrl, mAuthor;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entries);
        
        mProgressBar = (ProgressBar)findViewById(R.id.view_books_progbar);
        mProgressBar.setVisibility(View.VISIBLE);
        
        mType = getIntent().getExtras().getString("type");
        mUrl = getIntent().getExtras().getString("url");
        mAuthor = getIntent().getExtras().getString("author");
        
        ParseEntries pe = new ParseEntries();
        pe.execute(mType, mUrl);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_books, menu);
        return true;
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        HashMap<String, String> m = (HashMap<String, String>)l.getAdapter().getItem(position);        
        Intent i = null;
        if (mType.equals("book")) {
            i = new Intent(this, ViewBookActivity.class);
            
            i.putExtra("author", m.get("author"));
            i.putExtra("title", m.get("title"));
            i.putExtra("html", m.get("html"));
            i.putExtra("baseurl", mUrl.substring(0, mUrl.lastIndexOf('/')));
        }
        if (mType.equals("audiobook")) {
            i = new Intent(this, ViewAudioBookActivity.class);
            
            i.putExtra("author", m.get("author"));
            i.putExtra("title", m.get("title"));
            i.putExtra("html", m.get("html"));
            i.putExtra("baseurl", mUrl.substring(0, mUrl.lastIndexOf('/')));
        }
        startActivity(i);
    }
    
    private class ParseEntries extends AsyncTask
        <String, Void, ArrayList<HashMap<String, String>>> {
        
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            super.onPostExecute(result);
            ListAdapter la = null;
            if (mType.equals("book")) {
                la = new SimpleAdapter(ViewEntriesActivity.this, result, android.R.layout.simple_list_item_1,
                        new String[] {"title"}, new int[] {android.R.id.text1});
                /* la = new BookEntryAdapter(ViewEntriesActivity.this, result,
                        R.layout.view_books_entry); */
            }
            if (mType.equals("audiobook")) {
                /* 
                la = new AudioBookEntryAdapter(ViewEntriesActivity.this, result,
                        R.layout.view_audiobooks_entry); */
                la = new SimpleAdapter(ViewEntriesActivity.this, result, android.R.layout.simple_list_item_1,
                        new String[] {"title"}, new int[] {android.R.id.text1});
            }
            if (mType.equals("music")) {
                la = new MusicEntryAdapter(ViewEntriesActivity.this, result,
                        R.layout.view_music_entry);
            }
            getListView().setAdapter(la);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        
        private ArrayList<HashMap<String, String>> parseBooks (String surl) {
            
            ArrayList<HashMap<String, String>> books = new ArrayList<HashMap<String, String>>();
            
            try {
                URL url = new URL(surl);
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
                    Element trBooks = content.getElementsByTag("tr").get(2);
                    
                    Element tdTitle = trTitle.getElementsByTag("td").get(2);
                    Element tdBooks = trBooks.getElementsByTag("td").get(1);
                    
                    m.put("title", tdTitle.text());
                    m.put("html", tdBooks.html());
                    m.put("author", mAuthor);
                    
                    books.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return books;
        }
        
        private ArrayList<HashMap<String, String>> parseAudioBooks (String surl) {
            ArrayList<HashMap<String, String>> audioBooks = new ArrayList<HashMap<String, String>>();
            
            try {
                URL url = new URL(surl);
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
                    m.put("html", trAudioBooks.html());
                    m.put("author", mAuthor);
                    
                    audioBooks.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return audioBooks;
        }
        
        private ArrayList<HashMap<String, String>> parseMusic (String surl) {
            return null;
        }
        
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            mType = params[0];
            if (mType.equals("book")) return parseBooks(params[1]);
            if (mType.equals("audiobook")) return parseAudioBooks(params[1]);
            if (mType.equals("music")) return parseMusic(params[1]);
            return null;
        }
    }
    
}
