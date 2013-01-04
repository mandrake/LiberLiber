package com.example.liberliber.audiobook;

import com.example.liberliber.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

public class AudioBookAuthorsFragment extends Fragment {
    private ProgressBar mProgressBar;
    private ListView mListView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View ret = inflater.inflate(R.layout.audiobook_authors_fragment, container, false);
        
        mProgressBar = (ProgressBar)ret.findViewById(R.id.audiobook_authors_fragment_progbar);
        mProgressBar.setVisibility(View.VISIBLE);
        
        mListView = (ListView)ret.findViewById(R.id.audiobook_authors_fragment_listview);//getListView();
        
        mListView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> aView, View v, int position, long id) {
                @SuppressWarnings("unchecked")
                HashMap<String, String> m = (HashMap<String, String>)mListView.getAdapter().getItem(position);

                Intent intent = new Intent(getActivity(), AudioBookViewListActivity.class);
                intent.putExtra("url", m.get("url"));
                intent.putExtra("author", m.get("author"));
                startActivity(intent);
            }
        });
        
        ParseAuthors pa = new ParseAuthors();
        pa.execute();
        
        return ret; 
    }
    
    private class ParseAuthors extends AsyncTask
    <Void, Void, ArrayList<HashMap<String, String>>> {
    
    @Override
    protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
        super.onPostExecute(result);
        if (result != null && AudioBookAuthorsFragment.this.getActivity() != null) {
            ListAdapter la = new SimpleAdapter(AudioBookAuthorsFragment.this.getActivity(), result,
                   android.R.layout.simple_list_item_1, new String[] {"author"},
                   new int[] {android.R.id.text1});
            mListView.setAdapter(la);
        }
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
        ArrayList<HashMap<String, String>> authors =
                new ArrayList<HashMap<String, String>>();
        try {
            char l = 'a';
            while (l <= 'a') {
                URL url = new URL("http://www.liberliber.it/audiolibri/" + l + "/index.htm");
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
                    
                    m.put("author", el.text());
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