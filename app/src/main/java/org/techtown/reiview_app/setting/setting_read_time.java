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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.home.home_data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_read_time extends AppCompatActivity {

    RecyclerView setting_read_time_recyclerview;
    TextView setting_read_time_not_textview;
    String email;

    Button setting_read_time_menu_button;

    List<home_data> list = new ArrayList<>();
    setting_my_memo_adapter adapter;
    setting_my_memo_adapter.ItemClickListener itemClickListener;

    int page = 0, limit = 10;
    NestedScrollView nestedScrollView;
    ProgressBar setting_read_time_progress_bar;
    String 번호 = "1";

    Dialog setting_read_book_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_read_time);


        setting_read_time_recyclerview = (RecyclerView) findViewById(R.id.setting_read_time_recyclerview);
        setting_read_time_not_textview = (TextView) findViewById(R.id.setting_read_time_not_textview);
        setting_read_time_menu_button = (Button) findViewById(R.id.setting_read_time_menu_button);

        setting_read_time_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setting_read_time_progress_bar = findViewById(R.id.setting_read_time_progress_bar);

        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");
        setting_read_time_recyclerview_select(email, page, limit, "최근독서순");
        setting_read_time_menu_button.setText("최근 독서순▽");


        itemClickListener = new setting_my_memo_adapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String book_subject = list.get(position).getBook_subject();
                int read_time = list.get(position).getRead_time();
                System.out.println(book_subject + "book_subjectbook_subject");

                Intent intent = new Intent(setting_read_time.this, setting_book_read_time.class);
                intent.putExtra("book_subject", book_subject);
                intent.putExtra("read_time", read_time);
                startActivity(intent);
            }
        };
    }


    private void setting_read_time_recyclerview_select(String email, int page, int limit, String choise) {
        System.out.println(email + "이메일값 확인하기");
        ApiInterface setting_finish_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = setting_finish_select.setting_read_time_recyclerview_select(email, page, limit, choise);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    String book_subject = response.body().get(0).getBook_subject();
                    String book_subject2 = response.body().get(1).getBook_subject();
                    String book_subject3 = response.body().get(2).getBook_subject();

                    System.out.println(book_subject + "book_subject");
                    System.out.println(book_subject2 + "book_subject2");
                    System.out.println(book_subject3 + "book_subject");


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
            setting_read_time_not_textview.setVisibility(View.VISIBLE);
            setting_read_time_recyclerview.setVisibility(View.GONE);

        } else {
            setting_read_time_progress_bar.setVisibility(View.INVISIBLE);
            setting_read_time_recyclerview.setVisibility(View.VISIBLE);
            setting_read_time_not_textview.setVisibility(View.GONE);

            list = lists;
            adapter = new setting_my_memo_adapter(this, lists, itemClickListener);
            adapter.notifyDataSetChanged();
            setting_read_time_recyclerview.setAdapter(adapter);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setting_read_book_dialog = new Dialog(this);       // Dialog 초기화
        setting_read_book_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        setting_read_book_dialog.setContentView(R.layout.setting_read_book_dialog);



        setting_read_time_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting_read_book_dialog.getWindow().setGravity(Gravity.CENTER);
                setting_read_book_dialog.show();


                Button button = (Button) setting_read_book_dialog.findViewById(R.id.setting_read_book_last_read_button);
                Button button2 = (Button) setting_read_book_dialog.findViewById(R.id.setting_read_book_more_read_button);

                setting_read_book_dialog.findViewById(R.id.setting_read_book_last_read_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        SharedPreferences pref4 = getSharedPreferences("setting_read_time" + email, MODE_PRIVATE);
//                        SharedPreferences.Editor editor = pref4.edit();
//                        editor.putString("번호", "1");
//                        editor.apply();


                        번호 = "1";

                        setting_read_time_recyclerview_select(email, page, limit, "최근독서순");
                        button.setTextColor(Color.parseColor("#828282"));
                        button2.setTextColor(Color.BLACK);
                        setting_read_time_menu_button.setText("최근 독서순▽");
                        setting_read_book_dialog.dismiss();
                    }
                });


                setting_read_book_dialog.findViewById(R.id.setting_read_book_more_read_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences pref4 = getSharedPreferences("setting_read_time" + email, MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref4.edit();
                        editor.putString("번호", "2");
                        editor.apply();

                        번호 = "2";

                        setting_read_time_recyclerview_select(email, page, limit, "독서시간순");
                        button.setTextColor(Color.BLACK);
                        button2.setTextColor(Color.parseColor("#828282"));

                        setting_read_time_menu_button.setText("독서 시간순▽");
                        setting_read_book_dialog.dismiss();
                    }
                });
            }
        });
    }
}