package org.techtown.reiview_app.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.techtown.reiview_app.MainActivity;
import org.techtown.reiview_app.MainActivity2;
import org.techtown.reiview_app.R;

public class book_read_finish extends AppCompatActivity {
    TextView book_read_finish_today_time_textview,book_read_finish_all_time_textview,book_read_finish_read_book_page_textview,book_read_finish_all_read_book_page_textview;
    int today_read_time,all_time_read_time,today_read_book_page;
    String all_read_book_page,book_page,read_page_intent;
    Button book_page_check_next_button,book_page_check_diary_button;
    org.techtown.reiview_app.home.home home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_read_finish);

        book_read_finish_today_time_textview = (TextView) findViewById(R.id.book_read_finish_today_time_textview);
        book_read_finish_all_time_textview = (TextView) findViewById(R.id.book_read_finish_all_time_textview);
        book_read_finish_read_book_page_textview = (TextView) findViewById(R.id.book_read_finish_read_book_page_textview);
        book_read_finish_all_read_book_page_textview = (TextView) findViewById(R.id.book_read_finish_all_read_book_page_textview);
        book_page_check_next_button = (Button) findViewById(R.id.book_page_check_next_button);
        book_page_check_diary_button = (Button) findViewById(R.id.book_page_check_diary_button);


        Intent intent_get_data = getIntent();
        today_read_time = intent_get_data.getIntExtra("today_read_time",0);
        all_time_read_time = intent_get_data.getIntExtra("all_time_read_time",0);
        today_read_book_page = intent_get_data.getIntExtra("read_book_page",0);
        all_read_book_page = intent_get_data.getStringExtra("all_read_book_page");
        book_page = intent_get_data.getStringExtra("book_page");
        read_page_intent = intent_get_data.getStringExtra("read_page_intent");


        String all_time_read_time_String = String.valueOf(all_time_read_time);
        String today_read_book_page_String = String.valueOf(today_read_book_page);

        int hrs = today_read_time / 3600;
        int mins = (today_read_time % 3600) / 60;
        int sec = today_read_time % 60;
        String today_time = String.format("%02d:%02d:%02d", hrs, mins, sec);


        int hrs_all = all_time_read_time / 3600;
        int mins_all = (all_time_read_time % 3600) / 60;
        int sec_all = all_time_read_time % 60;
        String time_all = String.format("%02d:%02d:%02d", hrs_all, mins_all, sec_all);

        book_read_finish_today_time_textview.setText(today_time);
        book_read_finish_all_time_textview.setText(time_all);
        book_read_finish_read_book_page_textview.setText(today_read_book_page_String+" page");
        book_read_finish_all_read_book_page_textview.setText(all_read_book_page+" / "+book_page+" page");


        book_page_check_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(book_read_finish.this, MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        book_page_check_diary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(book_read_finish.this, book_diary_plus.class);
                startActivity(intent);
                finish();
            }
        });
    }
}