package com.example.liberliber.book;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.liberliber.R;

public class BookViewEntryActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_view_entry_activity);
        
        TextView mTitle, mAuthor;
        
        mTitle = (TextView)findViewById(R.id.book_view_entry_title);
        mAuthor = (TextView)findViewById(R.id.book_view_entry_author);
        
        ImageView mImgPdf, mImgHtml, mImgHtmlzip, mImgRtfzip, mImgTxtzip;
        mImgPdf = (ImageView)findViewById(R.id.book_view_imgpdf);
        mImgHtml = (ImageView)findViewById(R.id.book_view_imghtml);
        mImgHtmlzip = (ImageView)findViewById(R.id.book_view_imghtmlzip);
        mImgRtfzip = (ImageView)findViewById(R.id.book_view_imgrtfzip);
        mImgTxtzip = (ImageView)findViewById(R.id.book_view_imgtxtzip);
        
        ProgressBar mProgressBar = (ProgressBar)findViewById(R.id.book_view_progbar);
        mProgressBar.setVisibility(View.VISIBLE);
        
        Log.w("ASDFAF", getIntent().getExtras().getString("title"));
        Log.w("ASDFAF", getIntent().getExtras().getString("author"));
        
        mTitle.setText(getIntent().getExtras().getString("title"));
        mAuthor.setText("di " + getIntent().getExtras().getString("author"));
        
        String html, baseUrl;
        html = getIntent().getExtras().getString("html");
        baseUrl = getIntent().getExtras().getString("baseurl");
        
        Document doc = Jsoup.parse(html);
        for (Element link : doc.getElementsByTag("a")) {
            String fileUrl = baseUrl + '/' + link.attr("href");
            Element img = link.getElementsByTag("img").first();
            String imgSrc = img.attr("src");
            
            if (imgSrc.matches(".*ebook_pdf_free.*")) { mImgPdf.setAlpha(1.0f); mImgPdf.setTag(fileUrl); }
            if (imgSrc.matches(".*ebook_html_free.*")) { mImgHtml.setAlpha(1.0f); mImgHtml.setTag(fileUrl); }
            if (imgSrc.matches(".*ebook_htmlzip_free.*")) { mImgHtmlzip.setAlpha(1.0f); mImgHtmlzip.setTag(fileUrl); }
            if (imgSrc.matches(".*ebook_rtfzip_free.*")) { mImgRtfzip.setAlpha(1.0f); mImgRtfzip.setTag(fileUrl); }
            if (imgSrc.matches(".*ebook_txtzip_free.*")) { mImgTxtzip.setAlpha(1.0f); mImgTxtzip.setTag(fileUrl); }
        }
        
        ImageView imgs[] = new ImageView[] {mImgPdf, mImgHtml, mImgHtmlzip, mImgRtfzip, mImgTxtzip};
        
        for (ImageView img : imgs) {
            img.setOnClickListener(new OnClickListener() {
                
                public void onClick(View v) {
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse((String)v.getTag()));
                    startActivity(i);
                }
            });
        }
        
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_book, menu);
        return true;
    }
    
}
