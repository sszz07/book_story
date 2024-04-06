package org.techtown.reiview_app.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.home.book_time_adapter;
import org.techtown.reiview_app.home.home_data;
import org.techtown.reiview_app.home.memo_adapter;
import org.techtown.reiview_app.home.memo_plus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_book_read_time extends AppCompatActivity {
    TextView setting_book_read_time_subject_textview, setting_book_read_time_textview;
    RecyclerView setting_book_read_time_recyclerview;
    ProgressBar setting_book_read_time_progress_bar;
    int page = 0, limit = 10;
    String email, book_subject;
    NestedScrollView nestedScrollView;
    int read_time;

    List<home_data> list = new ArrayList<>();
    book_time_adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_book_read_time);


        setting_book_read_time_recyclerview = (RecyclerView) findViewById(R.id.setting_book_read_time_recyclerview);
        setting_book_read_time_subject_textview = (TextView) findViewById(R.id.setting_book_read_time_subject_textview);
        setting_book_read_time_textview = (TextView) findViewById(R.id.setting_book_read_time_textview);

        setting_book_read_time_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setting_book_read_time_progress_bar = findViewById(R.id.setting_book_read_time_progress_bar);


        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        Intent intent = getIntent();
        book_subject = intent.getStringExtra("book_subject");
        read_time = intent.getIntExtra("read_time", 0);

        int hrs = read_time / 3600;
        int mins = (read_time % 3600) / 60;
        int sec = read_time % 60;
        String time = String.format("%02d:%02d:%02d", hrs, mins, sec);

        setting_book_read_time_textview.setText("총 - " + time);
        if (book_subject.length() > 22) {
            setting_book_read_time_subject_textview.setText(book_subject.substring(0, 22) + "....");
        } else {
            setting_book_read_time_subject_textview.setText(book_subject);
        }

    }


    private void setting_book_read_time_recyclerview_select(String email, String book_subject, int page, int limit) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_time_recyclerview_select(email, book_subject, page, limit);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
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
        adapter = new book_time_adapter(this, lists);
        adapter.notifyDataSetChanged();
        setting_book_read_time_recyclerview.setAdapter(adapter);
        list = lists;
        setting_book_read_time_progress_bar.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setting_book_read_time_recyclerview_select(email, book_subject, page, limit);
        nestedScrollView = findViewById(R.id.setting_book_read_time_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    limit += 10;
                    setting_book_read_time_progress_bar.setVisibility(View.VISIBLE);
                    setting_book_read_time_recyclerview_select(email, book_subject, page, limit);
                }
            }
        });
    }
}