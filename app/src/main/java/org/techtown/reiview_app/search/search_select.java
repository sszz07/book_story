package org.techtown.reiview_app.search;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.MainActivity;
import org.techtown.reiview_app.MainActivity2;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.home.home_adapter;
import org.techtown.reiview_app.home.home_data;
import org.techtown.reiview_app.login_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class search_select extends AppCompatActivity {
    ImageView search_select_book_imageview, search_select_want_read_imageview, search_select_now_read_imageview, search_select_want_read_cancel_imageview, search_select_now_read_cancel_imageview;
    TextView search_select_book_name_textview, search_select_book_make_textview, search_select_want_read_textview, search_select_now_read_textview;
    TextView search_select_writer_textview, search_select_make_date_textview, search_select_book_content_textview, search_select_page_textview;

    String email, book_name, image, book_subject, make, date, book_content, isbn, book_page, author,itemId;
    String book_name_server;

    int book_number;

    Button search_select_book_naver_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_select);


        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        book_home_recyclerview_select(email);
        Intent intent = getIntent();
        image = intent.getStringExtra("image");
        book_subject = intent.getStringExtra("book_subject");
        make = intent.getStringExtra("make");
        date = intent.getStringExtra("date");
        book_content = intent.getStringExtra("book_content");
        isbn = intent.getStringExtra("isbn");
        book_page = intent.getStringExtra("book_page");
        author = intent.getStringExtra("author");
        itemId = intent.getStringExtra("itemId");


        book_now_read_select(email, book_subject);
        System.out.println(book_subject + "book_subject");
        System.out.println(isbn + "isbn");
        System.out.println(itemId + "itemIditemId");

        book_want_read_select(email, book_subject);

        search_select_book_imageview = (ImageView) findViewById(R.id.search_select_book_imageview);


        search_select_now_read_cancel_imageview = (ImageView) findViewById(R.id.search_select_now_read_cancel_imageview);

        search_select_want_read_imageview = (ImageView) findViewById(R.id.search_select_want_read_imageview);
        search_select_now_read_imageview = (ImageView) findViewById(R.id.search_select_now_read_imageview);
        search_select_want_read_cancel_imageview = (ImageView) findViewById(R.id.search_select_want_read_cancel_imageview);


        search_select_book_name_textview = (TextView) findViewById(R.id.search_select_book_name_textview);
        search_select_book_make_textview = (TextView) findViewById(R.id.search_select_book_make_textview);


        search_select_want_read_textview = (TextView) findViewById(R.id.search_select_want_read_textview);
        search_select_now_read_textview = (TextView) findViewById(R.id.search_select_now_read_textview);


        search_select_writer_textview = (TextView) findViewById(R.id.search_select_writer_textview);
        search_select_make_date_textview = (TextView) findViewById(R.id.search_select_make_date_textview);
        search_select_book_content_textview = (TextView) findViewById(R.id.search_select_book_content_textview);
        search_select_page_textview = (TextView) findViewById(R.id.search_select_page_textview);

        search_select_book_naver_button = (Button) findViewById(R.id.search_select_book_naver_button);


        //읽고 싶어요가 하고싶을때
        search_select_want_read_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_want_read_insert(image, book_subject, make, author, date, book_content, email, book_page);
                book_now_read_delete(email,book_subject);
                //플러스 버튼 사라짐
                search_select_want_read_imageview.setVisibility(View.INVISIBLE);
                search_select_want_read_cancel_imageview.setVisibility(View.VISIBLE);

                search_select_now_read_cancel_imageview.setVisibility(View.INVISIBLE);

                //글씨색변경
                search_select_want_read_textview.setTextColor(Color.parseColor("#FF5050"));

                //읽을래요
                search_select_now_read_imageview.setVisibility(View.VISIBLE);
                search_select_now_read_textview.setTextColor(Color.BLACK);
                Toast.makeText(search_select.this, "읽고 싶은책에 추가 되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        //읽고 싶어요 취소했을때
        search_select_want_read_cancel_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences(book_subject+email, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref.edit();
                editor2.remove("이메일");
                editor2.apply();

                book_want_read_delete(email, book_subject);
                book_now_read_delete(email,book_subject);
                //플러스 버튼 사라짐
                //플러스 버튼 사라짐
                search_select_want_read_imageview.setVisibility(View.VISIBLE);
                search_select_want_read_cancel_imageview.setVisibility(View.INVISIBLE);
                //글씨색변경
                search_select_want_read_textview.setTextColor(Color.BLACK);

                //읽을래요
                search_select_now_read_imageview.setVisibility(View.VISIBLE);
                search_select_now_read_textview.setTextColor(Color.BLACK);
                Toast.makeText(search_select.this, "읽고 싶은책에 취소가 되었습니다", Toast.LENGTH_SHORT).show();
            }
        });


        //읽을래요를 했을때
        search_select_now_read_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (book_number > 9) {
                    Toast.makeText(search_select.this, "10권 이상 읽을수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    book_want_read_delete(email, book_subject);
                    book_now_read_insert(image, book_subject, make, author, book_page, email);

                    search_select_want_read_textview.setTextColor(Color.BLACK);
                    search_select_want_read_imageview.setVisibility(View.VISIBLE);
                    search_select_want_read_cancel_imageview.setVisibility(View.INVISIBLE);

                    search_select_now_read_imageview.setVisibility(View.INVISIBLE);
                    search_select_now_read_cancel_imageview.setVisibility(View.VISIBLE);

                    search_select_now_read_textview.setTextColor(Color.parseColor("#FF5050"));
                    Toast.makeText(search_select.this, "읽을책 추가 되었습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        읽을래요를 안했을때
        search_select_now_read_cancel_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_now_read_delete(email,book_subject);
                search_select_now_read_imageview.setVisibility(View.VISIBLE);
                search_select_now_read_cancel_imageview.setVisibility(View.VISIBLE);
                search_select_now_read_cancel_imageview.setVisibility(View.INVISIBLE);

                search_select_now_read_textview.setTextColor(Color.BLACK);
                Toast.makeText(search_select.this, "읽을책 취소 되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        //책제목
        search_select_book_name_textview.setText(book_subject);
        //출판사
        search_select_book_make_textview.setText(make);
        //날짜
        search_select_make_date_textview.setText(date);
        //책 내용
        if(book_content.equals("")){
            search_select_book_content_textview.setText("책 소개가 없습니다");
        }else{
            search_select_book_content_textview.setText(book_content);
        }
        //책 페이지
        search_select_page_textview.setText(book_page);
        //작가
        search_select_writer_textview.setText(author);

        Glide.with(this)
                .load(image)
                .into(search_select_book_imageview);


        //네이버 도서 홈페이지 이동 코드
        search_select_book_naver_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msearch.shopping.naver.com/search/all?query="+book_subject));
                startActivity(intent);
            }
        });

    }


    //티스토리 유저 만들기
    @SuppressLint("LongLogTag")
    private void book_now_read_insert(String image, String book_subject, String make, String writer, String book_page, String email ) {
        System.out.println(image+"image");
        System.out.println(book_subject+"book_subject");
        System.out.println(make+"make");
        System.out.println(writer+"writer");
        System.out.println(book_page+"book_page");
        System.out.println(email+"email");

        ApiInterface member_join = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = member_join.book_now_read_insert(image, book_subject, make, writer, book_page, email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call,  Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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

    private void book_now_read_delete(String email, String book_subject) {
        ApiInterface search_delete = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_delete.book_now_read_delete(email, book_subject);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call,  Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //티스토리 유저 만들기
    @SuppressLint("LongLogTag")
    private void book_now_read_select(String email, String book_subject) {
        ApiInterface login = ApiClient.getClient().create(ApiInterface.class);
        Call<List<search_data>> call = login.book_now_read_select(email, book_subject);
        call.enqueue(new Callback<List<search_data>>() {
            @Override
            public void onResponse( Call<List<search_data>> call,  Response<List<search_data>> response) {
                try {
                    book_name_server = response.body().get(0).getBook_subject();
                    if (book_name_server != null) {
                        System.out.println("여기1111");
                        search_select_now_read_imageview.setVisibility(View.INVISIBLE);
                        search_select_now_read_cancel_imageview.setVisibility(View.VISIBLE);
                        search_select_now_read_textview.setTextColor(Color.parseColor("#FF5050"));
                    }

                } catch (IndexOutOfBoundsException e) {
                    search_select_now_read_imageview.setVisibility(View.VISIBLE);
                    search_select_now_read_cancel_imageview.setVisibility(View.INVISIBLE);
                    search_select_now_read_textview.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onFailure(Call<List<search_data>> call, Throwable t) {
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


    @SuppressLint("LongLogTag")
//    -이미지ㅇ,제목ㅇ,출판사ㅇ,출간일x,책 소개x 데이터 값 넣기

    private void book_want_read_insert(String image, String book_subject, String make, String writer, String date, String book_content, String email, String book_page) {
        System.out.println(book_page + "book_pagebook_page");
        ApiInterface member_join = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = member_join.book_want_read_insert(image, book_subject, make, writer, date, book_content, email, book_page);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call,  Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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


                /**
                 * 에러-Expected a string but was BEGIN_OBJECT at line 1 column 2 path $
                 * 이유-db의 테이블 네임값이 맞지 않아서
                 * */

                Log.e("t_story_make_user_insert()", "에러 : " + t.getMessage());
            }
        });
    }


    //티스토리 유저 만들기
    @SuppressLint("LongLogTag")
    private void book_want_read_select(String email, String book_subject) {
        ApiInterface login = ApiClient.getClient().create(ApiInterface.class);
        Call<List<search_data>> call = login.book_want_read_select(email, book_subject);
        call.enqueue(new Callback<List<search_data>>() {
            @Override
            public void onResponse( Call<List<search_data>> call,  Response<List<search_data>> response) {
                search_select_want_read_imageview.setVisibility(View.VISIBLE);
                search_select_want_read_cancel_imageview.setVisibility(View.INVISIBLE);
                search_select_want_read_textview.setTextColor(Color.BLACK);
                try {
                    book_name_server = response.body().get(0).getBook_subject();
                    System.out.println(book_name_server + "book_name_serverbook_name_serverbook_name_server");
                    //책이 존재했을때
                    if (book_name_server != null) {
                        System.out.println("여기로 들어노나??");
                        search_select_want_read_imageview.setVisibility(View.INVISIBLE);
                        search_select_want_read_cancel_imageview.setVisibility(View.VISIBLE);
                        search_select_want_read_textview.setTextColor(Color.parseColor("#FF5050"));
                    }

                } catch (IndexOutOfBoundsException e) {

                }
            }

            @Override
            public void onFailure(Call<List<search_data>> call, Throwable t) {
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


    private void book_want_read_delete(String email, String book_subject) {
        ApiInterface search_delete = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_delete.book_want_read_delete(email, book_subject);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call,  Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private void book_home_recyclerview_select(String email) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_home_recyclerview_select(email, null);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse( Call<List<home_data>> call,  Response<List<home_data>> response) {
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
        System.out.println(lists.size() + "lists.size()lists.size()lists.size()");
        book_number = lists.size();
    }

}