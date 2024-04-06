package org.techtown.reiview_app.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class book_read_start_noti extends AppCompatActivity {

    TextView book_read_start_noti_timer_textview, book_read_start_noti_book_subject_textview, book_read_start_noti_back_textview, book_read_start_noti_book_time_textview;
    String book_subject;
    static boolean running = true;
    Dialog book_timer_dialog, book_timer_plus_dialog;
    String time, book_page, image;
    int read_page;
    Button book_read_start_noti_record_button;
    String start_day, email;
    int read_time;
    int time_setting, noti_time_setting;

    //테스트하기 위해 설정함 기존 설정-int time_shared
    //10분 590
    int time_shared = 590;
    Handler handler;
    // adapter 생성
    Button book_read_start_noti_book_time_button;
    ImageView book_read_start_noti_book_time_imageview;
    int time_setting_change;
    private long presstime = 0;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    //실시간 변수 변경 클래스
    MutableLiveData<Integer> score = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_read_start_noti);
        /*
        무엇이 문제인가?-알림 종료를 알리는 바가 생기고 클릭을 했을때 뷰에 데이터값이 없어서 문제가 생긱고 있다
         * 1.독서시작을 하고 백그라운드로 이동을 했을때 어떤 데이터를 저장을 해놔야 되는지 생각해보기
         * intent.putExtra("book_name", book_subject);
        intent.putExtra("time", time_shared);
        intent.putExtra("book_page", book_page);
        intent.putExtra("read_page", String.valueOf(read_page));
        intent.putExtra("read_time", read_time);
        intent.putExtra("start_day", start_day);
        intent.putExtra("image", image);

         * 2.네/아니오 했을때 생각해보기



         3.현재 액티비티에서는 푸시 알림 오지 않게 조건문 만들기

         4.시간
                  * */
        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        /**
         * time_setting값이 0인 이유는 book_subject값이 눌값이기 때문이다
         SharedPreferences pref2 = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
         time_shared = pref2.getInt("나간시간", 0);
         time_setting = pref2.getInt("독서시간", 0);
         score.setValue(time_setting);
         * */

        book_read_start_noti_back_textview = (TextView) findViewById(R.id.book_read_start_noti_back_textview);
        book_read_start_noti_book_subject_textview = (TextView) findViewById(R.id.book_read_start_noti_book_subject_textview);
        book_read_start_noti_timer_textview = (TextView) findViewById(R.id.book_read_start_noti_timer_textview);
        book_read_start_noti_record_button = (Button) findViewById(R.id.book_read_start_noti_record_button);


        book_read_start_noti_book_time_imageview = (ImageView) findViewById(R.id.book_read_start_noti_book_time_imageview);

        book_read_start_noti_book_time_textview = (TextView) findViewById(R.id.book_read_start_noti_book_time_textview);
        book_read_start_noti_book_time_button = (Button) findViewById(R.id.book_read_start_noti_book_time_button);


        book_timer_dialog = new Dialog(book_read_start_noti.this);       // Dialog 초기화
        book_timer_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        book_timer_dialog.setContentView(R.layout.book_timer_dialog);

        book_timer_plus_dialog = new Dialog(book_read_start_noti.this);       // Dialog 초기화
        book_timer_plus_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        book_timer_plus_dialog.setContentView(R.layout.dialog_book_time_plus);


        //책 제목 값 넣기
        Intent intent_get_data = getIntent();
        book_subject = intent_get_data.getStringExtra("book_subject");
        image = intent_get_data.getStringExtra("image");

        book_read_start_noti_book_subject_textview.setText(book_subject);
        book_read_page_check(book_subject);
        startTimer(book_subject);


        //서비스 실행하기
        Intent intent = new Intent(this, MyService.class);
        startService(intent);


        //x표시 텍스트 뷰
        book_read_start_noti_back_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                //이미지에 붙일 시간 값 이미지 재사용 위해서
                Date date = new Date();
                start_day = simpleDateFormat.format(date);


                time_shared = time_shared - 1;
                running = false;

                book_timer_dialog.getWindow().setGravity(Gravity.CENTER);
                book_timer_dialog.show();


                TextView textView = (TextView) book_timer_dialog.findViewById(R.id.book_timer_ago_time_textview);
                textView.setText("독서를 종료하시겠습니까?");


                TextView book_timer = (TextView) book_timer_dialog.findViewById(R.id.book_timer_time_textview);
                book_timer.setText(time);


                book_timer_dialog.findViewById(R.id.book_timer_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences pref = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = pref.edit();
                        editor2.remove(book_subject + "book_name");
                        editor2.remove("나간시간");
                        editor2.apply();

                        finish();
                        handler.removeMessages(0);
                        onBackPressed();
                    }
                });


                book_timer_dialog.findViewById(R.id.book_timer_cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        running = true;
                        book_timer_dialog.dismiss();
                    }
                });
            }
        });


        //기록하기
        book_read_start_noti_record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                //이미지에 붙일 시간 값 이미지 재사용 위해서
                Date date = new Date();
                start_day = simpleDateFormat.format(date);


                time_shared = time_shared - 1;
                running = false;

                /**
                 * WindowLeaked : Activity has leaked window DecorView that was originally added here
                 * 다이얼로그가 dismiss()하지 않아서 생긴 에러이다
                 * */
                book_timer_dialog.getWindow().setGravity(Gravity.CENTER);
                book_timer_dialog.show();


                TextView book_timer_ago_time_textview = (TextView) book_timer_dialog.findViewById(R.id.book_timer_ago_time_textview);
                book_timer_ago_time_textview.setText("기록 하시겠습니까?");

                TextView book_timer = (TextView) book_timer_dialog.findViewById(R.id.book_timer_time_textview);
                book_timer.setText(time);


                book_timer_dialog.findViewById(R.id.book_timer_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (time_shared != 0) {

                            book_timer_dialog.dismiss();
                            handler.removeMessages(0);
                            Intent intent = new Intent(book_read_start_noti.this, book_page_check.class);
                            intent.putExtra("book_name", book_subject);
                            intent.putExtra("time", time_shared);
                            intent.putExtra("book_page", book_page);
                            intent.putExtra("read_page", String.valueOf(read_page));
                            intent.putExtra("read_time", read_time);
                            intent.putExtra("start_day", start_day);
                            intent.putExtra("image", image);

                            startActivity(intent);

                            finish();
                        } else if (time_shared == 0) {
                            Toast.makeText(book_read_start_noti.this, "0초는 기록할수 없습니다다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                book_timer_dialog.findViewById(R.id.book_timer_cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        running = true;
                        book_timer_dialog.dismiss();
                    }
                });
            }
        });
    }


    private void startTimer(String book_subject) {
        SharedPreferences pref2 = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
        time_setting = pref2.getInt("독서시간", 0);
//        time_shared = pref2.getInt("나간시간", 0);

        score.setValue(time_setting);
        running = true;


        if (score.getValue() == -1) {
            book_read_start_noti_book_time_button.setText("시간 재설정");
            book_read_start_noti_book_time_textview.setText("제한없이");
        }
        //1시간 이상
        else if (score.getValue() >= 3600) {
            int hrs = score.getValue() / 3600;
            String time2 = String.format("%2d시간", hrs);
            book_read_start_noti_book_time_textview.setText(time2);
        } else if (score.getValue() < 3600) {
            int mins = (score.getValue() % 3600) / 60;
            String time2 = String.format("%2d분", mins);
            book_read_start_noti_book_time_textview.setText(time2);
        }


        //나갔을때
        if (time_shared != 0) {
            if (time_shared == -1) {
                book_read_start_noti_book_time_textview.setVisibility(View.GONE);
                book_read_start_noti_book_time_imageview.setVisibility(View.GONE);
                book_read_start_noti_book_time_button.setVisibility(View.GONE);
            }
            //1시간 이상
            else if (time_setting >= 3600) {
                int hrs = time_setting / 3600;
                String time2 = String.format("%2d시간", hrs);
                book_read_start_noti_book_time_textview.setText(time2);
            } else if (time_setting < 3600) {
                int mins = (time_setting % 3600) / 60;
                String time2 = String.format("%02d분", mins);
                book_read_start_noti_book_time_textview.setText(time2);
            }
        }


        //독서 강제 종료 했을때 와 독서시간을 추가했을때
        if (time_shared != 0) {
            handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int hrs = time_shared / 3600;
                    int mins = (time_shared % 3600) / 60;
                    int sec = time_shared % 60;

                    time = String.format("%02d:%02d:%02d", hrs, mins, sec);
                    Log.e("시간", time);
                    book_read_start_noti_timer_textview.setText(time);

                    if (running) {
                        if (time_setting_change == -1) {
                            if (score.getValue() == -1) {
                                finish_time_read(time_shared);
                            }
                        } else {
                            if (score.getValue() == time_shared) {
                                finish_time_read(score.getValue());
                            }
                        }
                        time_shared++;
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
        //시간추가와 독서 강제 종료를 하지 않았을때
        else {
            handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int hrs = time_shared / 3600;
                    int mins = (time_shared % 3600) / 60;
                    int sec = time_shared % 60;
                    time = String.format("%02d:%02d:%02d", hrs, mins, sec);

                    Log.e("time_shared", String.valueOf(time_shared));
                    Log.e("시간", time);
                    book_read_start_noti_timer_textview.setText(time);


                    if (running) {
                        //제한없이 할때
                        if (time_setting_change == -1) {
                            if (score.getValue() == -1) {
                                finish_time_read(time_shared);
                            }
                        } else {
                            if (score.getValue() == time_shared) {
                                finish_time_read(score.getValue());
                            }
                        }
                        time_shared++;
                    } else {
                        finish_time_read(score.getValue());
                    }
                    handler.postDelayed(this, 1000);
                }

            });
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //타이머가 동작이 되고 있는 상태였을때
        if (!running) {
            handler.removeCallbacksAndMessages(null);
            SharedPreferences sharedPreferences = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(book_subject + "book_name", book_subject);
            editor.putInt("독서시간", score.getValue());
            editor.putInt("나간시간", time_shared);
            editor.apply();
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        time_shared = time_shared - 1;
//        running = false;
//    }


    // INSERT 구문 은 WHERE 절을 지원하지 않으므로 쿼리가 실패합니다. id열이 고유하거나 기본 키 라고 가정합니다 .
    private void book_read_page_check(String book_subject) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_read_page_check(book_subject);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    read_page = response.body().get(0).getRead_page();
                    book_page = response.body().get(0).getBook_page();
                    read_time = response.body().get(0).getRead_time();

                    System.out.println("read_page" + read_page + "book_page" + book_page + "read_time" + read_time);
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<home_data>> call, Throwable t) {
                Log.e("t_story_home_recyclerview_select()", "에러 : " + t.getMessage());
            }
        });
    }


    private void finish_time_read(int time) {
        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        String noti = pref.getString("알림", "");

        if (noti.equals("켜기")) {
            NotificationSomethings();
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //이미지에 붙일 시간 값 이미지 재사용 위해서
        Date date = new Date();
        start_day = simpleDateFormat.format(date);

        time_shared = time_shared - 1;

        //핸들러 멈추기
        running = false;

        book_timer_dialog.getWindow().setGravity(Gravity.CENTER);
        book_timer_dialog.show();
        book_timer_dialog.dismiss();//다이얼로그 재실행하기 위해서


        TextView book_timer_ago_time_textview = (TextView) book_timer_dialog.findViewById(R.id.book_timer_ago_time_textview);
        book_timer_ago_time_textview.setText("기록 하시겠습니까?");

        TextView book_timer = (TextView) book_timer_dialog.findViewById(R.id.book_timer_time_textview);

        int hrs = time / 3600;
        int mins = (time % 3600) / 60;
        int sec = time % 60;
        String time2 = String.format("%02d:%02d:%02d", hrs, mins, sec);

        book_timer.setText(time2);

        book_timer_dialog.findViewById(R.id.book_timer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time_shared != 0) {
                    handler.removeMessages(0);
                    Intent intent = new Intent(book_read_start_noti.this, book_page_check.class);
                    intent.putExtra("book_name", book_subject);
                    intent.putExtra("time", time_shared);
                    intent.putExtra("book_page", book_page);
                    intent.putExtra("read_page", String.valueOf(read_page));
                    intent.putExtra("read_time", read_time);
                    intent.putExtra("start_day", start_day);
                    startActivity(intent);

                    finish();
                }
            }
        });


        book_timer_dialog.findViewById(R.id.book_timer_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;
                time_setting_change();
                book_timer_dialog.dismiss();

            }
        });
    }


    //시간 추가하는 메소드
    private void time_setting_change() {
        book_timer_plus_dialog.getWindow().setGravity(Gravity.CENTER);
        book_timer_plus_dialog.show();
        running = false;

        //제한없음
        book_timer_plus_dialog.findViewById(R.id.dialog_book_time_plus_x).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;
                time_setting_change = -1;
                book_read_start_noti_book_time_textview.setText("제한없이");
                score.setValue(0);
                book_timer_plus_dialog.dismiss();
            }
        });


        //10분 추가
        book_timer_plus_dialog.findViewById(R.id.dialog_book_time_ten_min_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;

                if (score.getValue() >= 10800) {
                    Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (score.getValue() == -1) {
                        score.setValue(0);
                    }
                    score.setValue(score.getValue() + 600);
                    if (score.getValue() < 3600) {
                        int mins = (score.getValue() % 3600) / 60;
                        String time = String.format("%02d분", mins);
                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() >= 3600) {

                        if (score.getValue() > 10800) {
                            score.setValue(score.getValue() - 600);
                            Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            int hrs = score.getValue() / 3600;
                            int mins = (score.getValue() % 3600) / 60;
                            String time = String.format("%2d시간 %02d분", hrs, mins);

                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        }
                    } else if (score.getValue() == 3600) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() == 7200) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() == 10800) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() > 10800) {
                        score.setValue(score.getValue() - 600);
                        Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //20분 추가
        book_timer_plus_dialog.findViewById(R.id.dialog_book_time_two_min_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;
                System.out.println(score.getValue() + "score.getValue()score.getValue()score.getValue()+20분");
                if (score.getValue() >= 10800) {
                    Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (score.getValue() == -1) {
                        score.setValue(0);
                    }
                    score.setValue(score.getValue() + 1200);
                    System.out.println("score.getValue()" + score.getValue());
                    if (score.getValue() < 3600) {
                        int mins = (score.getValue() % 3600) / 60;
                        String time = String.format("%02d분", mins);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() >= 3600) {
                        if (score.getValue() == 3600) {

                            int hrs = score.getValue() / 3600;
                            int mins = (score.getValue() % 3600) / 60;
                            String time = String.format("%2d시간 %02d분", hrs, mins);

                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        } else if (score.getValue() == 7200) {
                            int hrs = score.getValue() / 3600;
                            String time = String.format("%2d시간", hrs);

                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        } else if (score.getValue() == 10800) {
                            int hrs = score.getValue() / 3600;
                            String time = String.format("%2d시간", hrs);

                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        } else if (score.getValue() >= 10800) {
                            score.setValue(score.getValue() - 1200);

                            Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            int hrs = score.getValue() / 3600;
                            int mins = (score.getValue() % 3600) / 60;
                            String time = String.format("%2d시간 %02d분", hrs, mins);

                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        }
                    }
                }
            }
        });

        //30분 추가
        book_timer_plus_dialog.findViewById(R.id.dialog_book_time_three_min_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;

                System.out.println(score.getValue() + "score.getValue()score.getValue()score.getValue()+30분");

                if (score.getValue() >= 10800) {
                    Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (score.getValue() == -1) {
                        score.setValue(0);
                    }
                    score.setValue(score.getValue() + 1800);
                    if (score.getValue() < 3600) {
                        int mins = (score.getValue() % 3600) / 60;
                        String time = String.format("%02d분", mins);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() >= 3600) {

                        //1시간
                        if (score.getValue() == 3600) {
                            int hrs = score.getValue() / 3600;
                            String time = String.format("%2d시간", hrs);

                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        }

                        //2시간
                        else if (score.getValue() == 7200) {
                            int hrs = score.getValue() / 3600;
                            String time = String.format("%2d시간", hrs);

                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        }
                        //3시간
                        else if (score.getValue() == 10800) {
                            int hrs = score.getValue() / 3600;
                            String time = String.format("%2d시간", hrs);

                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        } else if (score.getValue() >= 10800) {
                            score.setValue(score.getValue() - 1800);

                            Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            int hrs = score.getValue() / 3600;
                            int mins = (score.getValue() % 3600) / 60;
                            String time = String.format("%2d시간 %02d분", hrs, mins);

                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        }
                    }
                }
            }
        });

        //1시간 추가
        book_timer_plus_dialog.findViewById(R.id.dialog_book_time_one_hour_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;
                System.out.println(score.getValue() + "score.getValue()score.getValue()score.getValue()+1시간");

                if (score.getValue() >= 10800) {
                    Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (score.getValue() == -1) {
                        score.setValue(0);
                    }
                    score.setValue(score.getValue() + 3600);

                    if (score.getValue() > 10800) {
                        score.setValue(score.getValue() - 3600);
                        Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                    } else if (score.getValue() == 3600) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() == 7200) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() == 10800) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else {
                        int hrs = score.getValue() / 3600;
                        int mins = (score.getValue() % 3600) / 60;

                        String time = String.format("%2d시간 %02d분", hrs, mins);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    }
                }
            }
        });

        //1분 추가
        book_timer_plus_dialog.findViewById(R.id.dialog_book_time_one_min_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;

                if (score.getValue() >= 10800) {
                    Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (score.getValue() == -1) {
                        score.setValue(0);
                    }
                    score.setValue(score.getValue() + 60);
                    if (score.getValue() == 3600) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() == 7200) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() == 10800) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() >= 10800) {
                        score.setValue(score.getValue() - 60);

                        Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        int hrs = score.getValue() / 3600;
                        int mins = (score.getValue() % 3600) / 60;

                        if (hrs == 0) {
                            String time = String.format("%2d분", mins);
                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        } else {
                            String time = String.format("%2d시간 %2d분", hrs, mins);
                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        }
                    }
                }

            }
        });

        //5분 추가
        book_timer_plus_dialog.findViewById(R.id.dialog_book_time_five_min_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;
                System.out.println(score.getValue() + "score.getValue()score.getValue()score.getValue()+5분");

                if (score.getValue() >= 10800) {
                    Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                } else {


                    score.setValue(score.getValue() + 300);
                    if (score.getValue() == 3600) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() == 7200) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() == 10800) {
                        int hrs = score.getValue() / 3600;
                        String time = String.format("%2d시간", hrs);

                        book_read_start_noti_book_time_textview.setText(time);
                        book_timer_plus_dialog.dismiss();
                    } else if (score.getValue() >= 10800) {
                        score.setValue(score.getValue() - 300);

                        Toast.makeText(book_read_start_noti.this, "3시간 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        int hrs = score.getValue() / 3600;
                        int mins = (score.getValue() % 3600) / 60;

                        if (hrs == 0) {
                            String time = String.format("%2d분", mins);
                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        } else {
                            String time = String.format("%2d시간 %2d분", hrs, mins);
                            book_read_start_noti_book_time_textview.setText(time);
                            book_timer_plus_dialog.dismiss();
                        }
                    }
                }

            }
        });


        book_timer_plus_dialog.findViewById(R.id.dialog_book_time_cancel_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;
                book_timer_plus_dialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //시간 추가하기
        book_read_start_noti_book_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_setting_change();
            }
        });
    }


    //에뮬레이터 뒤로가기 버튼을 클릭했을때
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - presstime;


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //이미지에 붙일 시간 값 이미지 재사용 위해서
        Date date = new Date();
        start_day = simpleDateFormat.format(date);


        time_shared = time_shared - 1;
        running = false;

        book_timer_dialog.getWindow().setGravity(Gravity.CENTER);
        book_timer_dialog.show();


        TextView textView = (TextView) book_timer_dialog.findViewById(R.id.book_timer_ago_time_textview);
        textView.setText("독서를 종료하시겠습니까?");


        TextView book_timer = (TextView) book_timer_dialog.findViewById(R.id.book_timer_time_textview);
        book_timer.setText(time);


        book_timer_dialog.findViewById(R.id.book_timer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences(email + book_subject + "time_setting", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref.edit();
                editor2.remove(book_subject + "book_name");
                editor2.remove("나간시간");
                editor2.apply();

                finish();
                handler.removeMessages(0);
                onBackPressed();
            }
        });


        book_timer_dialog.findViewById(R.id.book_timer_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = true;
                book_timer_dialog.dismiss();
            }
        });
    }

    public void NotificationSomethings() {
        running = false;

        // 채널을 생성 및 전달해 줄수 있는 NotificationManager를 생성한다.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 이동하려는 액티비티를 작성해준다.
//        Intent notificationIntent = new Intent(this, book_read_start_noti.class);
//        // 노티를 눌러서 이동시 전달할 값을 담는다. // 전달할 값을 notificationIntent에 담습니다.
////        notificationIntent.putExtra("notificationId", count);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent mPendingIntent = PendingIntent.getActivity(
                this,
                0, // 보통 default값 0을 삽입
                new Intent(this, book_read_start.class),
                PendingIntent.FLAG_CANCEL_CURRENT//
        );


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground)) //BitMap 이미지 요구
                .setContentTitle("독서가 종료되었습니다")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(mPendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                .setAutoCancel(true); // 눌러야 꺼지는 설정


        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName = "노티페케이션 채널";
            String description = "오레오 이상";
            int importance = NotificationManager.IMPORTANCE_HIGH;// 우선순위 설정

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);


        } else
            builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작
        finish();//액티비티 스택의 탑을 제거하고 PendingIntent로 만든 액티비티가 탑으로 설정함 하지 않으면 기존에 있던 액티비티 때문에 에러가 생김


    }
}