package com.example.ken.fyp_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ken on 14/1/2016.
 */
public class LoginActivity extends Activity {
    private static EditText username;
    private static EditText password;
    private static Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        username = (EditText)findViewById(R.id.StaffID);
        password = (EditText)findViewById(R.id.Password);
        login_btn = (Button)findViewById(R.id.Login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (username.getText().toString().equals("admin") &&
                                password.getText().toString().equals("admin")) {
                            Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent("com.example.ken.fyp_prototype.QRScannerActivity");
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Fail!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

    }
}