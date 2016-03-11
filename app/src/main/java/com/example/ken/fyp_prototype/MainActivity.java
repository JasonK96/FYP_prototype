package com.example.ken.fyp_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login_cn = (Button) findViewById(R.id.Language_cn);
        login_cn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("com.example.ken.fyp_prototype.LoginActivity");
                startActivity(i);
            }
        });

        /*Button quick = (Button)findViewById(R.id.btn_quick);
        quick.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent j = new Intent("com.example.ken.fyp_prototype.FunctionActivity");
                startActivity(j);
            }
        });*/
    }
}