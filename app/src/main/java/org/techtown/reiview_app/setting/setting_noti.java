package org.techtown.reiview_app.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.techtown.reiview_app.R;

public class setting_noti extends AppCompatActivity {


    TextView setting_noti_textview;
    Switch setting_noti_swithch_button;
    String noti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_noti);

        //1.쉐어드 만들어서 알림 켜기 끄기 만들기
        //2.

        setting_noti_textview = (TextView) findViewById(R.id.setting_noti_textview);
        setting_noti_swithch_button = (Switch) findViewById(R.id.setting_noti_swithch_button);

        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        noti = pref.getString("알림", "");


        if (noti.equals("켜기")) {
            setting_noti_swithch_button.setChecked(true);
            setting_noti_textview.setText("알림 ON");
        } else {
            setting_noti_swithch_button.setChecked(false);
            setting_noti_textview.setText("알림 OFF");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        setting_noti_swithch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("알림", "켜기");
                    editor.apply();
                    setting_noti_textview.setText("알림 ON");

                } else {
                    SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("알림", "끄기");
                    editor.apply();

                    setting_noti_textview.setText("알림 OFF");

                }
            }
        });

    }
}