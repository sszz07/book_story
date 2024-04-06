package org.techtown.reiview_app;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.reiview_app.home.home;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText book_login_email_eidttext,book_login_password_eidttext;
    TextView book_user_make_textview,book_find_password_textview;
    Button book_login_button;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        book_login_email_eidttext = (EditText) findViewById(R.id.book_login_email_eidttext);
        book_login_password_eidttext = (EditText) findViewById(R.id.book_login_password_eidttext);
        book_user_make_textview = (TextView) findViewById(R.id.book_user_make_textview);
        book_find_password_textview = (TextView) findViewById(R.id.book_find_password_textview);
        book_login_button = (Button) findViewById(R.id.book_login_button);


        book_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = book_login_email_eidttext.getText().toString();
                password = book_login_password_eidttext.getText().toString();

                book_login(email,password);
                Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;

                if (email.equals("")) {
                    Toast.makeText(MainActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if(!pattern.matcher(email).matches()){
                    Toast.makeText(MainActivity.this, "이메일 형식으로 입력해주세요", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    Toast.makeText(MainActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        book_user_make_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, user_make.class);
                startActivity(intent);
            }
        });

        book_find_password_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, email_certification_find_password.class);
                startActivity(intent);
            }
        });

    }


    //티스토리 유저 만들기
    @SuppressLint("LongLogTag")
    private void book_login(String email, String password) {
        Log.e("t_story_login_email", email);
        Log.e("t_story_login_password", password);
        ApiInterface login = ApiClient.getClient().create(ApiInterface.class);
        Call<login_data> call = login.book_login(email, password);
        call.enqueue(new Callback<login_data>() {
            @Override
            public void onResponse(Call<login_data> call, Response<login_data> response) {
                String server_email = response.body().getEmail();
                String server_password = response.body().getPassword();
                String server_nick = response.body().getNick();
                String server_profile_image = response.body().getProfile_image();

                int server_following = response.body().getFollowing();
                int server_follower = response.body().getFollwer();

                System.out.println(server_email + "server_email 서버 이메일값,로그인 액티비티");
                System.out.println(server_password + "server_패스워드 로그인 액티비티");
                System.out.println(server_nick + "server_nick 액티비티");
                System.out.println(server_profile_image + "server_profile_image 로그인 액티비티");

                System.out.println(server_following + "server_following 로그인 액티비티");
                System.out.println(server_follower + "server_follower 로그인 액티비티");


                //아이디가 존재하지 않을때
                if(server_email == null && server_password == null){
                    Toast.makeText(MainActivity.this, "존재하지 않는 이메일 입니다", Toast.LENGTH_SHORT).show();
                }
                //패스워드가 틀렸을때
                else if(!server_password.equals(password)){
                    System.out.println("패스워드가 틀렸습니다");
                    Toast.makeText(MainActivity.this, "패스워드가 맞지 않습니다", Toast.LENGTH_SHORT).show();
                }

                //아이디값이 맞다면 인텐트로 넘기기
                else{
                    SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("이메일",server_email);
                    editor.putString("닉네임",server_nick);
                    editor.putInt("팔로워",server_follower);
                    editor.putInt("팔로잉",server_following);

                    //쉐어드는 눌값으로 설정할수가 없다
                    if(server_profile_image != null){
                        editor.putString("프로필이미지",server_profile_image);
                    }else{
                        editor.putString("프로필이미지","이미지x");
                    }
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);

                }
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