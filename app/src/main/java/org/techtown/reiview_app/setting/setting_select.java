package org.techtown.reiview_app.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.MainActivity;
import org.techtown.reiview_app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_select extends AppCompatActivity {

    Button setting_select_profile_button,setting_select_logout_button,setting_select_out_button,setting_noti_button;
    Dialog logout_dialog,out_dialog;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_select);

        setting_select_profile_button = (Button) findViewById(R.id.setting_select_profile_button);
        setting_select_logout_button = (Button) findViewById(R.id.setting_select_logout_button);
        setting_select_out_button = (Button) findViewById(R.id.setting_select_out_button);
        setting_noti_button = (Button) findViewById(R.id.setting_noti_button);


        logout_dialog = new Dialog(setting_select.this);       // Dialog 초기화
        logout_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        logout_dialog.setContentView(R.layout.logout_dialog);

        out_dialog = new Dialog(setting_select.this);       // Dialog 초기화
        out_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        out_dialog.setContentView(R.layout.out_dialog);

        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        setting_select_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_select.this, setting_update.class);
                startActivity(intent);
                finish();
            }
        });


        setting_noti_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_select.this, setting_noti.class);
                startActivity(intent);
            }
        });

        setting_select_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out_dialog.getWindow().setGravity(Gravity.CENTER);
                out_dialog.show();
                out_dialog.findViewById(R.id.out_ok_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.remove("이메일");
                        editor.remove("프로필이미지");
                        editor.remove("닉네임");
                        editor.apply();


                        SharedPreferences pref2 = getSharedPreferences("최근검색어"+email, MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = pref2.edit();
                        editor2.remove("검색어");
                        editor2.apply();


                        SharedPreferences pref3 = getSharedPreferences("home_select" + email, MODE_PRIVATE);
                        SharedPreferences.Editor editor3 = pref3.edit();
                        editor3.remove("번호");
                        editor3.apply();

                        setting_select_out(email);

                        Intent intent = new Intent(setting_select.this, MainActivity.class);
                        /*로그아웃을 할때 전에 사용한 액티비티는 전부 삭제하게 된다*/
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });


                out_dialog.findViewById(R.id.out_cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        out_dialog.dismiss();
                    }
                });
            }
        });


        setting_select_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout_dialog.getWindow().setGravity(Gravity.CENTER);
                logout_dialog.show();
                logout_dialog.findViewById(R.id.logout_ok_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = pref.edit();
                        editor2.remove("이메일");
                        editor2.apply();

                        Intent intent = new Intent(setting_select.this, MainActivity.class);
                        /*로그아웃을 할때 전에 사용한 액티비티는 전부 삭제하게 된다다*/
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });


                logout_dialog.findViewById(R.id.logout_cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logout_dialog.dismiss();
                    }
                });
            }
        });
    }

    private void setting_select_out(String email) {
        ApiInterface search_delete = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_delete.setting_select_out(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}