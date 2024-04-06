package org.techtown.reiview_app.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;


import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.login_data;
import org.techtown.reiview_app.record.record;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class search extends Fragment {
    View view;
    EditText search_record_edittext;
    ImageView search_cancel_imageview;
    ImageView search_record_search_imageview;
    String book_name, email;
    RecyclerView search_record_recyclerview;
    search_adapter adapter;
    search_adapter.ItemClickListener itemClickListener;

    friends_adapter friends_adapter;
    friends_adapter.ItemClickListener friends_itemClickListener;

    List<search_data> search_list = new ArrayList<>();
    List<login_data> friends_list, filteredList;

    TextView  search_record_book_textview, search_record_book_line_textview, search_record_friends_textview, search_record_friends_line_textview;
    Button search_all_remove_button;

    public Calendar cal = Calendar.getInstance();

    NestedScrollView nestedScrollView;
    int page = 0, limit = 10;
    ProgressBar search_progress_bar;
    MutableLiveData<Integer> score = new MutableLiveData<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
         * 쉐어드로 최근 검색어 저장하기
         * onresume에 검색어 settext() 값 넣기
         * */

        view = inflater.inflate(R.layout.fragment_search, container, false);

        friends_list = new ArrayList<>();
        //검색 필터 어레이 리스트
        filteredList = new ArrayList<>();





        int month_home = cal.get(Calendar.MONTH) + 1;
        record.month = month_home;
//        search_last_textview = (TextView) view.findViewById(R.id.search_last_textview);

        //새로 추가하는 뷰
        search_record_book_textview = (TextView) view.findViewById(R.id.search_record_book_textview);
        search_record_book_line_textview = (TextView) view.findViewById(R.id.search_record_book_line_textview);
        search_record_friends_textview = (TextView) view.findViewById(R.id.search_record_friends_textview);
        search_record_friends_line_textview = (TextView) view.findViewById(R.id.search_record_friends_line_textview);
        //

        search_progress_bar = (ProgressBar) view.findViewById(R.id.search_progress_bar);
        score.setValue(2);


        search_record_edittext = (EditText) view.findViewById(R.id.search_record_edittext);
        search_cancel_imageview = (ImageView) view.findViewById(R.id.search_cancel_imageview);
        search_record_search_imageview = (ImageView) view.findViewById(R.id.search_record_search_imageview);
        search_record_recyclerview = (RecyclerView) view.findViewById(R.id.search_record_recyclerview);
//        search_record_friends_x_recyclerview = (RecyclerView) view.findViewById(R.id.search_record_friends_x_recyclerview);

        search_all_remove_button = (Button) view.findViewById(R.id.search_all_remove_button);

        search_record_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        search_record_friends_x_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        search_cancel_imageview.setVisibility(View.INVISIBLE);


        //첫 화면 시작 했을때
        search_record_friends_textview.setTextColor(Color.parseColor("#828282"));
        search_record_friends_line_textview.setTextColor(Color.parseColor("#828282"));

        //도서 텍스트뷰를 선택을 했을때
        search_record_book_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_record_friends_textview.setTextColor(Color.parseColor("#828282"));
                search_record_friends_line_textview.setTextColor(Color.parseColor("#828282"));

                search_record_book_textview.setTextColor(Color.BLACK);
                search_record_book_line_textview.setTextColor(Color.BLACK);

//                search_last_textview.setText("최근 검색어");

                search_all_remove_button.setVisibility(View.VISIBLE);

                search_record_recyclerview.setVisibility(View.VISIBLE);
//                search_record_friends_recyclerview.setVisibility(View.GONE);

                search_record_edittext.setText("");
                search_record_edittext.setHint("친구이름을 검색해주세요");
            }
        });

        //친구 텍스트뷰를 선택을 했을때
//        search_record_friends_recyclerview.setVisibility(View.GONE);
        search_record_friends_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_record_book_textview.setTextColor(Color.parseColor("#828282"));
                search_record_book_line_textview.setTextColor(Color.parseColor("#828282"));

                search_record_friends_textview.setTextColor(Color.BLACK);
                search_record_friends_line_textview.setTextColor(Color.BLACK);

//                search_last_textview.setText("최근 피드글 올린 유저");
                search_all_remove_button.setVisibility(View.GONE);
                search_record_recyclerview.setVisibility(View.GONE);
//                search_record_friends_recyclerview.setVisibility(View.VISIBLE);

                search_record_edittext.setText("");
                search_record_edittext.setHint("책 이름을 검색해주세요");

            }
        });


        /**
         * 오류-Edittext에 텍스트를 입력을 했을때 입력한 텍스트를 한번에 지우기 위해서 x표시 이미지뷰를 만들었는데 일반적으로 텍스트가 없으면 x표시가 보이지 않게 된다
         * 하지만 내 앱에서는 보이는 현상이 일어났다
         *
         * 왜 오류가 났을까? 텍스트를 지운다는 생각만 해서 x표시 이미지뷰를 버튼으로만 만들었다
         *
         * 해결-addTextChangedListener 메소드를 이용해서 if (ewditable.length() == 0) {
         *                     search_cancel_imageview.setVisibility(View.INVISIBLE);
         *                 } else {
         *                     search_cancel_imageview.setVisibility(View.VISIBLE);
         *                 }
         *                 조건문으로 eidttext의 값이 0일때와 아닐때를 만들었다
         * */

        search_cancel_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_record_edittext.setText("");
            }
        });

//        search_record_friends_recyclerview.setVisibility(View.GONE);

        search_record_edittext.addTextChangedListener(new TextWatcher() {
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
                    search_friends_info_select2(email,page,limit);

                    search_cancel_imageview.setVisibility(View.INVISIBLE);
//                    search_record_friends_recyclerview.setVisibility(View.VISIBLE);

                } else {
//                    search_record_friends_recyclerview.setVisibility(View.GONE);

                    search_cancel_imageview.setVisibility(View.VISIBLE);
                    String searchText = search_record_edittext.getText().toString();
                    searchFilter(searchText);
                }
            }
        });


        SharedPreferences pref = getActivity().getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");
        search_info_select(email);
//        search_friends_info_select(email);

        search_record_search_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_name = search_record_edittext.getText().toString();
                if (book_name.length() == 0) {
                    Toast.makeText(getActivity(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {

                    search_record_edittext.clearFocus();

                    SharedPreferences pref2 = getActivity().getSharedPreferences("최근검색어" + email, MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = pref2.edit();
                    editor2.putString("검색어", book_name);
                    editor2.apply();

                    search_info_insert(email, book_name);
                    Intent intent = new Intent(getContext(), search_result.class);
                    intent.putExtra("book_name", book_name);
                    startActivity(intent);
                }
            }
        });

        itemClickListener = new search_adapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                search_record_edittext.clearFocus();
                String book_name = search_list.get(position).getBook_name();

                SharedPreferences pref2 = getActivity().getSharedPreferences("최근검색어" + email, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("검색어", book_name);
                editor2.apply();

                Intent intent = new Intent(getContext(), search_result.class);
                intent.putExtra("book_name", book_name);
                startActivity(intent);
            }
        };


        //친구 검색하고 아이템 클리하기
        friends_itemClickListener = new friends_adapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String other_nick = friends_list.get(position).getNick();
                String other_email = friends_list.get(position).getEmail();
                String other_image = friends_list.get(position).getProfile_image();
                int following = friends_list.get(position).getFollowing();
                int follower = friends_list.get(position).getFollwer();

                System.out.println(other_nick + "other_nick - search 클래스");//값 확인됨
                System.out.println(following + "following - search 클래스");//값 확인됨
                System.out.println(follower + "follower - search 클래스");//값 확인됨

                Intent intent = new Intent(getContext(), setting_other.class);
                intent.putExtra("other_nick", other_nick);
                intent.putExtra("other_email", other_email);
                intent.putExtra("other_image", other_image);
                intent.putExtra("following", following);
                intent.putExtra("follower", follower);

                startActivity(intent);
            }
        };

        search_all_remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_info_all_delete(email);
                search_record_recyclerview.setVisibility(View.GONE);
            }
        });

        return view;
    }

    //검색필터
    public void searchFilter(String searchText) {
        filteredList.clear();
        for (int i = 0; i < friends_list.size(); i++) {
            if (friends_list.get(i).getNick().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(friends_list.get(i));
            }
        }
        friends_adapter.filterList(filteredList);
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

    //최근 검색어 select
    private void search_info_select(String email) {
        ApiInterface search_info_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<search_data>> call = search_info_select.search_info_select(email);
        call.enqueue(new Callback<List<search_data>>() {
            @Override
            public void onResponse(Call<List<search_data>> call, Response<List<search_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onGetResult(response.body());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<search_data>> call, Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult(List<search_data> lists) {
        if (lists.size() == 0) {
//            search_last_textview.setText("최근 검색어가 없습니다");
            search_all_remove_button.setVisibility(View.GONE);

        } else {
            search_all_remove_button.setVisibility(View.VISIBLE);
            search_record_recyclerview.setVisibility(View.VISIBLE);


//            search_last_textview.setText("최근 검색어");
            adapter = new search_adapter(getActivity(), lists, itemClickListener, email);
            adapter.notifyDataSetChanged();
            search_record_recyclerview.setAdapter(adapter);
            search_list = lists;
        }
    }


    //친구 검색
//    private void search_friends_info_select(String email) {
//        ApiInterface search_info_select = ApiClient.getClient().create(ApiInterface.class);
//        Call<List<login_data>> call = search_info_select.search_friends_info_select(email);
//        call.enqueue(new Callback<List<login_data>>() {
//            @Override
//            public void onResponse(Call<List<login_data>> call, Response<List<login_data>> response) {
//                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
//                if (response.isSuccessful() && response.body() != null) {
//                    onGetResult_friends(response.body());
//                }
//            }
//
//            @SuppressLint("LongLogTag")
//            @Override
//            public void onFailure(Call<List<login_data>> call, Throwable t) {
//                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
//            }
//        });
//    }
//
//    private void onGetResult_friends(List<login_data> lists) {
//        if (lists.size() == 0) {
//            search_all_remove_button.setVisibility(View.INVISIBLE);
//            search_record_recyclerview.setVisibility(View.INVISIBLE);
//        } else {
//            search_all_remove_button.setVisibility(View.INVISIBLE);
//            search_record_recyclerview.setVisibility(View.INVISIBLE);
//
//            friends_adapter = new friends_adapter(getActivity(), lists, friends_itemClickListener);
//            friends_adapter.notifyDataSetChanged();
//            search_record_friends_recyclerview.setAdapter(friends_adapter);
//            friends_list = lists;
//        }
//    }

    private void search_friends_info_select2(String email,int page , int limit) {
        ApiInterface search_info_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<login_data>> call = search_info_select.search_friends_info_select2(email,page,limit);
        call.enqueue(new Callback<List<login_data>>() {
            @Override
            public void onResponse(Call<List<login_data>> call, Response<List<login_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onGetResult_friends2(response.body());
                    search_progress_bar.setVisibility(View.INVISIBLE);

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<login_data>> call, Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult_friends2(List<login_data> lists) {
        if (lists.size() == 0) {
            search_all_remove_button.setVisibility(View.INVISIBLE);
            search_record_recyclerview.setVisibility(View.GONE);
        } else {
            search_all_remove_button.setVisibility(View.INVISIBLE);
            search_record_recyclerview.setVisibility(View.GONE);

            friends_adapter = new friends_adapter(getActivity(), lists, friends_itemClickListener);
            friends_adapter.notifyDataSetChanged();
//            search_record_friends_x_recyclerview.setAdapter(friends_adapter);
            friends_list = lists;
        }

    }



    private void search_info_all_delete(String email) {
        ApiInterface search_delete = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_delete.search_info_all_delete(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        search_info_select(email);

        SharedPreferences pref = getActivity().getSharedPreferences("최근검색어" + email, MODE_PRIVATE);
        search_record_edittext.setText(pref.getString("검색어", ""));

        if (score.getValue() == 2) {
            search_friends_info_select2(email, page, limit);
            nestedScrollView = view.findViewById(R.id.scroll_view);
            search_progress_bar.setVisibility(View.INVISIBLE);
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        limit += 10;
                        search_progress_bar.setVisibility(View.VISIBLE);
                        search_friends_info_select2(email, page, limit);
                    }
                }
            });
        } else if (score.getValue() == 1) {
//            search_friends_info_select(email);
        }
    }
}