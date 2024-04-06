package org.techtown.reiview_app.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.home.home_data;
import org.techtown.reiview_app.home.home_select;
import org.techtown.reiview_app.home.memo_adapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_my_memo extends AppCompatActivity {
    RecyclerView setting_my_memo_recyclerview;
    TextView setting_my_memo_not_textview;
    String email;


    List<home_data> list = new ArrayList<>();
    setting_my_memo_adapter adapter;
    setting_my_memo_adapter.ItemClickListener itemClickListener;

    int page = 0, limit = 10;
    NestedScrollView nestedScrollView;
    ProgressBar setting_my_memo_progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_my_memo);


        setting_my_memo_recyclerview = (RecyclerView) findViewById(R.id.setting_my_memo_recyclerview);
        setting_my_memo_not_textview = (TextView) findViewById(R.id.setting_my_memo_not_textview);
        setting_my_memo_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setting_my_memo_progress_bar = findViewById(R.id.setting_my_memo_progress_bar);

        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");


        itemClickListener = new setting_my_memo_adapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String book_subject = list.get(position).getBook_subject();
                System.out.println(book_subject + "book_subjectbook_subject");

                Intent intent = new Intent(setting_my_memo.this, setting_my_book_memo.class);
                intent.putExtra("book_subject", book_subject);
                startActivity(intent);
            }
        };




    }


    private void setting_memo_recyclerview_select(String email, int page, int limit) {
        System.out.println(email + "이메일값 확인하기");
        ApiInterface setting_finish_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = setting_finish_select.setting_memo_recyclerview_select(email,page,limit);
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
        if (lists.size() == 0) {
            setting_my_memo_not_textview.setVisibility(View.VISIBLE);
            setting_my_memo_recyclerview.setVisibility(View.GONE);

        } else {
            setting_my_memo_progress_bar.setVisibility(View.INVISIBLE);
            setting_my_memo_recyclerview.setVisibility(View.VISIBLE);
            setting_my_memo_not_textview.setVisibility(View.GONE);

            list = lists;
            adapter = new setting_my_memo_adapter(this, lists, itemClickListener);
            adapter.notifyDataSetChanged();
            setting_my_memo_recyclerview.setAdapter(adapter);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setting_memo_recyclerview_select(email, page, limit);
        nestedScrollView = findViewById(R.id.setting_my_memo_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    limit += 10;
                    setting_my_memo_progress_bar.setVisibility(View.VISIBLE);
                    setting_memo_recyclerview_select(email, page, limit);
                }
            }
        });
    }


}