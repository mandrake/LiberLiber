package com.example.liberliber;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ViewBooksActivity extends ListActivity {
    private ProgressBar mProgressBar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);
        
        mProgressBar = (ProgressBar)findViewById(R.id.view_books_progbar);
        mProgressBar.setVisibility(View.VISIBLE);
        
        ParseBooks pb = new ParseBooks();
        pb.execute(getIntent().getExtras().getString("url"));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_books, menu);
        return true;
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        
    }

    private class ParseBooks extends AsyncTask
        <String, Void, ArrayList<HashMap<String, String>>> {
        
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            super.onPostExecute(result);
            /* ListAdapter la = new SimpleAdapter(ViewBooksActivity.this, result,
                    R.layout.view_books_entry, new String[] {"title"},
                    new int[] { R.id.book_entry_text }); */
            BookEntryAdapter la = new BookEntryAdapter(ViewBooksActivity.this, result,
                    R.layout.view_books_entry);
            getListView().setAdapter(la);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            ArrayList<HashMap<String, String>> books = new ArrayList<HashMap<String, String>>();
            
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
                    Element trBooks = content.getElementsByTag("tr").get(2);
                    
                    Element tdTitle = trTitle.getElementsByTag("td").get(2);
                    Element tdBooks = trBooks.getElementsByTag("td").get(1);
                    
                    m.put("title", tdTitle.text());
                    
                    for (Element link : tdBooks.getElementsByTag("a")) {
                        Log.w("IMGROBA", link.html());
                        String fileUrl = link.absUrl("href");
                        Element img = link.getElementsByTag("img").first();
                        String imgSrc = img.attr("src");
                        
                        if (imgSrc.matches(".*ebook_pdf_free.*")) m.put("pdf", fileUrl);
                        if (imgSrc.matches(".*ebook_html_free.*")) m.put("html", fileUrl);
                        if (imgSrc.matches(".*ebook_htmlzip_free.*")) m.put("htmlzip", fileUrl);
                        if (imgSrc.matches(".*ebook_rtfzip_free.*")) m.put("rtfzip", fileUrl);
                        if (imgSrc.matches(".*ebook_txtzip_free.*")) m.put("txtzip", fileUrl);
                    }
                    
                    books.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return books;
        }
    }
    
}
