package com.example.liberliber;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class BookEntryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, String>> mData;
    private int mResource;
    
    public BookEntryAdapter(Context context, ArrayList<HashMap<String, String>> data,
            int resource) {
        mContext = context;
        mData = data;
        mResource = resource;
    }

    public int getCount() {
        return mData.size();
    }

    public HashMap<String, String> getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        BookEntryHolder holder = null;
        
        if (v == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            v = inflater.inflate(mResource, parent, false);
            
            holder = new BookEntryHolder();
            holder.textView = (TextView)v.findViewById(R.id.book_entry_text);
            
            holder.btnPdf = (Button)v.findViewById(R.id.book_entry_pdf);
            holder.btnHtml = (Button)v.findViewById(R.id.book_entry_html);
            holder.btnHtmlzip = (Button)v.findViewById(R.id.book_entry_htmlzip);
            holder.btnRtfzip = (Button)v.findViewById(R.id.book_entry_rtfzip);
            holder.btnTxtzip = (Button)v.findViewById(R.id.book_entry_txtzip);
            
            v.setTag(holder);
        }
        else {
            holder = (BookEntryHolder)v.getTag();
        }
        
        HashMap<String, String> m = getItem(position);
        
        String title = m.get("title");
        
        holder.textView.setText(title);
        
        String keys[] = new String[] {"pdf", "html", "htmlzip", "rtfzip", "txtzip"};
        Button btns[] = new Button[] {holder.btnPdf, holder.btnHtml, holder.btnHtmlzip,
                holder.btnRtfzip, holder.btnTxtzip};

        int i;
        for (i = 0; i < keys.length; i++) {
            if (m.containsKey(keys[i])) {
                btns[i].setVisibility(View.VISIBLE);
                btns[i].setTag(m.get(keys[i]));
                btns[i].setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((String)v.getTag()));
                        mContext.startActivity(intent);
                    }
                });
            }
        }
        
        return v;
    }
    
    static class BookEntryHolder {
        TextView textView;
        Button btnPdf, btnHtml, btnHtmlzip, btnRtfzip, btnTxtzip;
    }

}
