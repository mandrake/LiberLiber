package com.example.liberliber;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
        try {
            HashMap<String, String> m = (HashMap<String, String>)l.getAdapter().getItem(position);
            ParseBooks pb = new ParseBooks();
            pb.execute(m.get("url"));
            
            ListAdapter la = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    pb.get());
            l.setAdapter(la);
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
    
    private class ParseBooks extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> books = new ArrayList<String>();
            try {
                URL url = new URL(params[0]);
                Document doc = Jsoup.parse(url, 5000);
                
                Element e = doc.getElementById("riga02_colonna02");
                e = e.getElementsByClass("contenuto_cornice").get(1);
                e = e.getElementsByTag("tbody").first();
                e = e.getElementsByTag("tr").get(1);
                e = e.getElementsByTag("td").get(1);
                
                for (Element currbook : e.getElementsByTag("table")) { 
                    Element x = currbook.getAllElements().first(); 
                    books.add(x.getAllElements().first().getAllElements().get(2).text());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return books;
        }
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
                        Log.w("ASDFCURR", "curr url: " + m.get("url"));
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
