package org.techtown.reiview_app.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class book_diary_update extends AppCompatActivity {
    EditText book_diary_update_read_start_edittext, book_diary_update_read_end_edittext, book_diary_update_memory_edittext, book_diary_update_think_edittext;
    Button book_diary_update_button;
    int idx;
String book_diary_update_read_start,book_diary_update_read_end,book_diary_update_memory,book_diary_update_think;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_diary_update);

        Intent intent = getIntent();
        idx = intent.getIntExtra("idx", 0);
        book_diary_select_second(idx);

        book_diary_update_read_start_edittext = (EditText) findViewById(R.id.book_diary_update_read_start_edittext);
        book_diary_update_read_end_edittext = (EditText) findViewById(R.id.book_diary_update_read_end_edittext);
        book_diary_update_memory_edittext = (EditText) findViewById(R.id.book_diary_update_memory_edittext);
        book_diary_update_think_edittext = (EditText) findViewById(R.id.book_diary_update_think_edittext);
        book_diary_update_button = (Button) findViewById(R.id.book_diary_update_button);


        book_diary_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_diary_update_read_start = book_diary_update_read_start_edittext.getText().toString();
                book_diary_update_read_end = book_diary_update_read_end_edittext.getText().toString();
                book_diary_update_memory = book_diary_update_memory_edittext.getText().toString();
                book_diary_update_think = book_diary_update_think_edittext.getText().toString();

                if (book_diary_update_read_start == null || book_diary_update_read_end == null) {
                    Toast.makeText(book_diary_update.this, "읽은 페이지를 적어주세요", Toast.LENGTH_SHORT).show();
                } else if (book_diary_update_memory == null && book_diary_update_think == null) {
                    Toast.makeText(book_diary_update.this, "둘중 하나는 적어주세요", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(book_diary_update_read_start) >= Integer.parseInt(book_diary_update_read_end)) {
                    Toast.makeText(book_diary_update.this, "읽은 페이지를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    book_diary_update(idx, book_diary_update_read_start, book_diary_update_read_end, book_diary_update_memory, book_diary_update_think);
                }
            }
        });
    }


    private void book_diary_select_second(int idx) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_diary_select_second(idx);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    book_diary_update_read_start_edittext.setText(response.body().get(0).getStart_page());
                    book_diary_update_read_end_edittext.setText(response.body().get(0).getEnd_page());

                    if (response.body().get(0).getMemory_content().equals("")) {

                    } else {
                        book_diary_update_memory_edittext.setText(response.body().get(0).getMemory_content());

                    }
                    if (response.body().get(0).getAfter_content().equals("")) {
                    } else {
                        book_diary_update_think_edittext.setText(response.body().get(0).getAfter_content());
                    }

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<home_data>> call, Throwable t) {
                Log.e("book_home_recyclerview_select()", "에러 : " + t.getMessage());
            }
        });
    }


    private void book_diary_update(int idx, String book_diary_update_read_start, String book_diary_update_read_end, String book_diary_update_memory, String book_diary_update_think) {
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_info_insert.book_diary_update(idx,book_diary_update_read_start,book_diary_update_read_end,book_diary_update_memory,book_diary_update_think);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call,  Response<String> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onBackPressed();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure( Call<String> call,  Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
                onBackPressed();
            }
        });
    }
}