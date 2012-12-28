package com.example.liberliber;

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewAuthorsActivity extends ListActivity {
    private ListView mListView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);
        
        mListView = getListView();
        
        ParseAuthors pa = new ParseAuthors();
        pa.execute();
        try {
            ListAdapter la = new SimpleAdapter(this, pa.get(),
                   android.R.layout.simple_list_item_1, new String[] {"name"},
                   new int[] {android.R.id.text1});
            mListView.setAdapter(la);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
               
        @SuppressWarnings("unchecked")
        HashMap<String, String> m = (HashMap<String, String>)l.getAdapter().getItem(position);

        Intent intent = new Intent(this, ViewBooksActivity.class);
        intent.putExtra("url", m.get("url"));
        startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_authors, menu);
        return true;
    }
    
    private class ParseAuthors extends AsyncTask
        <Void, Void, ArrayList<HashMap<String, String>>> {

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            //ArrayList<String> authors = new ArrayList<String>();
            ArrayList<HashMap<String, String>> authors =
                    new ArrayList<HashMap<String, String>>();
            try {
                char l = 'a';
                while (l <= 'a') {
                    URL url = new URL("http://www.liberliber.it/libri/" + l + "/index.htm");
                    Document doc = Jsoup.parse(url, 5000);
                    
                    Element e = doc.getElementById("riga02_colonna02");
                    e = e.getElementsByClass("contenuto_cornice").first();
                    e = e.getElementsByTag("tbody").first();
                    e = e.getElementsByTag("tr").get(1);
                    e = e.getElementsByTag("td").get(1);
                    e = e.getElementsByTag("ul").first();
                    
                    for (Element curr : e.getElementsByTag("li")) {
                        HashMap<String, String> m = new HashMap<String, String>();
                        Element el = curr.getAllElements().first();
                        
                        m.put("name", el.text());
                        m.put("url", el.unwrap().absUrl("href"));

                        authors.add(m);
                    }
                    
                    l++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return authors;
        }
    }
}
