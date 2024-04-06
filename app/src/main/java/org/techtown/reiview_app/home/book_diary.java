package org.techtown.reiview_app.home;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class book_diary extends AppCompatActivity {
    String book_subject,email,book_page;
    TextView book_diary_plus_textview;
    RecyclerView book_diary_plus_recyclerview;
    int page = 0, limit = 10;
    NestedScrollView nestedScrollView;
    ProgressBar progress_bar_book_diary;
    List<home_data> list = new ArrayList<>();
    book_diary_adapter adapter;
    book_diary_adapter.ItemClickListener itemClickListener;
    Dialog dialog_update_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_diary);

        book_diary_plus_textview = (TextView) findViewById(R.id.book_diary_plus_textview);
        book_diary_plus_recyclerview = (RecyclerView) findViewById(R.id.book_diary_plus_recyclerview);
        progress_bar_book_diary = findViewById(R.id.progress_bar_book_diary);

        //라이크 유저 겟하기
        dialog_update_delete = new Dialog(book_diary.this);       // Dialog 초기화
        dialog_update_delete.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog_update_delete.setContentView(R.layout.update_delete_dialog);

        Intent intent = getIntent();
        book_subject = intent.getStringExtra("book_subject");
        book_page = intent.getStringExtra("book_page");

        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        book_diary_plus_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        book_diary_plus_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_diary.this, book_diary_plus.class);
                intent.putExtra("book_subject", book_subject);
                intent.putExtra("book_page", book_page);
                startActivity(intent);
            }
        });


        //리사이클러뷰 아이템 클릭하기
        itemClickListener = new book_diary_adapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int idx = list.get(position).getIdx();

                Log.e("idx 데이터 확인", String.valueOf(idx));

                Intent intent = new Intent(book_diary.this, book_diary_select.class);
                intent.putExtra("idx", idx);
                startActivity(intent);
            }
        };
    }


    private void book_diary_select(String email, String book_subject, int page, int limit) {
        System.out.println(email+"email"+book_subject+"book_subject");//값확인
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_diary_select(email, book_subject, page, limit);
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
                Log.e("book_diary_select()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult(List<home_data> lists) {
        if (lists.size() == 0) {
            //디비에 메모의 값이 없다면 리사이클러뷰 가리기
        } else {
            adapter = new book_diary_adapter(this, lists,itemClickListener,dialog_update_delete);
            adapter.notifyDataSetChanged();
            book_diary_plus_recyclerview.setAdapter(adapter);
            list = lists;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        book_diary_select(email, book_subject, page, limit);

        nestedScrollView = findViewById(R.id.scroll_view);
        progress_bar_book_diary.setVisibility(View.INVISIBLE);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    limit += 10;
                    progress_bar_book_diary.setVisibility(View.VISIBLE);
                    System.out.println(email+"email"+book_subject+"book_subject");//값확인
                    book_diary_select(email, book_subject, page, limit);
                }
            }
        });
    }
}