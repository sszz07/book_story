package org.techtown.reiview_app.home;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.MainActivity;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.setting.setting_select;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class book_page_check extends AppCompatActivity {
    EditText book_page_check_edittext;
    TextView book_all_page_check_textview, book_my_page_check_textview;
    Button book_page_check_next_button,book_page_finish_book_button;
    String book_subject, read_page_intent, book_page, start_day, email,image;
    int today_read_time, read_time;

    String read_page;
String read_page_2;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_page_check);


        Intent intent_get_data = getIntent();
        book_subject = intent_get_data.getStringExtra("book_name");
        System.out.println(book_subject+"book_subjectbook_subject");
        read_page_intent = intent_get_data.getStringExtra("read_page");
        book_page = intent_get_data.getStringExtra("book_page");
        start_day = intent_get_data.getStringExtra("start_day");
        System.out.println(start_day+"start_daystart_day");
        image = intent_get_data.getStringExtra("image");
        today_read_time = intent_get_data.getIntExtra("time", 0);
        System.out.println(today_read_time+"today_read_timetoday_read_time");
        read_time = intent_get_data.getIntExtra("read_time", 0);
        System.out.println(read_time+"read_timeread_time");


        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        book_page_check_edittext = (EditText) findViewById(R.id.book_page_check_edittext);
        book_all_page_check_textview = (TextView) findViewById(R.id.book_all_page_check_textview);
        book_page_check_next_button = (Button) findViewById(R.id.book_page_check_next_button);
        book_page_finish_book_button = (Button) findViewById(R.id.book_page_finish_book_button);

        SharedPreferences sharedPreferences = getSharedPreferences(email + "noti", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("start_day", start_day);
        editor.apply();


        if(book_subject != null){
            book_all_page_check_textview.setText("전체 : " + book_page + "p");
            book_page_check_edittext.setHint("지난번에 " + read_page_intent + "p까지 읽었습니다");

            //책 다 읽었을때
            book_page_finish_book_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref = getSharedPreferences(email+book_subject+"time_setting", MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = pref.edit();
                    editor2.clear();
                    editor2.apply();

                    Intent intent = new Intent(book_page_check.this, finish_book.class);
                    intent.putExtra("book_name", book_subject);
                    intent.putExtra("start_day", start_day);
                    intent.putExtra("today_read_time", today_read_time);
                    intent.putExtra("all_time_read_time", read_time + today_read_time);
                    intent.putExtra("read_page_intent", read_page_intent);
                    startActivity(intent);

                }
            });

            book_page_check_next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int read_page_intent2 = Integer.parseInt(read_page_intent);

                    //총 페이지
                    int book_page_int = Integer.parseInt(book_page);

                    //읽은 페이지
                    read_page = book_page_check_edittext.getText().toString();
                    if(read_page.length() != 0) {
                        int read_page_int = Integer.parseInt(read_page);

                        long present_time = System.currentTimeMillis();

                        if (read_page_int > book_page_int) {
                            Toast.makeText(book_page_check.this, "총 페이지수를 초과 하였습니다", Toast.LENGTH_SHORT).show();
                        } else if (read_page_int == 0) {
                            Toast.makeText(book_page_check.this, "0은 입력할수 없습니다", Toast.LENGTH_SHORT).show();
                        } else if (read_page_intent2 > read_page_int) {
                            Toast.makeText(book_page_check.this, "전 보다 적은 페이지는 안됩니다", Toast.LENGTH_SHORT).show();
                        } else if (read_page.length() == 0) {
                            Toast.makeText(book_page_check.this, "페이지 수를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }  else if (read_page_int == Integer.parseInt(read_page_intent)) {
                            Toast.makeText(book_page_check.this, "같은 페이지는 입력할수 없습니다", Toast.LENGTH_SHORT).show();
                        } else if(book_page_int == Integer.parseInt(read_page)){
                            Intent intent = new Intent(book_page_check.this, finish_book.class);
                            intent.putExtra("book_name", book_subject);
                            intent.putExtra("start_day", start_day);
                            intent.putExtra("today_read_time", today_read_time);
                            intent.putExtra("all_time_read_time", read_time + today_read_time);
                            intent.putExtra("read_page", read_page);
                            startActivity(intent);
                        }else {
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                            String getTime = sdf.format(date);


                            //기록 그래프 추가하기
                            book_record_graph_insert(email,today_read_time,getTime);
                            //기록 부분 추가하기
                            book_record_insert(image,book_subject,email,read_time + today_read_time,getTime);
                            read_info_update(book_subject, read_time, read_page, present_time, today_read_time, read_time + today_read_time, read_page_int - read_page_intent2, read_page, book_page, start_day,getTime,image);
                        }
                    }
                }
            });
        }

        else{
            System.out.println("들어와지나???");
            SharedPreferences pref2 = getSharedPreferences(email + "noti", MODE_PRIVATE);
            String book_subject = pref2.getString("book_subject", "");
            String book_page = pref2.getString("book_page", "");
            //읽은 페이지수
            int read_page = pref2.getInt("read_page", 0);
            //총 읽은 시간
            int read_time = pref2.getInt("read_time", 0);
            //오늘 읽은 시간
            int 독서시간 = pref2.getInt("독서시간", 0);
            //읽은 날짜
            String start_day = pref2.getString("start_day", "");

            book_all_page_check_textview.setText("전체 : " + book_page + "p");
            book_page_check_edittext.setHint("지난번에 " + read_page + "p까지 읽었습니다");

            //책 다 읽었을때
            book_page_finish_book_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref = getSharedPreferences(email+book_subject+"time_setting", MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = pref.edit();
                    editor2.clear();
                    editor2.apply();

                    Intent intent = new Intent(book_page_check.this, finish_book.class);
                    intent.putExtra("book_name", book_subject);
                    intent.putExtra("start_day", start_day);
                    intent.putExtra("today_read_time", today_read_time);
                    intent.putExtra("all_time_read_time", read_time + today_read_time);
                    intent.putExtra("read_page_intent", read_page);
                    startActivity(intent);

                }
            });

            book_page_check_next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //총 페이지
                    int book_page_int = Integer.parseInt(book_page);

                    //읽은 페이지
                    read_page_2 = book_page_check_edittext.getText().toString();
                    if(read_page_2.length() != 0) {
                        int read_page_int = Integer.parseInt(read_page_2);

                        long present_time = System.currentTimeMillis();

                        if (read_page_int > book_page_int) {
                            Toast.makeText(book_page_check.this, "총 페이지수를 초과 하였습니다", Toast.LENGTH_SHORT).show();
                        } else if (read_page_int == 0) {
                            Toast.makeText(book_page_check.this, "0은 입력할수 없습니다", Toast.LENGTH_SHORT).show();
                        } else if (read_page > read_page_int) {
                            Toast.makeText(book_page_check.this, "전 보다 적은 페이지는 안됩니다", Toast.LENGTH_SHORT).show();
                        } else if (read_page_2.length() == 0) {
                            Toast.makeText(book_page_check.this, "페이지 수를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }  else if (read_page_int == Integer.parseInt(read_page_intent)) {
                            Toast.makeText(book_page_check.this, "같은 페이지는 입력할수 없습니다", Toast.LENGTH_SHORT).show();
                        } else if(book_page_int == Integer.parseInt(read_page_2)){
                            Intent intent = new Intent(book_page_check.this, finish_book.class);
                            intent.putExtra("book_name", book_subject);
                            intent.putExtra("start_day", start_day);
                            intent.putExtra("today_read_time", 독서시간);
                            intent.putExtra("all_time_read_time", read_time + 독서시간);
                            intent.putExtra("read_page", read_page_2);
                            startActivity(intent);
                        }else {
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                            String getTime = sdf.format(date);


                            //기록 그래프 추가하기
                            book_record_graph_insert(email,독서시간,getTime);
                            //기록 부분 추가하기
                            book_record_insert(image,book_subject,email,read_time + 독서시간,getTime);
                            read_info_update(book_subject, read_time, read_page_2, present_time, 독서시간, read_time + 독서시간, read_page_int - read_page, read_page_2, book_page, start_day,getTime,image);
                        }
                    }
                }
            });
        }

    }


    private void read_info_update(String book_subject, int read_time, String read_page, long present_time, int today_read_time, int all_read_time, int read_book_page, String all_read_book_page, String book_page,String start_day,String getTime,String image) {

        System.out.println(book_subject+"book_subject");
        System.out.println(read_time+today_read_time+"read_time+today_read_time");
        System.out.println(Integer.parseInt(read_page)+"Integer.parseInt(read_page)");
        System.out.println(present_time+"present_time");
        System.out.println(getTime+"getTimegetTime");

        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_info_insert.read_info_update(book_subject, read_time+today_read_time, Integer.parseInt(read_page), present_time);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call,  Response<String> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    SharedPreferences pref = getSharedPreferences(book_subject+email, MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = pref.edit();
                    editor2.remove(book_subject + "book_name");
                    editor2.remove(book_subject + "time");
                    editor2.apply();


                    read_date_insert(book_subject,today_read_time,email,start_day);


                    Intent intent = new Intent(book_page_check.this, book_read_finish.class);
                    System.out.println(all_read_time + "all_read_timeall_read_time");
                    intent.putExtra("today_read_time", today_read_time);
                    intent.putExtra("all_time_read_time", all_read_time);
                    intent.putExtra("read_book_page", read_book_page);
                    intent.putExtra("all_read_book_page", all_read_book_page);
                    intent.putExtra("book_page", book_page);
                    startActivity(intent);
                    finish();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure( Call<String> call,  Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
                read_date_insert(book_subject,today_read_time,email,start_day);
                SharedPreferences pref = getSharedPreferences(book_subject+email, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref.edit();
                editor2.remove(book_subject + "book_name");
                editor2.remove(book_subject + "time");
                editor2.apply();


                Intent intent = new Intent(book_page_check.this, book_read_finish.class);
                intent.putExtra("today_read_time", today_read_time);
                intent.putExtra("all_time_read_time", all_read_time);
                intent.putExtra("read_book_page", read_book_page);
                intent.putExtra("all_read_book_page", all_read_book_page);
                intent.putExtra("book_page", book_page);
                intent.putExtra("read_page_intent", read_page_intent);
                startActivity(intent);
                finish();
            }
        });
    }


    private void read_date_insert(String book_subject, int today_read_time, String email, String start_day) {
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_info_insert.read_date_insert(book_subject,today_read_time,email,start_day);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call,  Response<String> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure( Call<String> call,  Throwable t) {
            }
        });
    }



    private void book_record_insert(String image, String subject, String email, int all_read_time,String record_date) {
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<home_data> call = search_info_insert.book_record_insert(image,subject,email,all_read_time,record_date);
        call.enqueue(new Callback<home_data>() {
            @Override
            public void onResponse( Call<home_data> call,  Response<home_data> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure( Call<home_data> call,  Throwable t) {
            }
        });
    }

    private void book_record_graph_insert(String email, int all_read_time,String record_date) {
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<home_data> call = search_info_insert.book_record_graph_insert(email,all_read_time,record_date);
        call.enqueue(new Callback<home_data>() {
            @Override
            public void onResponse( Call<home_data> call,  Response<home_data> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure( Call<home_data> call,  Throwable t) {
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref = getSharedPreferences(email+book_subject+"time_setting", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref.edit();
        editor2.clear();
        editor2.apply();
    }
}