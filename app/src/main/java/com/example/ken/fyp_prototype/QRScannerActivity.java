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

public class QRScannerActivity extends Activity {

    private Activity mainactivity;
    private EditText scan_content;
    private TextView scan_format;
    private Button scan_btn;

    protected void onCreate(Bundle saveInstanceStat) {
        super.onCreate(saveInstanceStat);
        setContentView(R.layout.qrscanner_page);
        init_view();
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(mainactivity);
                scanIntegrator.initiateScan();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            scan_content.setText(scanContent);
            scan_format.setText(scanFormat);
            if (scanContent.toString().equals("admin123")) {
                Toast.makeText(QRScannerActivity.this, "The QR Code is match", Toast.LENGTH_LONG).show();
                Intent j = new Intent("com.example.ken.fyp_prototype.FunctionActivity");
                startActivity(j);
            } else
                Toast.makeText(QRScannerActivity.this, "The QR Code is not match", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "nothing", Toast.LENGTH_SHORT).show();
        }
    }

    private void init_view() {
        this.scan_content = (EditText) findViewById(R.id.scan_content);
        this.scan_format = (TextView) findViewById(R.id.scan_format);
        this.mainactivity = this;
        this.scan_btn = (Button) findViewById(R.id.scan_btn);
    }

}
