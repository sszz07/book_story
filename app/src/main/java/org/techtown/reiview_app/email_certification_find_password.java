package org.techtown.reiview_app;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class email_certification_find_password extends AppCompatActivity {
    EditText certificatioin_email_edittext, certificatioin_email_certification_number_edittext;
    TextView certificatioin_email_time_textview;
    Button certificatioin_email_certificaition_button, certificatioin_next_button, certificatioin_email_certificaition_rebutton;
    String email, number, server_email;
    private CountDownTimer mCountDownTimer;
    //숫자를 크게한 이유는 countdowninterval가 1000의 1초이기 때문이다
    private long mTimeLeftInMillis = 300000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_certification_find_password);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()


                //permitDiskReads 디스크를 읽을수 있게 허락해주는 코드이고 디스크에 문제가 있는지 확인해주기 위해서 사용한다
                .permitDiskReads()

                .permitDiskWrites()

                //permitNetwork()는 네트워크에 문제가 있을때 확인해주는 코드
                .permitNetwork().build());

        certificatioin_next_button = (Button) findViewById(R.id.certificatioin_next_button);
        certificatioin_email_certificaition_button = (Button) findViewById(R.id.certificatioin_email_certificaition_button);
        certificatioin_email_certificaition_rebutton = (Button) findViewById(R.id.certificatioin_email_certificaition_rebutton);
        certificatioin_email_edittext = (EditText) findViewById(R.id.certificatioin_email_edittext);
        certificatioin_email_certification_number_edittext = (EditText) findViewById(R.id.certificatioin_email_certification_number_edittext);
        certificatioin_email_time_textview = findViewById(R.id.certificatioin_email_time_textview);




        //다시받기
        certificatioin_email_certificaition_rebutton.setVisibility(View.GONE);

        //인증번호 입력하기
        certificatioin_email_certification_number_edittext.setVisibility(View.GONE);

        //이메일 인증번호 받는 버튼
        certificatioin_email_certificaition_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = certificatioin_email_edittext.getText().toString();
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                Matcher matcher = pattern.matcher(email);

                if (matcher.find()) {
                    certificatioin_email_certification_number_edittext.setVisibility(View.VISIBLE);
                    certificatioin_email_certificaition_button.setVisibility(View.GONE);
                    certificatioin_email_certificaition_rebutton.setVisibility(View.VISIBLE);


                    book_user_make_email_certification(email);
                } else {
                    Toast.makeText(email_certification_find_password.this, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //인증번호 다시 받았을때
        certificatioin_email_certificaition_rebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //~이고 ~을 위해서 ~이렇게 사용한다
                //mailServer클래스 객체이고 sendSecurityCode 메소드에 값을 넘기기 위해 매개변수에 입력한 값을 넣는다
                SendMail mailServer = new SendMail();
                mailServer.sendSecurityCode(getApplicationContext(), certificatioin_email_edittext.getText().toString());

                mCountDownTimer.cancel();
                mCountDownTimer = null;
                resetTimer();
            }
        });


        //인증번호 입력하고 다음페이지 이동 버튼
        certificatioin_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("이메일인증번호", MODE_PRIVATE);
                number = certificatioin_email_certification_number_edittext.getText().toString();

                //조건문으로 쉐어드값과 인증번호값 틀렸을때
                if (number.length() != 4 || !preferences.getString("이메일인증번호", "").equals(number)) {
                    Toast.makeText(email_certification_find_password.this, "인증번호가 맞지 않습니다", Toast.LENGTH_SHORT).show();
                }
                //인증번호와 같을때
                else if (preferences.getString("이메일인증번호", "").equals(number)) {
                    System.out.println(email+"email_certification_find_password+이메일값");
                    Intent intent = new Intent(email_certification_find_password.this, remake_password.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }
        });

    }


    /*
     * 타이머 메소드
     * */
    //재생하는 버튼
    private void startTimer() {
        //CountDownTimer 객체를 만드는 법
        //countdowninterval-콜백을 받기까지의 시간 간격
        //첫번째 인자의 값이 5000이면 5초이고,두번째 인자가 1000이면 1초이다
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            //onTick-위에서 두번째 인자값이 1000이면 onTick은 1초에 한번씩 자동이 된다
            @Override
            public void onTick(long millisUntilFinished) {
                //millisUntilFinished-완료 될때까지의 시간

                //시간을 변수mTimeLeftInMillis에 넣은것
                mTimeLeftInMillis = millisUntilFinished;

                updateCountDownText();
            }

            @Override
            public void onFinish() {
                //시간이 초과되면 로컬삭제
                SharedPreferences pref = getSharedPreferences("이메일인증번호", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("이메일인증번호");
                editor.apply();
            }

        }.start();
    }

    //타이머 계산 메소드
    private void updateCountDownText() {

        //
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;

        //
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        //
        String timeLeftFormatted = String.format(Locale.getDefault(), "(%02d:%02d)", minutes, seconds);

        //타이머 값을 보여주게 한다
        certificatioin_email_time_textview.setText(timeLeftFormatted);

    }

    //리셋을 할때 mTimeLeftInMillis을 온크리에이트에서 설정한 값을 재설정을 해준다
    private void resetTimer() {
        mTimeLeftInMillis = 300000;
        updateCountDownText();
        startTimer();
    }

    //유저 이메일 확인
    private void book_user_make_email_certification(String email) {
        ApiInterface member_join = ApiClient.getClient().create(ApiInterface.class);
        Call<login_data> call = member_join.book_user_make_email_certification(email);
        call.enqueue(new Callback<login_data>() {
            @Override
            public void onResponse( Call<login_data> call,  Response<login_data> response) {
                server_email = response.body().getEmail();

                if (!server_email.equals("null")) {
                    SendMail mailServer = new SendMail();
                    mailServer.sendSecurityCode(email_certification_find_password.this, certificatioin_email_edittext.getText().toString());
                    startTimer();
                } else {
                    Toast.makeText(email_certification_find_password.this, "회원가입 먼저 해주세요", Toast.LENGTH_SHORT).show();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<login_data> call, Throwable t) {
                Log.e("t_story_make_user_insert()", "에러 : " + t.getMessage());
            }
        });
    }

}