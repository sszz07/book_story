package org.techtown.reiview_app.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.home.memo_plus;
import org.techtown.reiview_app.login_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_other extends AppCompatActivity {

    ImageView setting_other_profile_image_x_imageview, setting_other_profile_image_imageview;
    TextView setting_other_nick_textview, setting_other_following_textview, setting_other_follower_textview;
    Button setting_other_follow_button, setting_other_follow_X_button;
    String other_nick, other_email, other_image, email;
    Dialog follow_cancel_dialog;
    int follower, following;
    String nick, image;
    MutableLiveData<Integer> score = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_other);

        setting_other_profile_image_imageview = (ImageView) findViewById(R.id.setting_other_profile_image_imageview);
        setting_other_profile_image_x_imageview = (ImageView) findViewById(R.id.setting_other_profile_image_x_imageview);
        setting_other_nick_textview = (TextView) findViewById(R.id.setting_other_nick_textview);
        setting_other_following_textview = (TextView) findViewById(R.id.setting_other_following_textview);
        setting_other_follower_textview = (TextView) findViewById(R.id.setting_other_follower_textview);
        setting_other_follow_button = (Button) findViewById(R.id.setting_other_follow_button);
        setting_other_follow_X_button = (Button) findViewById(R.id.setting_other_follow_X_button);


        follow_cancel_dialog = new Dialog(setting_other.this);       // Dialog 초기화
        follow_cancel_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        follow_cancel_dialog.setContentView(R.layout.following_cancel);

        //나의 이메일값 가져오기
        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");
        nick = pref.getString("닉네임", "");
        image = pref.getString("프로필이미지", "");

        Intent intent = getIntent();
        other_nick = intent.getStringExtra("other_nick");
        other_email = intent.getStringExtra("other_email");
        other_image = intent.getStringExtra("other_image");
        follower = intent.getIntExtra("follower", 0);
        following = intent.getIntExtra("following", 0);

        //실시간 값 넣기
        score.setValue(follower);

        System.out.println(other_nick + "other_nick 값확인" + other_email + "other_email 값 확인" + other_image + "other_image 값 확인   setting_other");//값 들어옴


        setting_other_following_textview.setText("팔로잉 " + String.valueOf(following));
        setting_other_follower_textview.setText("팔로워 " + String.valueOf(follower));
        System.out.println(following + "following - setting_other 클래스");//값 확인됨

        //버튼 상태 확인
        follower_info_select(email, other_email);

        //닉네임 뷰 설정
        setting_other_nick_textview.setText(other_nick);//이미지랑 따로 빼냄

//이미지 눌값 확인
        if (other_image == null) {
            setting_other_profile_image_imageview.setVisibility(View.INVISIBLE);
            setting_other_profile_image_x_imageview.setVisibility(View.VISIBLE);

        } else {
            setting_other_profile_image_imageview.setVisibility(View.VISIBLE);
            setting_other_profile_image_x_imageview.setVisibility(View.INVISIBLE);

            Glide.with(setting_other.this)
                    .load("http://52.79.180.89/kakao_story" + other_image)
                    .apply(new RequestOptions().circleCrop())
                    .into(setting_other_profile_image_imageview);
        }


        setting_other_following_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_other.this, search_follwing.class);
                System.out.println(other_email+"값확인");
                intent.putExtra("other_email", other_email);
                startActivity(intent);
            }
        });


        setting_other_follower_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_other.this, search_follwer.class);
                intent.putExtra("other_email", other_email);
                startActivity(intent);
            }
        });

        setting_other_follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting_other_follower_textview.setText("팔로워 " + String.valueOf(score.getValue() + 1));


                following_info_insert(email, other_nick, other_email, other_image,nick,image);

                setting_other_follow_X_button.setVisibility(View.VISIBLE);
                setting_other_follow_button.setVisibility(View.INVISIBLE);

                //팔로잉 재설정
                SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                int following = pref.getInt("팔로잉", 0);

                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("팔로잉", following + 1);
                editor.apply();

            }
        });


        //팔로우 안함
        setting_other_follow_X_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow_cancel_dialog.show(); // 다이얼로그 띄우기
                /* 이 함수 안에 원하는 디자인과 기능을 구현하면 된다. */


                Button follow_stop_button = follow_cancel_dialog.findViewById(R.id.follow_cancel_button);
                follow_stop_button.findViewById(R.id.follow_cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //팔로우 취소하기
                        following_info_delete(email, other_email);

                        setting_other_follow_button.setVisibility(View.VISIBLE);
                        setting_other_follow_X_button.setVisibility(View.INVISIBLE);


                        //팔로잉 재설정
                        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                        int following = pref.getInt("팔로잉", 0);

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("팔로잉", following - 1);
                        editor.apply();


                        if (score.getValue() == 0) {
                            setting_other_follower_textview.setText("팔로워 " + String.valueOf(score.getValue()));
                        } else {
                            setting_other_follower_textview.setText("팔로워 " + String.valueOf(score.getValue() - 1));

                        }
                        follow_cancel_dialog.dismiss();
                    }
                });


                Button follow_stop_cancel_button = follow_cancel_dialog.findViewById(R.id.follow_cancel_x_button);
                follow_stop_cancel_button.findViewById(R.id.follow_cancel_x_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        follow_cancel_dialog.dismiss();
                    }
                });
            }
        });

    }


    //팔로우 하기
    private void following_info_insert(String email, String other_nick, String other_email, String other_image,String nick,String image) {
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_info_insert.following_info_insert(email, other_nick, other_email, other_image,nick,image);
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


    //버튼 상태 확인 + 팔로워 확인
    private void follower_info_select(String email, String other_email) {
        ApiInterface search_info_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<login_data>> call = search_info_select.follower_info_select(email, other_email);
        call.enqueue(new Callback<List<login_data>>() {
            @Override
            public void onResponse(Call<List<login_data>> call, Response<List<login_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onGetResult(response.body());
                    try {
                        String 상태 = response.body().get(0).getOther_nick();

                        //팔로잉이 안되어 있을때
                        if (상태 == null) {
                            //회색 버튼
                            setting_other_follow_X_button.setVisibility(View.INVISIBLE);

                            //파란색 버튼
                            setting_other_follow_button.setVisibility(View.VISIBLE);
                        } else {
                            setting_other_follow_X_button.setVisibility(View.VISIBLE);
                            setting_other_follow_button.setVisibility(View.INVISIBLE);
                        }
                    } catch (IndexOutOfBoundsException e) {

                    }

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<login_data>> call, Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
            }
        });

    }

    private void onGetResult(List<login_data> lists) {
        if (lists.size() == 0) {
            //회색 버튼
            setting_other_follow_X_button.setVisibility(View.INVISIBLE);

            //파란색 버튼
            setting_other_follow_button.setVisibility(View.VISIBLE);
        } else {
            setting_other_follow_X_button.setVisibility(View.VISIBLE);
            setting_other_follow_button.setVisibility(View.INVISIBLE);
        }

    }


    //팔로우 하기
    private void following_info_delete(String email, String other_email) {
        ApiInterface search_info_insert = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = search_info_insert.following_info_delete(email, other_email);
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
}