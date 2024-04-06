package org.techtown.reiview_app.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class book_diary_plus extends AppCompatActivity {

    EditText book_diary_plus_read_start_edittext, book_diary_plus_read_end_edittext, book_diary_plus_memory_edittext, book_diary_plus_think_edittext;
    String book_diary_plus_read_start, book_diary_plus_read_end, book_diary_plus_memory, book_diary_plus_think, email, book_subject, book_page,read_page_intent;
    Button book_diary_plus_button;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일 hh시mm분");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_diary_plus);

        Intent intent = getIntent();
        book_subject = intent.getStringExtra("book_subject");
        book_page = intent.getStringExtra("book_page");
        read_page_intent = intent.getStringExtra("read_page_intent");

        book_diary_plus_read_start_edittext = (EditText) findViewById(R.id.book_diary_plus_read_start_edittext);
        book_diary_plus_read_end_edittext = (EditText) findViewById(R.id.book_diary_plus_read_end_edittext);
        book_diary_plus_memory_edittext = (EditText) findViewById(R.id.book_diary_plus_memory_edittext);
        book_diary_plus_think_edittext = (EditText) findViewById(R.id.book_diary_plus_think_edittext);
        book_diary_plus_button = (Button) findViewById(R.id.book_diary_plus_button);

        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");


        book_diary_plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_diary_plus_read_start = book_diary_plus_read_start_edittext.getText().toString();
                book_diary_plus_read_end = book_diary_plus_read_end_edittext.getText().toString();
                book_diary_plus_memory = book_diary_plus_memory_edittext.getText().toString();
                book_diary_plus_think = book_diary_plus_think_edittext.getText().toString();

                if (book_diary_plus_read_start == null || book_diary_plus_read_end == null) {
                    Toast.makeText(book_diary_plus.this, "읽은 페이지를 적어주세요", Toast.LENGTH_SHORT).show();
                } else if (book_diary_plus_memory == null && book_diary_plus_think == null) {
                    Toast.makeText(book_diary_plus.this, "둘중 하나는 적어주세요", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(book_diary_plus_read_start) >= Integer.parseInt(book_diary_plus_read_end)) {
                    Toast.makeText(book_diary_plus.this, "읽은 페이지를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(book_diary_plus_read_end) > Integer.parseInt(book_page)) {
                    Toast.makeText(book_diary_plus.this, "책 페이지를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    //제목값,시간값,이메일값
                    mNow = System.currentTimeMillis();
                    mDate = new Date(mNow);
                    String date = mFormat.format(mDate);
                    System.out.println(date + "datedate");//날짜값 확인
                    book_diary_plus_insert(book_subject, date, email, book_diary_plus_read_start, book_diary_plus_read_end, book_diary_plus_memory, book_diary_plus_think);
                }
            }
        });
    }


    private void book_diary_plus_insert(String book_subject, String date, String email, String book_diary_plus_read_start, String book_diary_plus_read_end, String book_diary_plus_memory, String book_diary_plus_think) {
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<home_data> call = search_info_insert.book_diary_plus_insert(book_subject, date, email, book_diary_plus_read_start, book_diary_plus_read_end, book_diary_plus_memory, book_diary_plus_think);
        call.enqueue(new Callback<home_data>() {
            @Override
            public void onResponse(Call<home_data> call, Response<home_data> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onBackPressed();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<home_data> call, Throwable t) {
                onBackPressed();
            }
        });
    }

}