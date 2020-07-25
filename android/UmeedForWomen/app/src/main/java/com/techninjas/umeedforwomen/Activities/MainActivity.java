package com.techninjas.umeedforwomen.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AsyncPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.techninjas.umeedforwomen.Models.User;
import com.techninjas.umeedforwomen.Network.ApiClient;
import com.techninjas.umeedforwomen.Network.ApiInterface;
import com.techninjas.umeedforwomen.R;
import com.techninjas.umeedforwomen.Utils.Constants;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

        loginButton.setOnClickListener(view -> {
            String mobileNo = Objects.requireNonNull(mobileNumberView.getText()).toString();
            String password = Objects.requireNonNull(passwordView.getText()).toString();

            logger(mobileNo + " " + password);

            authCredentials(mobileNo, password);
            //proceed("Asha");
        });

        //startActivity(new Intent(MainActivity.this, ImageActivity.class));
    }

    private void proceed(String name){
        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    private void authCredentials(String mobileNo, String password){
        if(mobileNo.isEmpty() || password.isEmpty()){
            toast("Please enter your mobile no and password!");
        }

        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<User> call = apiInterface.loginUser(mobileNo, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    String name = response.body().getName();
                    proceed(name);
                }else{
                    toast("Invalid mobile no or password");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                toast("Invalid mobile no or password");
            }
        });
    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void logger(String msg){
        Log.d(Constants.LOG, msg);
    }

}