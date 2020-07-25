package com.techninjas.umeedforwomen.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.techninjas.umeedforwomen.R;
import com.techninjas.umeedforwomen.Utils.Constants;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText mobileNumberView;
    private TextInputEditText passwordView;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobileNumberView = findViewById(R.id.number);
        passwordView = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNo = Objects.requireNonNull(mobileNumberView.getText()).toString();
                String password = Objects.requireNonNull(passwordView.getText()).toString();

                logger(mobileNo + " " + password);

                if(authCredentials(mobileNo, password))
                    proceed();
                else
                    toast("Invalid mobile no. or password");
            }
        });
        //startActivity(new Intent(MainActivity.this, ImageActivity.class));
    }

    private void proceed(){
        startActivity(new Intent(MainActivity.this, TaskActivity.class));
    }

    private boolean authCredentials(String mobileNo, String password){
        if(mobileNo.isEmpty() || password.isEmpty()){
            toast("Please enter your mobile no and password!");
            return true;
        }
        return true;
    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void logger(String msg){
        Log.d(Constants.LOG, msg);
    }

}