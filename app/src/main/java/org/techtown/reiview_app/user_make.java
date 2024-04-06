package org.techtown.reiview_app;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_make extends AppCompatActivity {

    EditText make_user_id_edittext, user_make_password_first_edittext, user_make_password_second_edittext, make_user_nick_edittext;
    Button make_user_make_button;
    TextView make_user_password_error_textview;
    String password1, password2, email, nick;
    String server_nick,server_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_make);


        //쉐어드에 저장되어있는값으로 중복을 확인했는지 알기!!!!!!!!!!

        make_user_id_edittext = (EditText) findViewById(R.id.make_user_id_edittext);
        user_make_password_first_edittext = (EditText) findViewById(R.id.user_make_password_first_edittext);
        user_make_password_second_edittext = (EditText) findViewById(R.id.user_make_password_second_edittext);
        make_user_nick_edittext = (EditText) findViewById(R.id.make_user_nick_edittext);
        make_user_make_button = (Button) findViewById(R.id.make_user_make_button);
        make_user_password_error_textview = (TextView) findViewById(R.id.make_user_password_error_textview);

        make_user_password_error_textview.setVisibility(View.GONE);


        //패스워드 재패스워드 확인
        user_make_password_second_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password1 = user_make_password_first_edittext.getText().toString();
                password2 = user_make_password_second_edittext.getText().toString();
                Log.e("첫번째 패스워드 값", password1);
                Log.e("두번쨰 패스워드 값", password2);
                System.out.println(editable.length());


                if (!password1.equals(password2)) {
                    make_user_password_error_textview.setVisibility(View.VISIBLE);
                } else if(password1.equals(password2)){
                    make_user_password_error_textview.setVisibility(View.GONE);
                } else if (editable.length() == 0) {
                    make_user_password_error_textview.setVisibility(View.INVISIBLE);
                }
            }
        });


        user_make_password_first_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    make_user_password_error_textview.setVisibility(View.INVISIBLE);
                }
            }
        });



        //회원가입 버튼
        // 아이디값,패스워드,블로그 이름,닉네임값  t_story_make_user()메소드에 보냄
        make_user_make_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = make_user_id_edittext.getText().toString();
                nick = make_user_nick_edittext.getText().toString();
                password1 = user_make_password_first_edittext.getText().toString();
                password2 = user_make_password_second_edittext.getText().toString();

                Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;

                if (email.equals("")) {
                    Toast.makeText(user_make.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if(!pattern.matcher(email).matches()){
                    Toast.makeText(user_make.this, "이메일 형식으로 입력해주세요", Toast.LENGTH_SHORT).show();
                  }else if (nick.equals("")) {
                    Toast.makeText(user_make.this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else if(password1.equals(password2)){
                    book_user_make_email_certification(email);
                }
            }
        });
    }


    //유저 이메일 확인
    private void book_user_make_email_certification(String email) {
        ApiInterface member_join = ApiClient.getClient().create(ApiInterface.class);
        Call<login_data> call = member_join.book_user_make_email_certification(email);
        call.enqueue(new Callback<login_data>() {
            @Override
            public void onResponse( Call<login_data> call,  Response<login_data> response) {
                server_email = response.body().getEmail();

                if(!server_email.equals("null")){
                    Toast.makeText(user_make.this, "이미 가입한 이메일 입니다", Toast.LENGTH_SHORT).show();
                }else {
                    book_user_make_nick_certification(nick);
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<login_data> call, Throwable t) {
                Log.e("t_story_make_user_insert()", "에러 : " + t.getMessage());
            }
        });
    }


    //유저 닉네임 확인
    private void book_user_make_nick_certification(String nick) {
        ApiInterface member_join = ApiClient.getClient().create(ApiInterface.class);
        Call<login_data> call = member_join.book_user_make_nick_certification(nick);
        call.enqueue(new Callback<login_data>() {
            @Override
            public void onResponse( Call<login_data> call,  Response<login_data> response) {
                server_nick = response.body().getNick();

                if(!server_nick.equals("null")){
                    Toast.makeText(user_make.this, "이미 존재하는 닉네임 입니다", Toast.LENGTH_SHORT).show();
                }else {
                    book_user_make_insert(email , password1, nick);
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<login_data> call, Throwable t) {
                Log.e("t_story_make_user_insert()", "에러 : " + t.getMessage());
            }
        });
    }


    //티스토리 유저 만들기
    @SuppressLint("LongLogTag")
    private void book_user_make_insert(String email, String password, String nick) {
        ApiInterface member_join = ApiClient.getClient().create(ApiInterface.class);
        Call<login_data> call = member_join.book_user_make_insert(email, password, nick);
        call.enqueue(new Callback<login_data>() {
            @Override
            public void onResponse( Call<login_data> call,  Response<login_data> response) {

                onBackPressed();
            }

            @Override
            public void onFailure(Call<login_data> call, Throwable t) {

                /**
                 * 에러-CLEARTEXT communication to 52.79.180.89 not permitted by network security policy
                 * 이유-https 아니라 http로 되어있기 때문이다,안드로이드 9.0 파이에서는 https를 사용하도록 강제합니다.
                 * 해결- android:usesCleartextTraffic="true" 매니페스트에 넣어준다
                 * */

                /**
                 * 에러- java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1 path $
                 * 이유-전달 받은 타입과 내가 예상한 타입이 맞지 않아서 발생하는 문제다. 특히 위 에러는 나는 Object가 올것으로 기대 했는데 실제 데이터가 배열인 경우다
                 * 해결-서버와 디비 연결하는 php클래스 이름이 같으면 연결이 안된다
                 * */
                Log.e("t_story_make_user_insert()", "에러 : " + t.getMessage());
            }
        });
    }
}