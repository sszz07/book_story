package org.techtown.reiview_app.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.login_data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class search_follwer extends AppCompatActivity {
    EditText search_follwer_edittext;
    ImageView search_follwer_cancel_imageview;
    ImageView search_follwer_search_imageview;
    RecyclerView search_follwer_search_recyclerview, search_follwer_search_x_recyclerview;
    String email;
    search_follwer_adapter search_follwer_adapter;
    List<login_data> follower_list, filteredList;
    NestedScrollView nestedScrollView;
    int page = 0, limit = 10;
    ProgressBar search_follower_progress_bar;
    MutableLiveData<Integer> score = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_follwer);


        search_follower_progress_bar = (ProgressBar) findViewById(R.id.search_follower_progress_bar);

        search_follwer_edittext = (EditText) findViewById(R.id.search_follwer_edittext);
        search_follwer_cancel_imageview = (ImageView) findViewById(R.id.search_follwer_cancel_imageview);
        search_follwer_search_imageview = (ImageView) findViewById(R.id.search_follwer_search_imageview);
        search_follwer_search_recyclerview = (RecyclerView) findViewById(R.id.search_follwer_search_recyclerview);
        search_follwer_search_x_recyclerview = (RecyclerView) findViewById(R.id.search_follwer_search_x_recyclerview);

        search_follwer_search_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        search_follwer_search_x_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        filteredList = new ArrayList<>();
        follower_list = new ArrayList<>();

        Intent intent = getIntent();
        email = intent.getStringExtra("other_email");

        if (email == null) {
            SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
            String email = pref.getString("이메일", "");
            search_follwer_info_select2(email, page, limit);

        } else {
            search_follwer_info_select2(email, page, limit);
        }
        //검색을 하지 않았을때
        score.setValue(2);

        //시작했을때는 없어짐
        search_follwer_search_recyclerview.setVisibility(View.GONE);
        search_follwer_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable ewditable) {
                if (ewditable.length() == 0) {
                    //검색을 한뒤에 데이터값이 포멧이 된다
                    score.setValue(2);
                    search_follwer_info_select2(email, page, limit);
                    search_follwer_cancel_imageview.setVisibility(View.INVISIBLE);

                    search_follwer_search_recyclerview.setVisibility(View.VISIBLE);
                } else {
                    score.setValue(1);
                    search_follwer_search_recyclerview.setVisibility(View.GONE);//검색기능을 했을때 다른 리사이클러뷰 사용
                    search_follwer_cancel_imageview.setVisibility(View.VISIBLE);
                    String searchText = search_follwer_edittext.getText().toString();
                    searchFilter(searchText);
                }
            }
        });


        //edittext 텍스트 전부삭제하기
        search_follwer_cancel_imageview.setVisibility(View.INVISIBLE);
        search_follwer_cancel_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_follwer_edittext.setText("");
            }
        });
    }


    private void search_follwer_info_select(String email) {
        System.out.println(email + "email값 확인_search_follwer_info_select");
        ApiInterface search_info_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<login_data>> call = search_info_select.search_follwer_info_select(email);
        call.enqueue(new Callback<List<login_data>>() {
            @Override
            public void onResponse(Call<List<login_data>> call, Response<List<login_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onGetResult_follwer(response.body());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<login_data>> call, Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult_follwer(List<login_data> lists) {
        if (lists.size() == 0) {
        } else {

            search_follwer_adapter = new search_follwer_adapter(this, lists);
            search_follwer_adapter.notifyDataSetChanged();
            search_follwer_search_recyclerview.setAdapter(search_follwer_adapter);
            follower_list = lists;
        }
    }


    private void search_follwer_info_select2(String email, int page, int limit) {
        System.out.println(email + "email값 확인_search_follwer_info_select");
        ApiInterface search_info_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<login_data>> call = search_info_select.search_follwer_info_select2(email, page, limit);
        call.enqueue(new Callback<List<login_data>>() {
            @Override
            public void onResponse(Call<List<login_data>> call, Response<List<login_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onGetResult_follwer2(response.body());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<login_data>> call, Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult_follwer2(List<login_data> lists) {
        if (lists.size() == 0) {
            search_follower_progress_bar.setVisibility(View.INVISIBLE);
        } else {
            search_follower_progress_bar.setVisibility(View.INVISIBLE);
            search_follwer_adapter = new search_follwer_adapter(this, lists);
            search_follwer_adapter.notifyDataSetChanged();
            search_follwer_search_x_recyclerview.setAdapter(search_follwer_adapter);
            follower_list = lists;
        }
    }


    public void searchFilter(String searchText) {
        filteredList.clear();
        for (int i = 0; i < follower_list.size(); i++) {
            if (follower_list.get(i).getNick().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(follower_list.get(i));
            }
        }
        search_follwer_adapter.filterList(filteredList);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (score.getValue() == 2) {
            search_follwer_info_select2(email, page, limit);
            nestedScrollView = findViewById(R.id.scroll_view);
            search_follower_progress_bar.setVisibility(View.INVISIBLE);
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        limit += 10;
                        search_follower_progress_bar.setVisibility(View.VISIBLE);
                        search_follwer_info_select2(email, page, limit);

                    }
                }
            });
        } else if (score.getValue() == 1) {
            search_follwer_info_select(email);
        }
    }
}