package org.techtown.reiview_app.setting;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;


import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import org.jetbrains.annotations.NotNull;
import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.MainActivity;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.home.home_adapter;
import org.techtown.reiview_app.home.home_data;
import org.techtown.reiview_app.home.home_select;
import org.techtown.reiview_app.home.memo_update;
import org.techtown.reiview_app.login_data;
import org.techtown.reiview_app.record.record;
import org.techtown.reiview_app.search.search_follwer;
import org.techtown.reiview_app.search.search_follwing;
import org.techtown.reiview_app.search.search_select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class setting extends Fragment {
    View view;
    ImageView setting_profile_image_imageview, setting_update_imageview, setting_profile_image_x_imageview;
    TextView setting_nick_textview;
    String profile_image, nick, email;


    TextView setting_want_book_textview, setting_want_book_line_textview, setting_read_book_textview, setting_read_book_line_textview, setting_finish_book_textview, setting_finish_book_line_textview;
    TextView setting_want_book_not_textview, setting_read_book_not_textview, setting_finish_book_not_textview, setting_following_textview, setting_follower_textview;
    RecyclerView setting_want_book_recyclerview, setting_read_book_recyclerview, setting_finish_book_recyclerview;

    Button setting_read_book_menu_button, setting_finish_book_menu_button, setting_my_memo_button, setting_my_book_time_button;
    Dialog read_book_dialog, finish_book_dialog;
    String read_book_status;
    List<home_data> list_read = new ArrayList<>();
    setting_read_book_adapter adapter;
    setting_read_book_adapter.ItemClickListener itemClickListener;

    List<home_data> list_want = new ArrayList<>();
    setting_want_book_adapter adapter2;
    setting_want_book_adapter.ItemClickListener itemClickListener2;


    List<home_data> list_finish = new ArrayList<>();
    setting_finish_book_adapter adapter3;
    setting_finish_book_adapter.ItemClickListener itemClickListener3;

    NestedScrollView nestedScrollView;
    int page = 0, limit = 10;
    public Calendar cal = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_setting, container, false);
        setHasOptionsMenu(true);
        int month_home = cal.get(Calendar.MONTH) + 1;
        record.month = month_home;
        SharedPreferences pref = getActivity().getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");

        read_book_dialog = new Dialog(getActivity());       // Dialog 초기화
        read_book_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        read_book_dialog.setContentView(R.layout.read_book_dialog);


        finish_book_dialog = new Dialog(getActivity());       // Dialog 초기화
        finish_book_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        finish_book_dialog.setContentView(R.layout.finish_book_dialog);


        setting_following_textview = (TextView) view.findViewById(R.id.setting_following_textview);
        setting_follower_textview = (TextView) view.findViewById(R.id.setting_follower_textview);



        setting_update_imageview = (ImageView) view.findViewById(R.id.setting_update_imageview);
        setting_profile_image_imageview = (ImageView) view.findViewById(R.id.setting_profile_image_imageview);
        setting_profile_image_x_imageview = (ImageView) view.findViewById(R.id.setting_profile_image_x_imageview);
        setting_nick_textview = (TextView) view.findViewById(R.id.setting_nick_textview);

        setting_want_book_textview = (TextView) view.findViewById(R.id.setting_want_book_textview);
        setting_want_book_line_textview = (TextView) view.findViewById(R.id.setting_want_book_line_textview);
        setting_read_book_textview = (TextView) view.findViewById(R.id.setting_read_book_textview);
        setting_read_book_line_textview = (TextView) view.findViewById(R.id.setting_read_book_line_textview);
        setting_finish_book_textview = (TextView) view.findViewById(R.id.setting_finish_book_textview);
        setting_finish_book_line_textview = (TextView) view.findViewById(R.id.setting_finish_book_line_textview);

        setting_want_book_not_textview = (TextView) view.findViewById(R.id.setting_want_book_not_textview);
        setting_read_book_not_textview = (TextView) view.findViewById(R.id.setting_read_book_not_textview);
        setting_finish_book_not_textview = (TextView) view.findViewById(R.id.setting_finish_book_not_textview);
        setting_want_book_recyclerview = (RecyclerView) view.findViewById(R.id.setting_want_book_recyclerview);
        setting_read_book_recyclerview = (RecyclerView) view.findViewById(R.id.setting_read_book_recyclerview);
        setting_finish_book_recyclerview = (RecyclerView) view.findViewById(R.id.setting_finish_book_recyclerview);

        setting_read_book_menu_button = (Button) view.findViewById(R.id.setting_read_book_menu_button);
        setting_finish_book_menu_button = (Button) view.findViewById(R.id.setting_finish_book_menu_button);
        setting_my_memo_button = (Button) view.findViewById(R.id.setting_my_memo_button);

        setting_my_book_time_button = (Button) view.findViewById(R.id.setting_my_book_time_button);

        setting_finish_book_menu_button.setVisibility(View.INVISIBLE);
        setting_read_book_menu_button.setVisibility(View.INVISIBLE);

        setting_want_book_textview.setTextColor(Color.BLACK);
        setting_want_book_line_textview.setTextColor(Color.BLACK);
        setting_read_book_textview.setTextColor(Color.parseColor("#828282"));
        setting_want_book_line_textview.setTextColor(Color.parseColor("#828282"));
        setting_finish_book_textview.setTextColor(Color.parseColor("#828282"));
        setting_finish_book_line_textview.setTextColor(Color.parseColor("#828282"));

        setting_read_book_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        setting_want_book_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        setting_finish_book_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        //처음 화면을 켰을때 보고싶은 책 보여주기
        read_book_status = "최근독서순";
        book_want_recyclerview_select(email);
        SharedPreferences pref3 = getActivity().getSharedPreferences("번호", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref3.edit();
        editor.putString("번호", "1");
        editor.apply();


        SharedPreferences pref2 = getActivity().getSharedPreferences("번호", MODE_PRIVATE);
        String number = pref2.getString("번호", "");
        String 상태 = pref2.getString("상태", "");


        setting_following_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), search_follwing.class);
                startActivity(intent);
            }
        });

        setting_follower_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), search_follwer.class);
                startActivity(intent);
            }
        });

        setting_my_memo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), setting_my_memo.class);
                startActivity(intent);
            }
        });


        setting_my_book_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), setting_read_time.class);
                startActivity(intent);
            }
        });


        //책 선택하기
        itemClickListener = new setting_read_book_adapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int idx = list_read.get(position).getIdx();
                String book_subject = list_read.get(position).getBook_subject();
                String make = list_read.get(position).getMake();
                String writer = list_read.get(position).getWriter();
                String image = list_read.get(position).getImage();
                int read_time = list_read.get(position).getRead_time();
                int read_page = list_read.get(position).getRead_page();
                String book_page = list_read.get(position).getBook_page();

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
                System.out.println(read_book_status + "read_book_status");
                intent.putExtra("상태", read_book_status);

                startActivity(intent);
            }
        };


        //책 선택하기
        itemClickListener2 = new setting_want_book_adapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int idx = list_want.get(position).getIdx();
                String image = list_want.get(position).getImage();
                String book_subject = list_want.get(position).getBook_subject();
                String make = list_want.get(position).getMake();
                String writer = list_want.get(position).getWriter();
                String want_date = list_want.get(position).getWant_date();
                String book_content = list_want.get(position).getBook_content();
                String book_page = list_want.get(position).getBook_page();

                Log.e("book_content 데이터 확인", book_content);

                Intent intent = new Intent(getContext(), search_select.class);
                intent.putExtra("image", image);
                intent.putExtra("book_subject", book_subject);
                intent.putExtra("make", make);
                intent.putExtra("author", writer);
                intent.putExtra("date", want_date);
                intent.putExtra("book_content", book_content);
                intent.putExtra("book_page", book_page);
                startActivity(intent);
            }
        };


        //책 선택하기
        itemClickListener3 = new setting_finish_book_adapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String image = list_finish.get(position).getImage();
                String book_subject = list_finish.get(position).getBook_subject();
                String make = list_finish.get(position).getMake();
                String writer = list_finish.get(position).getWriter();
                String my_content = list_finish.get(position).getMy_content();
                int read_time = list_finish.get(position).getRead_time();
                float star = list_finish.get(position).getBook_star();
                String finish = list_finish.get(position).getFinish();


                Intent intent = new Intent(getContext(), setting_finish_select.class);
                intent.putExtra("image", image);
                intent.putExtra("book_subject", book_subject);
                intent.putExtra("make", make);
                intent.putExtra("author", writer);
                intent.putExtra("my_content", my_content);
                intent.putExtra("read_time", read_time);
                intent.putExtra("star", star);
                intent.putExtra("finish", finish);
                startActivity(intent);
            }
        };


        //읽고싶은 클릭했을때
        setting_want_book_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_want_recyclerview_select(email);
                SharedPreferences pref = getActivity().getSharedPreferences("번호", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("번호", "1");
                editor.apply();


                setting_read_book_not_textview.setVisibility(View.INVISIBLE);
                setting_finish_book_not_textview.setVisibility(View.INVISIBLE);

                setting_finish_book_menu_button.setVisibility(View.INVISIBLE);
                setting_read_book_menu_button.setVisibility(View.INVISIBLE);

                setting_finish_book_menu_button.setText("최신순▽");
                Button button = (Button) finish_book_dialog.findViewById(R.id.finish_book_last_button);
                button.setTextColor(Color.parseColor("#828282"));
                Button button2 = (Button) finish_book_dialog.findViewById(R.id.finish_book_star_button);
                button2.setTextColor(Color.BLACK);

                Button button3 = (Button) read_book_dialog.findViewById(R.id.read_book_last_read_button);
                button3.setTextColor(Color.parseColor("#828282"));
                Button button4 = (Button) read_book_dialog.findViewById(R.id.read_book_more_read_button);
                button4.setTextColor(Color.BLACK);

                setting_read_book_menu_button.setText("최근 독서순▽");
                setting_want_book_textview.setTextColor(Color.BLACK);
                setting_want_book_line_textview.setTextColor(Color.BLACK);
                setting_read_book_textview.setTextColor(Color.parseColor("#828282"));
                setting_read_book_line_textview.setTextColor(Color.parseColor("#828282"));
                setting_finish_book_textview.setTextColor(Color.parseColor("#828282"));
                setting_finish_book_line_textview.setTextColor(Color.parseColor("#828282"));
            }
        });

        //읽는중 클릭했을때
        setting_read_book_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getActivity().getSharedPreferences("번호", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("번호", "2");
                editor.putString("상태", "최근독서순");
                editor.apply();


                read_book_status = "최근독서순";
                setting_want_book_recyclerview.setVisibility(View.INVISIBLE);
                setting_read_book_recyclerview.setVisibility(View.VISIBLE);
                setting_finish_book_recyclerview.setVisibility(View.INVISIBLE);


                setting_finish_book_menu_button.setVisibility(View.INVISIBLE);
                setting_read_book_menu_button.setVisibility(View.VISIBLE);


                setting_want_book_not_textview.setVisibility(View.INVISIBLE);
                setting_finish_book_not_textview.setVisibility(View.INVISIBLE);

                setting_finish_book_menu_button.setText("최신순▽");
                Button button = (Button) finish_book_dialog.findViewById(R.id.finish_book_last_button);
                button.setTextColor(Color.parseColor("#828282"));
                Button button2 = (Button) finish_book_dialog.findViewById(R.id.finish_book_star_button);
                button2.setTextColor(Color.BLACK);

                setting_want_book_textview.setTextColor(Color.parseColor("#828282"));
                setting_want_book_line_textview.setTextColor(Color.parseColor("#828282"));
                setting_read_book_textview.setTextColor(Color.BLACK);
                setting_read_book_line_textview.setTextColor(Color.BLACK);
                setting_finish_book_textview.setTextColor(Color.parseColor("#828282"));
                setting_finish_book_line_textview.setTextColor(Color.parseColor("#828282"));

                book_home_recyclerview_select(email, read_book_status);
            }
        });
//
//        //다 읽은 클릭했을때
        setting_finish_book_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting_finish_book_menu_button.setVisibility(View.VISIBLE);
                setting_read_book_menu_button.setVisibility(View.INVISIBLE);

                SharedPreferences pref = getActivity().getSharedPreferences("번호", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("번호", "3");
                editor.putString("상태", "최신순");
                editor.apply();

                book_finish_recyclerview_select(email, "최신순");

                setting_want_book_recyclerview.setVisibility(View.INVISIBLE);
                setting_read_book_recyclerview.setVisibility(View.INVISIBLE);
                setting_finish_book_recyclerview.setVisibility(View.VISIBLE);

                setting_read_book_menu_button.setText("최근 독서순▽");
                Button button3 = (Button) read_book_dialog.findViewById(R.id.read_book_last_read_button);
                button3.setTextColor(Color.parseColor("#828282"));
                Button button4 = (Button) read_book_dialog.findViewById(R.id.read_book_more_read_button);
                button4.setTextColor(Color.BLACK);


                setting_want_book_textview.setTextColor(Color.parseColor("#828282"));
                setting_want_book_line_textview.setTextColor(Color.parseColor("#828282"));
                setting_read_book_textview.setTextColor(Color.parseColor("#828282"));
                setting_read_book_line_textview.setTextColor(Color.parseColor("#828282"));
                setting_finish_book_textview.setTextColor(Color.BLACK);
                setting_finish_book_line_textview.setTextColor(Color.BLACK);

            }
        });


        //읽는중
        //버튼 최근 독서순
        //버튼 많이 읽는순
        setting_read_book_menu_button.setText("최근 독서순▽");
        setting_read_book_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                read_book_dialog.getWindow().setGravity(Gravity.CENTER);
                read_book_dialog.show();

                Button button = (Button) read_book_dialog.findViewById(R.id.read_book_last_read_button);
                Button button2 = (Button) read_book_dialog.findViewById(R.id.read_book_more_read_button);
                read_book_dialog.findViewById(R.id.read_book_last_read_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences pref = getActivity().getSharedPreferences("번호", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("번호", "2");
                        editor.putString("상태", "최근독서순");
                        editor.apply();


                        book_home_recyclerview_select(email, "최근독서순");
                        button.setTextColor(Color.parseColor("#828282"));
                        button2.setTextColor(Color.BLACK);
                        setting_read_book_menu_button.setText("최근 독서순▽");
                        read_book_dialog.dismiss();
                    }
                });


                read_book_dialog.findViewById(R.id.read_book_more_read_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences pref = getActivity().getSharedPreferences("번호", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("번호", "2");
                        editor.putString("상태", "책페이지");
                        editor.apply();


                        book_home_recyclerview_select(email, "책페이지");
                        button.setTextColor(Color.BLACK);
                        button2.setTextColor(Color.parseColor("#828282"));

                        setting_read_book_menu_button.setText("많이 읽는순▽");
                        read_book_dialog.dismiss();
                    }
                });
            }
        });


        //다 읽음
        //버튼 최근 독서순
        //버튼 많이 읽는순
        setting_finish_book_menu_button.setText("최신순▽");
        setting_finish_book_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish_book_dialog.getWindow().setGravity(Gravity.CENTER);
                finish_book_dialog.show();

                Button button = (Button) finish_book_dialog.findViewById(R.id.finish_book_last_button);
                Button button2 = (Button) finish_book_dialog.findViewById(R.id.finish_book_star_button);
                finish_book_dialog.findViewById(R.id.finish_book_last_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SharedPreferences pref = getActivity().getSharedPreferences("번호", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("번호", "3");
                        editor.putString("상태", "최신순");
                        editor.apply();

                        book_finish_recyclerview_select(email, "최신순");
                        setting_finish_book_recyclerview.setVisibility(View.VISIBLE);
                        button.setTextColor(Color.parseColor("#828282"));
                        button2.setTextColor(Color.BLACK);
                        setting_finish_book_menu_button.setText("최신순▽");
                        finish_book_dialog.dismiss();
                    }
                });


                finish_book_dialog.findViewById(R.id.finish_book_star_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences pref = getActivity().getSharedPreferences("번호", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("번호", "3");
                        editor.putString("상태", "별");
                        editor.apply();

                        book_finish_recyclerview_select(email, "별");
                        setting_finish_book_recyclerview.setVisibility(View.VISIBLE);
                        button.setTextColor(Color.BLACK);
                        button2.setTextColor(Color.parseColor("#828282"));

                        setting_finish_book_menu_button.setText("별좀 높은순▽");
                        finish_book_dialog.dismiss();
                    }
                });
            }
        });


        setting_update_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), setting_select.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getActivity().getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");
        nick = pref.getString("닉네임", "");
        profile_image = pref.getString("프로필이미지", "");
        follow_follwing_info_select(email);



        SharedPreferences pref2 = getActivity().getSharedPreferences("번호", MODE_PRIVATE);
        String number = pref2.getString("번호", "");
        String 상태 = pref2.getString("상태", "");
        System.out.println(상태 + "상태상태");

        if (number.equals("1")) {
            book_want_recyclerview_select(email);
        } else if (number.equals("2") && 상태.equals("최근독서순")) {
            book_home_recyclerview_select(email, "최근독서순");

        } else if (number.equals("2") && 상태.equals("책페이지")) {
            book_home_recyclerview_select(email, "책페이지");

        } else if (number.equals("3") && 상태.equals("최신순")) {
            book_finish_recyclerview_select(email, "최신순");

        } else if (number.equals("3") && 상태.equals("별")) {
            book_finish_recyclerview_select(email, "별");
        }


        setting_nick_textview.setText(nick);


        if (!profile_image.equals("이미지x")) {
            setting_profile_image_x_imageview.setVisibility(View.INVISIBLE);
            setting_profile_image_imageview.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load("http://52.79.180.89/kakao_story" + profile_image)
                    .apply(new RequestOptions().circleCrop())
                    .into(setting_profile_image_imageview);
        } else {
            setting_profile_image_x_imageview.setVisibility(View.VISIBLE);
            setting_profile_image_imageview.setVisibility(View.INVISIBLE);
        }

    }

    private void follow_follwing_info_select(String email) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<login_data>> call = home_select.follow_follwing_info_select(email);
        call.enqueue(new Callback<List<login_data>>() {
            @Override
            public void onResponse(Call<List<login_data>> call, Response<List<login_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    int follwing = response.body().get(0).getFollowing();
                    int follwer = response.body().get(0).getFollwer();

                    setting_follower_textview.setText("팔로워 " + String.valueOf(follwer));
                    setting_following_textview.setText("팔로잉 " + String.valueOf(follwing));

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<login_data>> call, Throwable t) {
                Log.e("t_story_home_recyclerview_select()", "에러 : " + t.getMessage());
            }
        });
    }


    //    // INSERT 구문 은 WHERE 절을 지원하지 않으므로 쿼리가 실패합니다. id열이 고유하거나 기본 키 라고 가정합니다 .
    private void book_home_recyclerview_select(String email, String choise) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_home_recyclerview_select(email, choise);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
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
        if (lists.size() == 0) {
            //읽고싶은책 , 읽는중 리사이클러뷰
            setting_want_book_recyclerview.setVisibility(View.GONE);
            setting_read_book_recyclerview.setVisibility(View.GONE);
            setting_finish_book_recyclerview.setVisibility(View.GONE);

            //
            setting_finish_book_not_textview.setVisibility(View.GONE);
            setting_want_book_not_textview.setVisibility(View.GONE);
            setting_read_book_not_textview.setVisibility(View.VISIBLE);


        } else {
            setting_read_book_not_textview.setVisibility(View.INVISIBLE);

            list_read = lists;
            adapter = new setting_read_book_adapter(getActivity(), lists, itemClickListener);
            adapter.notifyDataSetChanged();
            setting_read_book_recyclerview.setAdapter(adapter);

        }
    }


    // INSERT 구문 은 WHERE 절을 지원하지 않으므로 쿼리가 실패합니다. id열이 고유하거나 기본 키 라고 가정합니다 .
    private void book_want_recyclerview_select(String email) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_want_recyclerview_select(email);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onGetResult2(response.body());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<home_data>> call, Throwable t) {
                Log.e("t_story_home_recyclerview_select()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult2(List<home_data> lists) {
        if (lists.size() == 0) {
            setting_want_book_recyclerview.setVisibility(View.GONE);
            setting_read_book_recyclerview.setVisibility(View.GONE);
            setting_finish_book_recyclerview.setVisibility(View.GONE);

            setting_want_book_not_textview.setVisibility(View.VISIBLE);
            setting_finish_book_not_textview.setVisibility(View.GONE);
            setting_read_book_not_textview.setVisibility(View.GONE);
            setting_want_book_textview.setText("읽고싶은");

        } else {
            setting_want_book_recyclerview.setVisibility(View.VISIBLE);
            setting_read_book_recyclerview.setVisibility(View.GONE);
            setting_finish_book_recyclerview.setVisibility(View.GONE);

            setting_finish_book_not_textview.setVisibility(View.INVISIBLE);
            setting_read_book_not_textview.setVisibility(View.INVISIBLE);
            setting_want_book_not_textview.setVisibility(View.INVISIBLE);

            list_want = lists;
            adapter2 = new setting_want_book_adapter(getActivity(), lists, itemClickListener2);
            adapter2.notifyDataSetChanged();
            setting_want_book_recyclerview.setAdapter(adapter2);
        }
    }


    //    // INSERT 구문 은 WHERE 절을 지원하지 않으므로 쿼리가 실패합니다. id열이 고유하거나 기본 키 라고 가정합니다 .
    private void book_finish_recyclerview_select(String email, String choise) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_finish_recyclerview_select(email, choise);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {

                    onGetResult3(response.body());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<home_data>> call, Throwable t) {
                Log.e("t_story_home_recyclerview_select()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult3(List<home_data> lists) {
        if (lists.size() == 0) {
            setting_want_book_recyclerview.setVisibility(View.GONE);
            setting_read_book_recyclerview.setVisibility(View.GONE);
            setting_finish_book_recyclerview.setVisibility(View.GONE);

            setting_want_book_not_textview.setVisibility(View.VISIBLE);
            setting_read_book_not_textview.setVisibility(View.INVISIBLE);
            setting_finish_book_not_textview.setVisibility(View.INVISIBLE);
            setting_finish_book_textview.setText("다 읽은");

        } else {
            setting_want_book_recyclerview.setVisibility(View.GONE);
            setting_read_book_recyclerview.setVisibility(View.GONE);


            setting_read_book_not_textview.setVisibility(View.INVISIBLE);
            setting_finish_book_not_textview.setVisibility(View.INVISIBLE);
            setting_want_book_not_textview.setVisibility(View.INVISIBLE);
            setting_read_book_not_textview.setVisibility(View.INVISIBLE);
            setting_finish_book_not_textview.setVisibility(View.INVISIBLE);

            list_finish = lists;
            adapter3 = new setting_finish_book_adapter(getActivity(), lists, itemClickListener3);
            adapter3.notifyDataSetChanged();
            setting_finish_book_recyclerview.setAdapter(adapter3);
        }
    }
}