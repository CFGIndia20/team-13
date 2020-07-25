package com.techninjas.umeedforwomen.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.techninjas.umeedforwomen.R;

public class MainActivity extends AppCompatActivity {
    private static final String LOG = "APP_LOGS";
    private TextInputEditText aadharNumber;
    private TextInputEditText password;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aadharNumber = findViewById(R.id.number);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG, aadharNumber.getText() + " " + password.getText());
            }
        });
        //startActivity(new Intent(MainActivity.this, ImageActivity.class));
    }
}