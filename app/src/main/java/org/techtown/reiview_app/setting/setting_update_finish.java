package org.techtown.reiview_app.setting;

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
import org.techtown.reiview_app.home.finish_book;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_update_finish extends AppCompatActivity {
    RatingBar finish_book_update_rating_bar;
    EditText finish_book_update_edittext;
    TextView finish_book_update_number_textview;
    Button finish_book_update_ok_button;
    String email, my_content, book_subject,finish;
    float star;
    int read_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_update_finish);

        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        finish_book_update_rating_bar = (RatingBar) findViewById(R.id.finish_book_update_rating_bar);
        finish_book_update_edittext = (EditText) findViewById(R.id.finish_book_update_edittext);
        finish_book_update_number_textview = (TextView) findViewById(R.id.finish_book_update_number_textview);
        finish_book_update_ok_button = (Button) findViewById(R.id.finish_book_update_ok_button);


        Intent intent_get_data = getIntent();
        star = intent_get_data.getFloatExtra("star", 0);
        my_content = intent_get_data.getStringExtra("my_content");
        book_subject = intent_get_data.getStringExtra("book_subject");
        finish = intent_get_data.getStringExtra("finish");
        read_time = intent_get_data.getIntExtra("read_time", 0);


        finish_book_update_edittext.setText(my_content);
        finish_book_update_rating_bar.setRating(star);


        finish_book_update_number_textview.setText(String.valueOf(my_content.length()));

        //블로그 이름설정
        finish_book_update_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 100) {
                    finish_book_update_number_textview.setTextColor(Color.parseColor("#FF4646"));
                } else if (editable.length() <= 100) {
                    finish_book_update_number_textview.setTextColor(Color.BLACK);
                }

                String editable_string = String.valueOf(editable.length());
                finish_book_update_number_textview.setText(editable_string);
            }
        });


        finish_book_update_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_content = finish_book_update_edittext.getText().toString();
                star = finish_book_update_rating_bar.getRating();
                if (my_content.length() == 0) {
                    Toast.makeText(setting_update_finish.this, "한 줄평을 써주세요", Toast.LENGTH_SHORT).show();
                } else if(my_content.length() > 100) {
                    Toast.makeText(setting_update_finish.this, "100자를 넘겼습니다", Toast.LENGTH_SHORT).show();

                }else{
                    finish_read_info_update(book_subject, email, star, my_content, finish, read_time);

                }
            }
        });

    }


    private void finish_read_info_update(String book_subject, String email, float book_star, String my_content, String finish, int all_time_read_time) {
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_info_insert.finish_read_info_update(book_subject, email, book_star, my_content, finish, all_time_read_time);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call,  Response<String> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    SharedPreferences pref = getSharedPreferences(book_subject+"update", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("my_content", my_content);
                    editor.putFloat("book_star", book_star);
                    editor.apply();
                    onBackPressed();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure( Call<String> call,  Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
                SharedPreferences pref = getSharedPreferences(book_subject+"update", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("my_content", my_content);
                editor.putFloat("book_star", book_star);
                editor.apply();
                onBackPressed();

            }
        });
    }
}