package org.techtown.reiview_app.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.record.record;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class home extends Fragment {
    View view;

    TextView home_book_not_read_textview;
    RecyclerView home_book_recyclerview;
    String email, blog_name, nick, blog_text, blog_image;
    List<home_data> list = new ArrayList<>();
    home_adapter adapter;
    home_adapter.ItemClickListener itemClickListener;
    Button home_book_plus_button;
    public Calendar cal = Calendar.getInstance();
    NestedScrollView nestedScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        int month_home = cal.get(Calendar.MONTH) + 1;
        record.month =month_home;
//        home_book_plus_button = (Button) view.findViewById(R.id.home_book_plus_button);
        home_book_recyclerview = (RecyclerView) view.findViewById(R.id.home_book_recyclerview);
        home_book_not_read_textview = (TextView) view.findViewById(R.id.home_book_not_read_textview);


        SharedPreferences pref = getActivity().getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");
        book_home_recyclerview_select(email);


        SharedPreferences pref2 = getActivity().getSharedPreferences("최근검색어" + email, MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref2.edit();
        editor2.remove("검색어");
        editor2.apply();

        SharedPreferences pref4 = getActivity().getSharedPreferences("home_select" + email, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref4.edit();
        editor.putString("번호", "1");
        editor.apply();

        SharedPreferences read_time_setting = getActivity().getSharedPreferences("time_setting" + email, MODE_PRIVATE);
        SharedPreferences.Editor editor3 = read_time_setting.edit();
        editor3.putString("독서시간", "1분");
        editor3.apply();


        home_book_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        itemClickListener = new home_adapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int idx = list.get(position).getIdx();
                String book_subject = list.get(position).getBook_subject();
                String make = list.get(position).getMake();
                String writer = list.get(position).getWriter();
                String image = list.get(position).getImage();
                int read_time = list.get(position).getRead_time();
                int read_page = list.get(position).getRead_page();
                String book_page = list.get(position).getBook_page();

                Log.e("idx 데이터 확인", String.valueOf(idx));

                Intent intent = new Intent(getContext(), home_select.class);
                intent.putExtra("book_subject", book_subject);
                intent.putExtra("make", make);
                intent.putExtra("writer", writer);
                intent.putExtra("image", image);
                intent.putExtra("idx", idx);
                intent.putExtra("read_time", read_time);
                intent.putExtra("read_page", String.valueOf(read_page));
                intent.putExtra("book_page", book_page);

                startActivity(intent);
            }
        };
        return view;
    }


    // INSERT 구문 은 WHERE 절을 지원하지 않으므로 쿼리가 실패합니다. id열이 고유하거나 기본 키 라고 가정합니다 .
    private void book_home_recyclerview_select(String email) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_home_recyclerview_select(email, null);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("연결은 되나???");
                    onGetResult(response.body());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<home_data>> call, Throwable t) {
                Log.e("book_home_recyclerview_select()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult(List<home_data> lists) {
        if (lists.size() == 0) {
            home_book_recyclerview.setVisibility(View.GONE);
            home_book_not_read_textview.setVisibility(View.VISIBLE);
        } else {
            home_book_recyclerview.setVisibility(View.VISIBLE);
            home_book_not_read_textview.setVisibility(View.GONE);
            adapter = new home_adapter(getActivity(), lists, itemClickListener);
            adapter.notifyDataSetChanged();
            home_book_recyclerview.setAdapter(adapter);
            list = lists;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        book_home_recyclerview_select(email);

        SharedPreferences pref4 = getActivity().getSharedPreferences("home_select" + email, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref4.edit();
        editor.putString("번호", "1");
        editor.apply();
    }
}