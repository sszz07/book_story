package org.techtown.reiview_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class remake_password extends AppCompatActivity {

    EditText book_password_remake_edittext,book_password_remake_edittext_second;
    Button book_password_find_final_next_button;
    String email,remake_password,remake_password_second;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remake_password);

        book_password_remake_edittext = (EditText) findViewById(R.id.book_password_remake_edittext);
        book_password_remake_edittext_second = (EditText) findViewById(R.id.book_password_remake_edittext_second);
        book_password_find_final_next_button = (Button) findViewById(R.id.book_password_find_final_next_button);


        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        System.out.println(email+"이메일값 + remake_password");


        book_password_find_final_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remake_password = book_password_remake_edittext.getText().toString();
                remake_password_second = book_password_remake_edittext_second.getText().toString();
                if(remake_password.equals(remake_password_second)){
                    book_password_remake(email,remake_password);
                    Toast.makeText(remake_password.this, "비밀번호가 재설정이 되었습니다", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(remake_password.this, "비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void book_password_remake(String email,String password) {
        ApiInterface board_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = board_info_insert.book_password_remake(email,password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call,  Response<String> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(remake_password.this, MainActivity.class);
                    /*로그아웃을 할때 전에 사용한 액티비티는 전부 삭제하게 된다다*/
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure( Call<String> call,  Throwable t) {
                Intent intent = new Intent(remake_password.this, MainActivity.class);
                /*로그아웃을 할때 전에 사용한 액티비티는 전부 삭제하게 된다다*/
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Log.e("t_story_board_info_insert()", "에러 : " + t.getMessage());
            }
        });
    }
}