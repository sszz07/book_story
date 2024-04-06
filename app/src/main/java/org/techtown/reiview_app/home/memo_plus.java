package org.techtown.reiview_app.home;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class memo_plus extends AppCompatActivity {
    TextView memo_plus_all_count_textview, memo_plus_count_textview;
    EditText memo_plus_edittext;
    Button memo_plus_cancel_button, memo_plus_plus_button;
    String email, book_subject, memo_eidttext;
    Dialog memo_stop_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_plus);

        Intent intent = getIntent();
        book_subject = intent.getStringExtra("book_subject");


        memo_stop_dialog = new Dialog(memo_plus.this);       // Dialog 초기화
        memo_stop_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        memo_stop_dialog.setContentView(R.layout.memo_dialog);

        memo_plus_all_count_textview = (TextView) findViewById(R.id.memo_plus_all_count_textview);
        memo_plus_count_textview = (TextView) findViewById(R.id.memo_plus_count_textview);
        memo_plus_edittext = (EditText) findViewById(R.id.memo_plus_edittext);
        memo_plus_cancel_button = (Button) findViewById(R.id.memo_plus_cancel_button);
        memo_plus_plus_button = (Button) findViewById(R.id.memo_plus_plus_button);

        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        //글자수 넘겼을때
        memo_plus_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable ewditable) {

                String ewditable_string = String.valueOf(ewditable.length());
                memo_plus_count_textview.setText(ewditable_string);

                if (ewditable.length() > 200) {
                    memo_plus_count_textview.setTextColor(Color.parseColor("#FF5050"));
                } else if (ewditable.length() <= 200) {
                    memo_plus_count_textview.setTextColor(Color.BLACK);
                }
            }
        });

        memo_plus_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memo_stop_dialog.show(); // 다이얼로그 띄우기
                /* 이 함수 안에 원하는 디자인과 기능을 구현하면 된다. */


                Button memo_stop_button = memo_stop_dialog.findViewById(R.id.memo_stop_button);
                memo_stop_button.findViewById(R.id.memo_stop_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      onBackPressed();
                    }
                });


                Button memo_stop_cancel_button = memo_stop_dialog.findViewById(R.id.memo_stop_cancel_button);
                memo_stop_cancel_button.findViewById(R.id.memo_stop_cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        memo_stop_dialog.dismiss();
                    }
                });
            }
        });

        memo_plus_plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long 현재시간 = System.currentTimeMillis();
                memo_eidttext = memo_plus_edittext.getText().toString();

                if(memo_eidttext.length() == 0){
                    Toast.makeText(memo_plus.this, "메모를 입력해주세요", Toast.LENGTH_SHORT).show();
                }else if(memo_eidttext.length() > 200){
                    Toast.makeText(memo_plus.this, "200자가 넘었습니다", Toast.LENGTH_SHORT).show();
                }else{
                    memo_info_insert(email,book_subject,memo_eidttext);
                }

            }
        });
    }


    private void memo_info_insert(String email,String book_subject,String memo) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = sdf.format(date);
        System.out.println(getTime+"getTimegetTime");
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_info_insert.memo_info_insert(email,book_subject,memo,getTime);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call,  Response<String> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onBackPressed();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure( Call<String> call,  Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
                onBackPressed();
            }
        });
    }

}