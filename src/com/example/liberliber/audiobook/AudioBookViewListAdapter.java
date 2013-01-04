package com.example.liberliber.audiobook;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.liberliber.R;

public class AudioBookViewListAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> mContent;
    private Context mContext;
    
    public AudioBookViewListAdapter(Context context, ArrayList<HashMap<String, String>> content) {
        mContent = content;
        mContext = context;
    }

    public int getCount() {
        return mContent.size();
    }

    public HashMap<String, String> getItem(int position) {
        return mContent.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.audiobook_view_entry_listentry, null);
        }
        
        HashMap<String, String> m = getItem(position);
        if (m != null) {       
            TextView txt = (TextView)v.findViewById(R.id.audiobook_view_entry_listentry_text);
            TextView caption = (TextView)v.findViewById(R.id.audiobook_view_entry_listentry_caption);
            
            if (m.get("type").equals("mp3")) caption.setText(".mp3");
            else if (m.get("type").equals("ogg")) caption.setText(".ogg");
            else if (m.get("type").equals("ipod")) caption.setText(".m4b");
            else caption.setText("unkn");
            
            txt.setText(m.get("text"));
        }
        return v;
    }

}
