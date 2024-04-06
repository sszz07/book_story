package org.techtown.reiview_app.home;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.search.search_data;
import org.techtown.reiview_app.search.search_result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class home_select extends AppCompatActivity {
    TextView home_select_book_subject_textview, home_select_book_writer_textview, home_select_book_make_textview, home_select_book_record_textview, home_select_book_memo_textview;
    TextView home_select_book_record_line_textview, home_select_book_memo_line_textview, home_select_book_record_not_textview, home_select_book_memo_not_textview, home_select_book_read_time_textview, home_select_book_read_page_textview;
    RecyclerView home_select_book_record_recyclerview, home_select_book_memo_recyclerview;
    ImageView home_select_book_imageview;
    String book_subject, make, writer, image, email;
    Button home_select_book_memo_plus_button, home_select_book_start_button, home_select_book_stop_button;
    Dialog book_stop_dialog, dialog_update_delete, dialog_book_time;
    List<home_data> list_memo = new ArrayList<>();
    List<home_data> list_book_time = new ArrayList<>();
    memo_adapter adapter;
    book_time_adapter adapter_book_time;
    Dialog book_timer_dialog;
    int page = 0, limit = 10, read_time;
    int time_setting = 600;
    NestedScrollView nestedScrollView, nestedScrollView2;
    ProgressBar progress_bar_home_select;
    String read_page, book_page, 상태, 번호;
    TextView home_select_book_time_textview;
    Button home_select_book_time_change_button,home_select_book_diary_button;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_select);
        Intent intent = getIntent();
        book_subject = intent.getStringExtra("book_subject");
        make = intent.getStringExtra("make");
        writer = intent.getStringExtra("writer");
        image = intent.getStringExtra("image");
        book_page = intent.getStringExtra("book_page");
        read_page = intent.getStringExtra("read_page");
        read_time = intent.getIntExtra("read_time", 0);
        상태 = intent.getStringExtra("상태");

        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        SharedPreferences pref2 = getSharedPreferences(email + "noti", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref2.edit();
        editor2.remove("book_name");
        editor2.remove("독서시간");
        editor2.apply();


        //기록에서 들어왔을때
        book_recyclerview_select(email, book_subject);
        try {
            SharedPreferences pref3 = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
            String book_subject_shared = pref3.getString(book_subject + "book_name", "");
            System.out.println(book_subject_shared + "book_subject_shared");
            int time_shared = pref3.getInt("나간시간", 0);
            if (time_shared != 0) {
                //라이크 유저 겟하기
                book_timer_dialog = new Dialog(home_select.this);       // Dialog 초기화
                book_timer_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                book_timer_dialog.setContentView(R.layout.book_timer_dialog);

                dialog_shared(time_shared);
            }
        } catch (IllegalArgumentException e) {

        }


        dialog_book_time = new Dialog(home_select.this);       // Dialog 초기화
        dialog_book_time.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog_book_time.setContentView(R.layout.dialog_book_time);


        //라이크 유저 겟하기
        dialog_update_delete = new Dialog(home_select.this);       // Dialog 초기화
        dialog_update_delete.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog_update_delete.setContentView(R.layout.update_delete_dialog);

        book_stop_dialog = new Dialog(home_select.this);       // Dialog 초기화
        book_stop_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        book_stop_dialog.setContentView(R.layout.book_stop_dialog);


        home_select_book_read_time_textview = (TextView) findViewById(R.id.home_select_book_read_time_textview);
        home_select_book_read_page_textview = (TextView) findViewById(R.id.home_select_book_read_page_textview);


        //시간 버튼
        home_select_book_time_textview = (TextView) findViewById(R.id.home_select_book_time_textview);
        home_select_book_time_change_button = (Button) findViewById(R.id.home_select_book_time_change_button);
        home_select_book_diary_button = (Button) findViewById(R.id.home_select_book_diary_button);


        //책 일기 쓰려고 책 제목값 인텐트함
        home_select_book_diary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_select.this, book_diary.class);
                intent.putExtra("book_subject", book_subject);
                intent.putExtra("book_page", book_page);
                startActivity(intent);
            }
        });

        int hrs = read_time / 3600;
        int mins = (read_time % 3600) / 60;
        int sec = read_time % 60;
        String time = String.format("%02d:%02d:%02d", hrs, mins, sec);

        home_select_book_read_time_textview.setText(time);
        home_select_book_read_page_textview.setText(read_page + "/" + book_page + "p");
        progress_bar_home_select = findViewById(R.id.progress_bar_home_select);
        home_select_book_subject_textview = (TextView) findViewById(R.id.home_select_book_subject_textview);
        home_select_book_writer_textview = (TextView) findViewById(R.id.home_select_book_writer_textview);
        home_select_book_make_textview = (TextView) findViewById(R.id.home_select_book_make_textview);
        home_select_book_record_textview = (TextView) findViewById(R.id.home_select_book_record_textview);
        home_select_book_memo_textview = (TextView) findViewById(R.id.home_select_book_memo_textview);
        home_select_book_record_line_textview = (TextView) findViewById(R.id.home_select_book_record_line_textview);
        home_select_book_memo_line_textview = (TextView) findViewById(R.id.home_select_book_memo_line_textview);

        home_select_book_record_not_textview = (TextView) findViewById(R.id.home_select_book_record_not_textview);
        home_select_book_memo_not_textview = (TextView) findViewById(R.id.home_select_book_memo_not_textview);

        home_select_book_memo_plus_button = (Button) findViewById(R.id.home_select_book_memo_plus_button);
        home_select_book_start_button = (Button) findViewById(R.id.home_select_book_start_button);
        home_select_book_stop_button = (Button) findViewById(R.id.home_select_book_stop_button);

        home_select_book_record_recyclerview = (RecyclerView) findViewById(R.id.home_select_book_record_recyclerview);
        home_select_book_memo_recyclerview = (RecyclerView) findViewById(R.id.home_select_book_memo_recyclerview);

        home_select_book_imageview = (ImageView) findViewById(R.id.home_select_book_imageview);


        //처음으로 화면에 들어왔을때 메모 리사이클러뷰 가리기
        SharedPreferences pref4 = getSharedPreferences("home_select" + email, MODE_PRIVATE);
        번호 = pref4.getString("번호", "");


        if (번호.equals("1")) {
            home_select_book_memo_recyclerview.setVisibility(View.GONE);
            home_select_book_memo_plus_button.setVisibility(View.INVISIBLE);
            home_select_book_memo_textview.setTextColor(Color.parseColor("#828282"));
            home_select_book_memo_line_textview.setTextColor(Color.parseColor("#828282"));
        } else if (번호.equals("2")) {
            home_select_book_memo_recyclerview.setVisibility(View.VISIBLE);
            home_select_book_record_recyclerview.setVisibility(View.GONE);
            home_select_book_memo_plus_button.setVisibility(View.VISIBLE);
            home_select_book_record_textview.setTextColor(Color.parseColor("#828282"));
            home_select_book_record_line_textview.setTextColor(Color.parseColor("#828282"));
            home_select_book_memo_textview.setTextColor(Color.BLACK);
            home_select_book_memo_line_textview.setTextColor(Color.BLACK);
        }


        home_select_book_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time_setting == 600) {
                    SharedPreferences read_time_setting = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
                    SharedPreferences.Editor editor = read_time_setting.edit();
                    editor.putInt("독서시간", time_setting);
                    editor.apply();
                } else if (time_setting == 1200) {
                    SharedPreferences read_time_setting = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
                    SharedPreferences.Editor editor = read_time_setting.edit();
                    editor.putInt("독서시간", time_setting);
                    editor.apply();
                } else if (time_setting == 1800) {
                    SharedPreferences read_time_setting = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
                    SharedPreferences.Editor editor = read_time_setting.edit();
                    editor.putInt("독서시간", time_setting);
                    editor.apply();
                } else if (time_setting == 3600) {
                    SharedPreferences read_time_setting = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
                    SharedPreferences.Editor editor = read_time_setting.edit();
                    editor.putInt("독서시간", time_setting);
                    editor.apply();
                } else if (time_setting == 7200) {
                    SharedPreferences read_time_setting = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
                    SharedPreferences.Editor editor = read_time_setting.edit();
                    editor.putInt("독서시간", time_setting);
                    editor.apply();
                } else if (time_setting == -1) {
                    SharedPreferences read_time_setting = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
                    SharedPreferences.Editor editor = read_time_setting.edit();
                    editor.putInt("독서시간", time_setting);
                    editor.apply();
                }

                Intent intent = new Intent(home_select.this, book_read_start.class);
                intent.putExtra("book_subject", book_subject);
                intent.putExtra("book_page", book_page);
                intent.putExtra("image", image);
                startActivity(intent);
            }
        });

        //시간
        home_select_book_time_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_book_time.getWindow().setGravity(Gravity.CENTER);
                dialog_book_time.show();

                Button dialog_book_time_x_button = (Button) dialog_book_time.findViewById(R.id.dialog_book_time_x);
                Button dialog_book_time_ten_min_button = (Button) dialog_book_time.findViewById(R.id.dialog_book_time_ten_min);
                Button dialog_book_time_two_min_button = (Button) dialog_book_time.findViewById(R.id.dialog_book_time_two_min);
                Button dialog_book_time_three_min_button = (Button) dialog_book_time.findViewById(R.id.dialog_book_time_three_min);
                Button dialog_book_time_one_hour_button = (Button) dialog_book_time.findViewById(R.id.dialog_book_time_one_hour);
                Button dialog_book_time_two_hour_button = (Button) dialog_book_time.findViewById(R.id.dialog_book_time_two_hour);
                Button dialog_book_time_cancel_button = (Button) dialog_book_time.findViewById(R.id.dialog_book_time_cancel);


                //제한없이
                dialog_book_time_x_button.findViewById(R.id.dialog_book_time_x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        time_setting = -1;

                        dialog_book_time_x_button.setTextColor(Color.parseColor("#828282"));
                        dialog_book_time_ten_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_two_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_three_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_one_hour_button.setTextColor(Color.BLACK);
                        dialog_book_time_two_hour_button.setTextColor(Color.BLACK);
                        dialog_book_time_cancel_button.setTextColor(Color.BLACK);

                        home_select_book_time_textview.setText("제한없이");
                        dialog_book_time.dismiss();
                    }
                });

                //10분
                dialog_book_time_ten_min_button.findViewById(R.id.dialog_book_time_ten_min).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        time_setting = 600;

                        dialog_book_time_x_button.setTextColor(Color.BLACK);
                        dialog_book_time_ten_min_button.setTextColor(Color.parseColor("#828282"));
                        dialog_book_time_two_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_three_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_one_hour_button.setTextColor(Color.BLACK);
                        dialog_book_time_two_hour_button.setTextColor(Color.BLACK);
                        dialog_book_time_cancel_button.setTextColor(Color.BLACK);

                        home_select_book_time_textview.setText("10분");
                        dialog_book_time.dismiss();
                    }
                });


                //20분
                dialog_book_time_two_min_button.findViewById(R.id.dialog_book_time_two_min).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        time_setting = 1200;

                        dialog_book_time_x_button.setTextColor(Color.BLACK);
                        dialog_book_time_ten_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_two_min_button.setTextColor(Color.parseColor("#828282"));
                        dialog_book_time_three_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_one_hour_button.setTextColor(Color.BLACK);
                        dialog_book_time_two_hour_button.setTextColor(Color.BLACK);
                        dialog_book_time_cancel_button.setTextColor(Color.BLACK);

                        home_select_book_time_textview.setText("20분");
                        dialog_book_time.dismiss();
                    }
                });

                //30분
                dialog_book_time_three_min_button.findViewById(R.id.dialog_book_time_three_min).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        time_setting = 1800;

                        dialog_book_time_x_button.setTextColor(Color.BLACK);
                        dialog_book_time_ten_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_two_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_three_min_button.setTextColor(Color.parseColor("#828282"));
                        dialog_book_time_one_hour_button.setTextColor(Color.BLACK);
                        dialog_book_time_two_hour_button.setTextColor(Color.BLACK);
                        dialog_book_time_cancel_button.setTextColor(Color.BLACK);

                        home_select_book_time_textview.setText("30분");
                        dialog_book_time.dismiss();
                    }
                });

                //1시간
                dialog_book_time_one_hour_button.findViewById(R.id.dialog_book_time_one_hour).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        time_setting = 3600;


                        dialog_book_time_x_button.setTextColor(Color.BLACK);
                        dialog_book_time_x_button.setTextColor(Color.BLACK);
                        dialog_book_time_ten_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_two_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_three_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_one_hour_button.setTextColor(Color.parseColor("#828282"));
                        dialog_book_time_two_hour_button.setTextColor(Color.BLACK);
                        dialog_book_time_cancel_button.setTextColor(Color.BLACK);

                        home_select_book_time_textview.setText("1시간");
                        dialog_book_time.dismiss();
                    }
                });

                //2시간
                dialog_book_time_two_hour_button.findViewById(R.id.dialog_book_time_two_hour).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        time_setting = 7200;
                        dialog_book_time_x_button.setTextColor(Color.BLACK);
                        dialog_book_time_ten_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_two_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_three_min_button.setTextColor(Color.BLACK);
                        dialog_book_time_one_hour_button.setTextColor(Color.BLACK);
                        dialog_book_time_two_hour_button.setTextColor(Color.parseColor("#828282"));
                        dialog_book_time_cancel_button.setTextColor(Color.BLACK);

                        home_select_book_time_textview.setText("2시간");
                        dialog_book_time.dismiss();
                    }
                });

                //취소
                dialog_book_time_cancel_button.findViewById(R.id.dialog_book_time_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_book_time.dismiss();
                    }
                });
            }
        });

        home_select_book_record_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        home_select_book_memo_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        home_select_book_subject_textview.setText(book_subject);
        home_select_book_writer_textview.setText(writer + " |");
        home_select_book_make_textview.setText(make);


        Glide.with(this)
                .load(image)
                .into(home_select_book_imageview);


        home_select_book_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_stop_dialog();
            }
        });


        home_select_book_memo_plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_select.this, memo_plus.class);
                intent.putExtra("book_subject", book_subject);
                startActivity(intent);
            }
        });
    }


    private void home_select_book_stop(String email, String book_subject) {
        ApiInterface search_delete = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_delete.book_now_read_delete(email, book_subject);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                onBackPressed();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    public void book_stop_dialog() {
        book_stop_dialog.getWindow().setGravity(Gravity.CENTER);
        book_stop_dialog.show(); // 다이얼로그 띄우기
        /* 이 함수 안에 원하는 디자인과 기능을 구현하면 된다. */


        Button book_stop_button = book_stop_dialog.findViewById(R.id.book_stop_button);
        book_stop_button.findViewById(R.id.book_stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home_select_book_stop(email, book_subject);
            }
        });


        Button book_stop_cancel_button = book_stop_dialog.findViewById(R.id.book_stop_cancel_button);
        book_stop_cancel_button.findViewById(R.id.book_stop_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_stop_dialog.dismiss();
            }
        });
    }


    // INSERT 구문 은 WHERE 절을 지원하지 않으므로 쿼리가 실패합니다. id열이 고유하거나 기본 키 라고 가정합니다 .
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
        if (lists.size() == 0) {
            home_select_book_memo_not_textview.setText("메모가 없습니다");
            progress_bar_home_select.setVisibility(View.INVISIBLE);
            //디비에 메모의 값이 없다면 리사이클러뷰 가리기
            home_select_book_memo_recyclerview.setVisibility(View.GONE);
            home_select_book_record_not_textview.setVisibility(View.GONE);
            home_select_book_record_recyclerview.setVisibility(View.GONE);
        } else {
            home_select_book_memo_not_textview.setVisibility(View.GONE);
            home_select_book_memo_recyclerview.setVisibility(View.VISIBLE);
            adapter = new memo_adapter(this, lists, dialog_update_delete);
            adapter.notifyDataSetChanged();
            home_select_book_memo_recyclerview.setAdapter(adapter);
            list_memo = lists;
            progress_bar_home_select.setVisibility(View.INVISIBLE);
        }
    }

    // INSERT 구문 은 WHERE 절을 지원하지 않으므로 쿼리가 실패합니다. id열이 고유하거나 기본 키 라고 가정합니다 .
    private void book_time_recyclerview_select(String email, String book_subject, int page, int limit) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_time_recyclerview_select(email, book_subject, page, limit);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
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
            home_select_book_record_not_textview.setText("독서기록이 없습니다");

            progress_bar_home_select.setVisibility(View.INVISIBLE);
            home_select_book_record_recyclerview.setVisibility(View.GONE);
            home_select_book_record_not_textview.setVisibility(View.VISIBLE);
        } else {
            progress_bar_home_select.setVisibility(View.INVISIBLE);
            adapter_book_time = new book_time_adapter(this, lists);
            adapter_book_time.notifyDataSetChanged();
            home_select_book_record_recyclerview.setAdapter(adapter_book_time);
            list_book_time = lists;
        }
    }

    private void book_recyclerview_select(String email, String book_subject) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_recyclerview_select(email, book_subject);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    String book_page = response.body().get(0).getBook_page();
                    int read_page = response.body().get(0).getRead_page();
                    String writer = response.body().get(0).getWriter();
                    String make = response.body().get(0).getMake();

                    home_select_book_writer_textview.setText(writer + " |");
                    home_select_book_make_textview.setText(make);
                    home_select_book_read_page_textview.setText(String.valueOf(read_page) + "/" + book_page + "p");
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


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref4 = getSharedPreferences("home_select" + email, MODE_PRIVATE);
        번호 = pref4.getString("번호", "");
        System.out.println("번호확인" + 번호);

        if (번호.equals("1")) {
            book_time_recyclerview_select(email, book_subject, page, limit);
            nestedScrollView = findViewById(R.id.scroll_view);
            progress_bar_home_select.setVisibility(View.INVISIBLE);
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        limit += 10;
                        progress_bar_home_select.setVisibility(View.VISIBLE);
                        book_time_recyclerview_select(email, book_subject, page, limit);
                    }
                }
            });
        } else if (번호.equals("2")) {
            System.out.println("들어오는지 호강니하개ㅣ");
            memo_recyclerview_select(email, book_subject, page, limit);
            nestedScrollView2 = findViewById(R.id.scroll_view);
            progress_bar_home_select = findViewById(R.id.progress_bar_home_select);
            nestedScrollView2.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        limit += 10;
                        home_select_book_record_not_textview.setText("");
                        progress_bar_home_select.setVisibility(View.VISIBLE);
                        memo_recyclerview_select(email, book_subject, page, limit);
                    }
                }
            });
        }


        home_select_book_record_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//인텐트 종료
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                Intent intent = getIntent(); //인텐트
                startActivity(intent); //액티비티 열기
                overridePendingTransition(0, 0);//인텐트 효과 없애기

                SharedPreferences pref4 = getSharedPreferences("home_select" + email, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref4.edit();
                editor.putString("번호", "1");
                editor.apply();

                if (번호.equals("1")) {
                    book_time_recyclerview_select(email, book_subject, page, limit);
                    home_select_book_record_recyclerview.setVisibility(View.VISIBLE);
                    home_select_book_memo_recyclerview.setVisibility(View.GONE);
                    home_select_book_memo_plus_button.setVisibility(View.INVISIBLE);
                    home_select_book_record_textview.setTextColor(Color.BLACK);
                    home_select_book_record_line_textview.setTextColor(Color.BLACK);
                    home_select_book_memo_textview.setTextColor(Color.parseColor("#828282"));
                    home_select_book_memo_line_textview.setTextColor(Color.parseColor("#828282"));
                }
            }
        });

        home_select_book_memo_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//인텐트 종료
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                Intent intent = getIntent(); //인텐트
                startActivity(intent); //액티비티 열기
                overridePendingTransition(0, 0);//인텐트 효과 없애기

                SharedPreferences pref4 = getSharedPreferences("home_select" + email, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref4.edit();
                editor.putString("번호", "2");
                editor.apply();


                if (번호.equals("2")) {
                    memo_recyclerview_select(email, book_subject, page, limit);
                    home_select_book_memo_recyclerview.setVisibility(View.VISIBLE);
                    home_select_book_record_recyclerview.setVisibility(View.GONE);
                    home_select_book_memo_plus_button.setVisibility(View.VISIBLE);
                    home_select_book_record_textview.setTextColor(Color.parseColor("#828282"));
                    home_select_book_record_line_textview.setTextColor(Color.parseColor("#828282"));
                    home_select_book_memo_textview.setTextColor(Color.BLACK);
                    home_select_book_memo_line_textview.setTextColor(Color.BLACK);
                }
            }
        });
    }


    //독서중 강제로 앱을 종료 시켰을때
    private void dialog_shared(int time) {
        book_timer_dialog.getWindow().setGravity(Gravity.CENTER);
        book_timer_dialog.show();

        TextView book_timer_ago_time_textview = (TextView) book_timer_dialog.findViewById(R.id.book_timer_ago_time_textview);
        book_timer_ago_time_textview.setText("독서를 중단 하셨습니다 계속 진행 하시겠습니까?");

        int hrs = time / 3600;
        int mins = (time % 3600) / 60;
        int sec = time % 60;
        String time_string = String.format("%02d:%02d:%02d", hrs, mins, sec);
        System.out.println(time_string + "time_stringtime_string");

        TextView book_timer = (TextView) book_timer_dialog.findViewById(R.id.book_timer_time_textview);
        book_timer.setText(time_string);

        book_timer_dialog.findViewById(R.id.book_timer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_select.this, book_read_start.class);
                intent.putExtra("book_subject", book_subject);
                startActivity(intent);
                book_timer_dialog.dismiss();

            }
        });


        book_timer_dialog.findViewById(R.id.book_timer_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref.edit();
                editor2.remove(book_subject + "book_name");
                editor2.remove("나간시간");
                editor2.apply();
                book_timer_dialog.dismiss();

            }
        });
    }
}