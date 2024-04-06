package org.techtown.reiview_app.setting;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.home.book_read_start;
import org.techtown.reiview_app.home.book_time_adapter;
import org.techtown.reiview_app.home.home_data;
import org.techtown.reiview_app.home.home_select;
import org.techtown.reiview_app.home.memo_adapter;
import org.techtown.reiview_app.home.memo_plus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_finish_select extends AppCompatActivity {

    TextView setting_finish_select_book_subject_textview, setting_finish_select_book_writer_textview, setting_finish_select_book_make_textview, setting_finish_select_book_record_textview, setting_finish_select_book_memo_textview;
    TextView setting_finish_select_book_record_line_textview, setting_finish_select_book_memo_line_textview, setting_finish_select_book_record_not_textview, setting_finish_select_book_memo_not_textview, setting_finish_select_book_read_time_textview, setting_finish_select_my_content_textview, setting_finish_select_update_textveiw;
    RecyclerView setting_finish_select_book_record_recyclerview, setting_finish_select_book_memo_recyclerview;
    ImageView setting_finish_select_book_imageview, setting_finish_select_update;
    String book_subject, make, writer, image, email, my_content;
    Button  setting_finish_select_book_memo_plus_button,setting_finish_select_book_start_button, setting_finish_select_book_stop_button;
    Dialog dialog_update_delete;
    List<home_data> list_memo = new ArrayList<>();
    List<home_data> list_book_time = new ArrayList<>();
    memo_adapter adapter;
    book_time_adapter adapter_book_time;
    Dialog book_timer_dialog;
    int page = 0, limit = 10, read_time;
    NestedScrollView nestedScrollView;
    ProgressBar progress_bar_setting_finish_select;
    String read_page, book_page,finish,번호;
    float star;
    RatingBar setting_finish_select_rating_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_finish_select);


        Intent intent = getIntent();
        book_subject = intent.getStringExtra("book_subject");
        make = intent.getStringExtra("make");
        writer = intent.getStringExtra("author");
        image = intent.getStringExtra("image");
        my_content = intent.getStringExtra("my_content");
        finish = intent.getStringExtra("finish");
        read_time = intent.getIntExtra("read_time", 0);
        star = intent.getFloatExtra("star", 0);

        dialog_update_delete = new Dialog(setting_finish_select.this);       // Dialog 초기화
        dialog_update_delete.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog_update_delete.setContentView(R.layout.update_delete_dialog);

        setting_finish_select_book_read_time_textview = (TextView) findViewById(R.id.setting_finish_select_book_read_time_textview);


        setting_finish_select_my_content_textview = (TextView) findViewById(R.id.setting_finish_select_my_content_textview);


        setting_finish_select_update_textveiw = (TextView) findViewById(R.id.setting_finish_select_update_textveiw);

        setting_finish_select_book_subject_textview = (TextView) findViewById(R.id.setting_finish_select_book_subject_textview);
        setting_finish_select_book_writer_textview = (TextView) findViewById(R.id.setting_finish_select_book_writer_textview);
        setting_finish_select_book_make_textview = (TextView) findViewById(R.id.setting_finish_select_book_make_textview);
        setting_finish_select_book_record_textview = (TextView) findViewById(R.id.setting_finish_select_book_record_textview);
        setting_finish_select_book_memo_textview = (TextView) findViewById(R.id.setting_finish_select_book_memo_textview);
        setting_finish_select_book_record_line_textview = (TextView) findViewById(R.id.setting_finish_select_book_record_line_textview);
        setting_finish_select_book_memo_line_textview = (TextView) findViewById(R.id.setting_finish_select_book_memo_line_textview);

        setting_finish_select_book_record_not_textview = (TextView) findViewById(R.id.setting_finish_select_book_record_not_textview);
        setting_finish_select_book_memo_not_textview = (TextView) findViewById(R.id.setting_finish_select_book_memo_not_textview);

        setting_finish_select_book_record_recyclerview = (RecyclerView) findViewById(R.id.setting_finish_select_book_record_recyclerview);
        setting_finish_select_book_memo_recyclerview = (RecyclerView) findViewById(R.id.setting_finish_select_book_memo_recyclerview);

        setting_finish_select_book_imageview = (ImageView) findViewById(R.id.setting_finish_select_book_imageview);
        setting_finish_select_rating_bar = (RatingBar) findViewById(R.id.setting_finish_select_rating_bar);
        setting_finish_select_book_memo_plus_button = (Button) findViewById(R.id.setting_finish_select_book_memo_plus_button);
        progress_bar_setting_finish_select = (ProgressBar) findViewById(R.id.progress_bar_setting_finish_select);
        setting_finish_select_book_memo_plus_button.setVisibility(View.INVISIBLE);

        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");


        SharedPreferences pre = getSharedPreferences("setting_finish_select" + email, MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pre.edit();
        editor2.remove("번호");
        editor2.apply();

        setting_finish_select_book_record_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setting_finish_select_book_memo_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setting_finish_select_book_memo_recyclerview.setVisibility(View.GONE);

        int hrs = read_time / 3600;
        int mins = (read_time % 3600) / 60;
        int sec = read_time % 60;
        String time_string = String.format("%02d:%02d:%02d", hrs, mins, sec);


        setting_finish_select_book_subject_textview.setText(book_subject);
        setting_finish_select_book_writer_textview.setText(writer + " |");
        setting_finish_select_book_make_textview.setText(make);
        setting_finish_select_book_read_time_textview.setText(time_string);
        setting_finish_select_rating_bar.setRating(star);
        setting_finish_select_my_content_textview.setText(my_content);

        SharedPreferences pref2 = getSharedPreferences(book_subject+"update", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref2.edit();
        editor.putString("my_content", my_content);
        editor.putFloat("book_star", star);
        editor.apply();


        Glide.with(this)
                .load(image)
                .into(setting_finish_select_book_imageview);

        setting_finish_select_book_memo_not_textview.setVisibility(View.INVISIBLE);
        setting_finish_select_book_memo_textview.setTextColor(Color.parseColor("#828282"));
        setting_finish_select_book_memo_line_textview.setTextColor(Color.parseColor("#828282"));



        book_time_recyclerview_select(email, book_subject, page, limit);
        setting_finish_select_book_record_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("setting_finish_select" + email, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("번호", "1");
                editor.apply();

                번호 = pref.getString("번호", "");

                if (번호.equals("1")) {
                    book_time_recyclerview_select(email, book_subject, page, limit);
                    setting_finish_select_book_record_recyclerview.setVisibility(View.VISIBLE);
                    setting_finish_select_book_memo_recyclerview.setVisibility(View.GONE);
                    setting_finish_select_book_memo_plus_button.setVisibility(View.INVISIBLE);
                    setting_finish_select_book_memo_not_textview.setVisibility(View.INVISIBLE);
                    setting_finish_select_book_record_textview.setTextColor(Color.BLACK);
                    setting_finish_select_book_record_line_textview.setTextColor(Color.BLACK);
                    setting_finish_select_book_memo_textview.setTextColor(Color.parseColor("#828282"));
                    setting_finish_select_book_memo_line_textview.setTextColor(Color.parseColor("#828282"));
                }
            }
        });

        setting_finish_select_book_memo_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("setting_finish_select" + email, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("번호", "2");
                editor.apply();

                번호 = pref.getString("번호", "");

                if (번호.equals("2")) {
                    memo_recyclerview_select(email, book_subject, page, limit);
                    setting_finish_select_book_memo_plus_button.setVisibility(View.VISIBLE);
                    setting_finish_select_book_record_recyclerview.setVisibility(View.GONE);
                    setting_finish_select_book_memo_recyclerview.setVisibility(View.VISIBLE);
                    setting_finish_select_book_record_not_textview.setVisibility(View.INVISIBLE);
                    setting_finish_select_book_record_textview.setTextColor(Color.parseColor("#828282"));
                    setting_finish_select_book_record_line_textview.setTextColor(Color.parseColor("#828282"));
                    setting_finish_select_book_memo_textview.setTextColor(Color.BLACK);
                    setting_finish_select_book_memo_line_textview.setTextColor(Color.BLACK);
                }
            }
        });

        setting_finish_select_book_memo_plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_finish_select.this, memo_plus.class);
                intent.putExtra("book_subject", book_subject);
                startActivity(intent);
            }
        });
    }


    // INSERT 구문 은 WHERE 절을 지원하지 않으므로 쿼리가 실패합니다. id열이 고유하거나 기본 키 라고 가정합니다 .
    private void memo_recyclerview_select(String email, String book_subject, int page, int limit) {
        ApiInterface setting_finish_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = setting_finish_select.memo_recyclerview_select(email, book_subject, page, limit);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse( Call<List<home_data>> call,  Response<List<home_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onGetResult(response.body());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<home_data>> call, Throwable t) {
                Log.e("t_story_home_recyclerview_select()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult(List<home_data> lists) {
        if (lists.size() == 0) {
            setting_finish_select_book_memo_recyclerview.setVisibility(View.GONE);
            progress_bar_setting_finish_select.setVisibility(View.INVISIBLE);
        } else {
            setting_finish_select_book_memo_recyclerview.setVisibility(View.VISIBLE);
            adapter = new memo_adapter(this, lists, dialog_update_delete);
            adapter.notifyDataSetChanged();
            setting_finish_select_book_memo_recyclerview.setAdapter(adapter);
            list_memo = lists;
            progress_bar_setting_finish_select.setVisibility(View.INVISIBLE);
        }
    }

    // INSERT 구문 은 WHERE 절을 지원하지 않으므로 쿼리가 실패합니다. id열이 고유하거나 기본 키 라고 가정합니다 .
    private void book_time_recyclerview_select(String email, String book_subject, int page, int limit) {
        ApiInterface setting_finish_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = setting_finish_select.book_time_recyclerview_select(email, book_subject, page, limit);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse( Call<List<home_data>> call,  Response<List<home_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onGetResult2(response.body());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<home_data>> call, Throwable t) {
                Log.e("t_story_home_recyclerview_select()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult2(List<home_data> lists) {
        if (lists.size() == 0) {
            setting_finish_select_book_record_not_textview.setVisibility(View.VISIBLE);
            progress_bar_setting_finish_select.setVisibility(View.INVISIBLE);
        } else {
            progress_bar_setting_finish_select.setVisibility(View.INVISIBLE);
            setting_finish_select_book_record_not_textview.setVisibility(View.INVISIBLE);
            adapter_book_time = new book_time_adapter(this, lists);
            adapter_book_time.notifyDataSetChanged();
            setting_finish_select_book_record_recyclerview.setAdapter(adapter_book_time);
            list_book_time = lists;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences(book_subject + "update", MODE_PRIVATE);
        String my_content_shared = pref.getString("my_content", "");
        float book_star = pref.getFloat("book_star", 0);

        SharedPreferences pref2 = getSharedPreferences("setting_finish_select" + email, MODE_PRIVATE);
        번호 = pref2.getString("번호", "");
        if (!my_content_shared.equals("")) {
            setting_finish_select_rating_bar.setRating(book_star);
            setting_finish_select_my_content_textview.setText(my_content_shared);
        }

        setting_finish_select_update_textveiw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_finish_select.this, setting_update_finish.class);
                intent.putExtra("my_content", my_content_shared);
                intent.putExtra("star", book_star);
                intent.putExtra("book_subject", book_subject);
                intent.putExtra("read_time", read_time);
                intent.putExtra("finish", finish);
                startActivity(intent);
            }
        });


        if (번호.equals("2")) {
            memo_recyclerview_select(email, book_subject, page, limit);
            nestedScrollView = findViewById(R.id.scroll_view);
            progress_bar_setting_finish_select = findViewById(R.id.progress_bar_setting_finish_select);
            progress_bar_setting_finish_select.setVisibility(View.INVISIBLE);
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        limit += 10;
                        progress_bar_setting_finish_select.setVisibility(View.VISIBLE);
                        memo_recyclerview_select(email, book_subject, page, limit);
                    }
                }
            });
        }

        if (번호.equals("1")) {
            book_time_recyclerview_select(email, book_subject, page, limit);
            nestedScrollView = findViewById(R.id.scroll_view);
            progress_bar_setting_finish_select = findViewById(R.id.progress_bar_setting_finish_select);
            progress_bar_setting_finish_select.setVisibility(View.INVISIBLE);
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        limit += 10;
                        progress_bar_setting_finish_select.setVisibility(View.VISIBLE);
                        book_time_recyclerview_select(email, book_subject, page, limit);
                    }
                }
            });
        }
    }


}