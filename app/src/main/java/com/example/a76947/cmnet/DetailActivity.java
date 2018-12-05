package com.example.a76947.cmnet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.a76947.cmnet.model.CellInfo;

public class DetailActivity extends AppCompatActivity {

    private WebView text_mainbody;

    private CellInfo cellInfo;

    public static DetailActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
        setContentView(R.layout.activity_detail_simp);
        init();
        instance = this;
        setData(cellInfo);



    }

    public void init(){
        cellInfo = (CellInfo) getIntent().getSerializableExtra("cellDetail");

        // Using scrollview
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        text_mainbody = (WebView)findViewById(R.id.webview1);
        text_mainbody.getSettings().setJavaScriptEnabled(true);
        text_mainbody.setWebViewClient(new WebViewClient());
//        text_mainbody.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

    }

    public void setData(CellInfo info){
        text_mainbody.loadUrl(info.getCellurl());
    }
}
