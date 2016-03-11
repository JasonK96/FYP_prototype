package com.example.ken.fyp_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.view.View;
import android.content.*;
import android.webkit.WebViewClient;

/**
 * Created by Jason on 25/1/2016.
 */
public class FormFilling extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fill_form);
        WebView jj = (WebView)findViewById(R.id.form);
        jj.setWebViewClient(new WebViewClient());

        try{
            jj.loadUrl("file:///storage/emulated/0/app(by ken).html");
            //
        }
        catch (Exception e) {
            Log.i("NET", "Error in network call", e);
        }

        //webview setting
        WebSettings webSettings = jj.getSettings();
        //webview font size setting
        webSettings.setDefaultFontSize(30);
    }

    public void submit(View v){
        Intent change = new Intent("com.example.ken.fyp_prototype.TakePhoto");
        startActivity(change);
    }
}
