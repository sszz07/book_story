package org.techtown.reiview_app;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.reiview_app.home.home;
import org.techtown.reiview_app.record.record;
import org.techtown.reiview_app.search.search;
import org.techtown.reiview_app.setting.setting;


public class MainActivity2 extends AppCompatActivity {

    org.techtown.reiview_app.home.home home;
    org.techtown.reiview_app.search.search search;
    org.techtown.reiview_app.setting.setting setting;
    org.techtown.reiview_app.record.record record;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        home = new home();
        search = new search();
        setting = new setting();
        record = new record();


        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 초기 플래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.start_activity, home).commitAllowingStateLoss();

        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.start_activity, home).commitAllowingStateLoss();
                        return true;
                    case R.id.search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.start_activity, search).commitAllowingStateLoss();
                        return true;
                    case R.id.bar_chart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.start_activity, record).commitAllowingStateLoss();
                        return true;
                    case R.id.setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.start_activity, setting).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });
    }
}