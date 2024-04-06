package org.techtown.reiview_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.techtown.reiview_app.home.home;

public class login extends AppCompatActivity {
String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        if(!email.equals("")){
            @SuppressLint("HandlerLeak") Handler handler = new Handler() {

                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    startActivity(new Intent(login.this, MainActivity2.class));
                    finish();
                }

            };
            handler.sendEmptyMessageDelayed(0, 500);
        }else{
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        }
    }
}