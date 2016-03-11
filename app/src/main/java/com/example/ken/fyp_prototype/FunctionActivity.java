package com.example.ken.fyp_prototype;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class FunctionActivity extends Activity {

    protected void onCreate(Bundle saveInstanceStat) {
        super.onCreate(saveInstanceStat);
        setContentView(R.layout.function_page);
    }

    public void newSite(View v){
        Intent form = new Intent("com.example.ken.fyp_prototype.FormFilling");
        startActivity(form);
    }
}