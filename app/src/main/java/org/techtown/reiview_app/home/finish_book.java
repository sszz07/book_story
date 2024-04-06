package org.techtown.reiview_app.home;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.MainActivity2;
import org.techtown.reiview_app.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class finish_book extends AppCompatActivity {
    RatingBar finish_book_rating_bar;
    EditText finish_book_edittext;
    TextView finish_book_text_number_textview;
    Button finish_book_ok_button;
    String content, email,read_page_intent;
    float star;
    int today_read_time, all_time_read_time;
    String book_subject, start_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_book);

        finish_book_rating_bar = (RatingBar) findViewById(R.id.finish_book_rating_bar);
        finish_book_edittext = (EditText) findViewById(R.id.finish_book_edittext);
        finish_book_text_number_textview = (TextView) findViewById(R.id.finish_book_text_number_textview);
        finish_book_ok_button = (Button) findViewById(R.id.finish_book_ok_button);


        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");


        Intent intent_get_data = getIntent();
        today_read_time = intent_get_data.getIntExtra("today_read_time", 0);
        all_time_read_time = intent_get_data.getIntExtra("all_time_read_time", 0);
        book_subject = intent_get_data.getStringExtra("book_name");
        start_day = intent_get_data.getStringExtra("start_day");
        read_page_intent = intent_get_data.getStringExtra("read_page");
int read_page_intent_int = Integer.valueOf(read_page_intent);

        //블로그 이름설정
        finish_book_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 100) {
                    finish_book_text_number_textview.setTextColor(Color.parseColor("#FF4646"));
                } else if (editable.length() <= 100) {
                    finish_book_text_number_textview.setTextColor(Color.BLACK);
                }

                String editable_string = String.valueOf(editable.length());
                finish_book_text_number_textview.setText(editable_string);
            }
        });


        finish_book_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = finish_book_edittext.getText().toString();
                star = finish_book_rating_bar.getRating();

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");

                //이미지에 붙일 시간 값 이미지 재사용 위해서
                Date date = new Date();
                start_day = simpleDateFormat.format(date);
                System.out.println(start_day + "start_daystart_daystart_day");


                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

                //이미지에 붙일 시간 값 이미지 재사용 위해서
                Date date2 = new Date();
                String start_day2 = simpleDateFormat2.format(date2);
                System.out.println(start_day + "start_daystart_daystart_day");

                if (content.length() == 0) {
                    Toast.makeText(finish_book.this, "한 줄평을 써주세요", Toast.LENGTH_SHORT).show();
                } else if (content.length() > 100) {
                    Toast.makeText(finish_book.this, "100자 이하로 적어주세요", Toast.LENGTH_SHORT).show();
                } else {
                    read_date_insert(book_subject, today_read_time, email, start_day2);
                    finish_read_info_update(book_subject, email, star, content, start_day, all_time_read_time,read_page_intent_int);
                }
                star = finish_book_rating_bar.getRating();
            }
        });

        //finish쪽 update하고 완료,마지막 시간값,점수값,나의 한줄평 넣기
    }


    private void read_date_insert(String book_subject, int today_read_time, String email, String start_day) {
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_info_insert.read_date_insert(book_subject, today_read_time, email, start_day);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }


    private void finish_read_info_update(String book_subject, String email, float book_star, String my_content, String finish, int all_time_read_time,int read_page_intent) {
        long present_time = System.currentTimeMillis();
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_info_insert.finish_book(book_subject, email, book_star, my_content, finish, all_time_read_time, present_time,read_page_intent);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(finish_book.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
                Intent intent = new Intent(finish_book.this, MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });
    }
}