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
import org.techtown.reiview_app.home.home_data;
import org.techtown.reiview_app.home.home_select;
import org.techtown.reiview_app.home.memo_adapter;
import org.techtown.reiview_app.home.memo_plus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_my_book_memo extends AppCompatActivity {
    TextView setting_book_memo_textview;
    Button setting_book_memo_plus_button;
    RecyclerView setting_book_memo_recyclerview;
    ProgressBar setting_book_memo_progress_bar;
    int page = 0, limit = 10;
    String email, book_subject;
    NestedScrollView nestedScrollView;


    List<home_data> list = new ArrayList<>();
    memo_adapter adapter;

    Dialog dialog_update_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_my_book_memo);


        setting_book_memo_recyclerview = (RecyclerView) findViewById(R.id.setting_book_memo_recyclerview);
        setting_book_memo_textview = (TextView) findViewById(R.id.setting_book_memo_textview);
        setting_book_memo_plus_button = (Button) findViewById(R.id.setting_book_memo_plus_button);
        setting_book_memo_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setting_book_memo_progress_bar = findViewById(R.id.setting_book_memo_progress_bar);


        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        Intent intent = getIntent();
        book_subject = intent.getStringExtra("book_subject");


        setting_book_memo_plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_my_book_memo.this, memo_plus.class);
                intent.putExtra("book_subject", book_subject);
                startActivity(intent);
            }
        });

        if (book_subject.length() > 22) {
            setting_book_memo_textview.setText(book_subject.substring(0, 22) + "....");
        } else {
            setting_book_memo_textview.setText(book_subject);
        }

        //라이크 유저 겟하기
        dialog_update_delete = new Dialog(setting_my_book_memo.this);       // Dialog 초기화
        dialog_update_delete.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog_update_delete.setContentView(R.layout.update_delete_dialog);


    }


    private void memo_recyclerview_select(String email, String book_subject, int page, int limit) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.memo_recyclerview_select(email, book_subject, page, limit);
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
        adapter = new memo_adapter(this, lists, dialog_update_delete);
        adapter.notifyDataSetChanged();
        setting_book_memo_recyclerview.setAdapter(adapter);
        list = lists;
        setting_book_memo_progress_bar.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        memo_recyclerview_select(email, book_subject, page, limit);
        nestedScrollView = findViewById(R.id.setting_book_memo_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    limit += 10;
                    setting_book_memo_progress_bar.setVisibility(View.VISIBLE);
                    memo_recyclerview_select(email, book_subject, page, limit);
                }
            }
        });
    }
}