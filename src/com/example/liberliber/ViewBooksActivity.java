package com.example.liberliber;

import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewBooksActivity extends ListActivity {
    private ListView mListView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);
        
        mListView = getListView();
        
        ParseAuthors pa = new ParseAuthors();
        pa.execute();
        try {
            String[] authors = pa.get();
            ListAdapter la = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, authors);
            mListView.setAdapter(la);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_books, menu);
        return true;
    }
    
    private class ParseAuthors extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            ArrayList<String> authors = new ArrayList<String>();
            try {
                char l = 'a';
                while (l <= 'z') {
                    URL url = new URL("http://www.liberliber.it/libri/" + l + "/index.htm");
                    Document doc = Jsoup.parse(url, 5000);
                    
                    Element e = doc.getElementById("riga02_colonna02");
                    e = e.getElementsByClass("contenuto_cornice").first();
                    e = e.getElementsByTag("tbody").first();
                    e = e.getElementsByTag("tr").get(1);
                    e = e.getElementsByTag("td").get(1);
                    e = e.getElementsByTag("ul").first();
                    
                    for (Element name : e.getElementsByTag("li")) {
                        authors.add(name.getAllElements().first().text());
                    }
                    
                    l++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return authors.toArray(new String[authors.size()]);
        }
        
    }
    
}
