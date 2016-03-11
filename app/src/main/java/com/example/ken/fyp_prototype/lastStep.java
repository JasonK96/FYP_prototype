package com.example.ken.fyp_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jason on 22/2/2016.
 */
public class lastStep extends Activity{
    protected void onCreate(Bundle saveInstanceStat) {
        super.onCreate(saveInstanceStat);
        setContentView(R.layout.finish);
        Button exit = (Button)findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appExit();
            }
        });
    }
    public void appExit()
    {

        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
