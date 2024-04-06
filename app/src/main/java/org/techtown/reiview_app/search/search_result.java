package org.techtown.reiview_app.search;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.ThreadEx;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class search_result extends AppCompatActivity {
    EditText search_result_edittext;
    ImageView search_result_cancel_imageview, search_result_search_imageview;
    RecyclerView search_result_book_recyclerview;
    TextView search_result_book_x_textview;
    String book_name_intent, email;
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;
    private search_result_adapter adapter = new search_result_adapter();    // adapter 생성
    private ArrayList<book_data> list = new ArrayList<>();
    private ProgressDialog pBar;
    private ThreadEx threadEx;
    String book_name;
    // 1페이지에 10개씩 데이터를 불러온다
    int page = 1, limit = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        //프로그래스바는 기본적으로 원형을 제공한다.


        pBar = ProgressDialog.show(search_result.this//Context
                , ""//타이틀
                , "검색 중입니다..."//메세지
        );
        pBar.getWindow().setGravity(Gravity.CENTER);

        //도중 취소 여부
        pBar.setCancelable(false);


        threadEx = new ThreadEx(handler);
        threadEx.start();//Thread 실행


        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");
        Intent intent = getIntent();
        book_name = intent.getStringExtra("book_name");

        getData(page, limit, book_name);

        nestedScrollView = findViewById(R.id.scroll_view);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    getData(page, limit, book_name);
                }
            }
        });


        search_result_edittext = (EditText) findViewById(R.id.search_result_edittext);
        search_result_cancel_imageview = (ImageView) findViewById(R.id.search_result_cancel_imageview);
        search_result_search_imageview = (ImageView) findViewById(R.id.search_result_search_imageview);
        search_result_book_recyclerview = (RecyclerView) findViewById(R.id.search_result_book_recyclerview);
        search_result_book_x_textview = (TextView) findViewById(R.id.search_result_book_x_textview);

        search_result_book_x_textview.setVisibility(View.INVISIBLE);

        search_result_book_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        search_result_book_recyclerview.setLayoutManager(linearLayoutManager);

        //===== [Click 이벤트 구현을 위해 추가된 코드] ==============
        adapter.setOnItemClickListener(new search_result_adapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, String image, String book_subject, String make, String date, String book_content, String isbn, String book_page, String author,String itemId) {
                System.out.println("image:" + image);
                System.out.println("book_subject:" + book_subject);
                System.out.println("make:" + make);
                System.out.println("date:" + date);
                System.out.println("book_content:" + book_content);
                System.out.println("isbn:" + isbn);
                System.out.println("book_page:" + book_page);
                System.out.println("itemId:" + itemId);

                Intent intent = new Intent(search_result.this, search_select.class);
                intent.putExtra("image", image);
                intent.putExtra("book_subject", book_subject);
                intent.putExtra("make", make);
                intent.putExtra("date", date);
                intent.putExtra("book_content", book_content);
                intent.putExtra("isbn", isbn);
                intent.putExtra("book_page", book_page);
                intent.putExtra("author", author);
                intent.putExtra("itemId", itemId);
                startActivity(intent);

            }
        });

        //==========================================================
        // recyclerview에 adapter 적용
        search_result_book_recyclerview.setAdapter(adapter);


        search_result_edittext.setText(book_name);
        search_result_cancel_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_result_edittext.setText("");
            }
        });

        search_result_search_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_name_intent = search_result_edittext.getText().toString();

                SharedPreferences pref = getSharedPreferences("최근검색어"+email, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("검색어",book_name_intent);
                editor.apply();

                search_info_insert(email, book_name_intent);
                Intent intent = new Intent(search_result.this, search_result.class);
                intent.putExtra("book_name", book_name_intent);
                startActivity(intent);
                finish();
            }
        });


    }

    private void search_info_insert(String email, String book_name) {
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_info_insert.search_info_insert(email, book_name);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            int value = msg.getData().getInt("value");

            pBar.setProgress(value);

            super.handleMessage(msg);

            if (value >= 100) {
                pBar.dismiss();
                threadEx.setCheck(false);
            }
        }
    };

    private void getData(int page, int limit, String book_name) {

        String urlAddress = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbsszz071522003&Query=" + book_name + "&QueryType=Title&MaxResults=" + limit + "&start=" + page + "&SearchTarget=Book&output=js&Version=20070901";
        System.out.println(urlAddress+"urlAddress");
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAddress);

                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();


                    while (line != null) {
                        buffer.append(line + "\n");
                        line = reader.readLine();
                    }
                    String jsonData = buffer.toString();
                    System.out.println(jsonData.length() + "jsonData.length()");


                    // jsonData를 먼저 JSONObject 형태로 바꾼다.
                    JSONObject obj = new JSONObject(jsonData);

                    // boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출
                    JSONArray dailyBoxOfficeList = (JSONArray) obj.get("item");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //검색한 내용이 존재한다면
                                search_result_book_x_textview.setText("");
                                if (dailyBoxOfficeList.length() != 0) {
                                    // dailyBoxOfficeList의 length만큼 for문 반복
                                    for (int i = 0; i < dailyBoxOfficeList.length(); i++) {
                                        // dailyBoxOfficeList를 각 JSONObject 형태로 객체를 생성한다.
                                        JSONObject temp = dailyBoxOfficeList.getJSONObject(i);

                                        list.add(new book_data(temp.getString("title"), temp.getString("author"), temp.getString("publisher"), temp.getString("pubDate"), temp.getString("cover"), temp.getString("isbn"), temp.getString("description"), temp.getString("itemId")));

                                        // list의 json 값들을 넣는다.
                                    }
                                    // adapter에 적용
                                    adapter.setmovieList(search_result.this, list);
                                } else if(dailyBoxOfficeList.length() == 0) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    search_result_book_x_textview.setVisibility(View.VISIBLE);

                                    /**
                                     * 오류-책 제목을 검색을 했을때 계속 밑으로 스크롤 했을때 로딩 바와 "검색한 내용이 없습니다"의 값이 보이는 현상이 있었다
                                     * 왜 오류를 냈을까? 책 정보의 값이 스크롤을 내린 다음을 생각을 못했다
                                     * 해결-ArrayList<book_data> list = new ArrayList<>(); list.size() == 0을
                                     * */
                                    if(list.size() == 0){
                                        search_result_book_x_textview.setText("검색한 내용이 없습니다");
                                    }
                                }
                                //존재하지 않는다면


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}