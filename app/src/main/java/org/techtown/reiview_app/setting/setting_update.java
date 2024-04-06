package org.techtown.reiview_app.setting;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.login_data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_update extends AppCompatActivity {

    EditText setting_update_nick_edittext;
    TextView setting_update_nick_count_textview, setting_update_nick_all_count_textview;

    ImageView setting_update_profile_image_imageview, setting_update_nick_cancel_imageview, setting_update_profile_image_cancel_imageview;
    ImageButton setting_update_gallery_imagebutton;

    Button setting_update_x_button, setting_update_button;
    String shared_profile_image, shared_nick, shared_email, nick;
    final static int TAKE_PICTURE = 1;
    private static final int IMG_REQUEST = 888;
    private Bitmap bitmap = null;
    Dialog image_change_dialog;
    String image = "이미지선택안함";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_update);

        /*
        1.갤러리 열기-비트맵으로 설정해서 갤러리 열수 있게 하기
        2.카메라 찍기
        3.기본이미지 설정하기-쉐어드값 삭제하기
        4.다이얼로그로 3가지 메뉴 만들기
        5.메소드로 유저 이미지 업데이트하기기 **/


        setting_update_nick_edittext = (EditText) findViewById(R.id.setting_update_nick_edittext);
        setting_update_nick_count_textview = (TextView) findViewById(R.id.setting_update_nick_count_textview);
        setting_update_nick_all_count_textview = (TextView) findViewById(R.id.setting_update_nick_all_count_textview);
        setting_update_profile_image_imageview = (ImageView) findViewById(R.id.setting_update_profile_image_imageview);
        setting_update_profile_image_cancel_imageview = (ImageView) findViewById(R.id.setting_update_profile_image_cancel_imageview);
        setting_update_nick_cancel_imageview = (ImageView) findViewById(R.id.setting_update_nick_cancel_imageview);
        setting_update_gallery_imagebutton = (ImageButton) findViewById(R.id.setting_update_gallery_imagebutton);
        setting_update_x_button = (Button) findViewById(R.id.setting_update_x_button);
        setting_update_button = (Button) findViewById(R.id.setting_update_button);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            } else {

                ActivityCompat.requestPermissions(setting_update.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        image_change_dialog = new Dialog(setting_update.this);       // Dialog 초기화
        image_change_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        image_change_dialog.setContentView(R.layout.setting_image_dialog);


        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
        shared_email = pref.getString("이메일", "");
        shared_nick = pref.getString("닉네임", "");
        shared_profile_image = pref.getString("프로필이미지", "");
        setting_update_nick_edittext.setText(shared_nick);
        String shared_nick_length = String.valueOf(shared_nick.length());
        setting_update_nick_count_textview.setText(shared_nick_length);

        //이미지가 존재했을때

        if (!shared_profile_image.equals("이미지x")) {
            setting_update_profile_image_imageview.setVisibility(View.VISIBLE);
            setting_update_profile_image_cancel_imageview.setVisibility(View.INVISIBLE);
            Glide.with(this)
                    .load("http://52.79.180.89/kakao_story" + shared_profile_image)
                    .apply(new RequestOptions().circleCrop())
                    .into(setting_update_profile_image_imageview);
        }
        //이미지가 존재하지 않았을때
        else {
            setting_update_profile_image_imageview.setVisibility(View.INVISIBLE);
            setting_update_profile_image_cancel_imageview.setVisibility(View.VISIBLE);
        }


        setting_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String image_time = simpleDateFormat.format(date);

                nick = setting_update_nick_edittext.getText().toString();

                //기본 이미지를 설정했을때
                if (image.equals("이미지x")) {
                    //닉네임만 수정
                    if (nick != null && !image.equals("이미지x")) {
                        System.out.println("11111");
                        book_user_image_x_update(shared_email, null, null);
                        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("닉네임", nick);
                        editor.apply();
                    }
                    //이미지만 수정하기
                    else if (nick == null && image.equals("이미지x")) {
                        System.out.println("22222");

                        book_user_image_x_update(shared_email, null, "이미지x");
                        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("프로필이미지", "이미지x");
                        editor.apply();
                    }
                    //닉네임 이미지 수정하기
                    else {
                        System.out.println("3333");

                        book_user_image_x_update(shared_email, nick, "이미지x");
                        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("닉네임", nick);
                        editor.putString("프로필이미지", "이미지x");
                        editor.apply();
                    }
                }

                //갤러리 이미지 및 카메라 이미지 설정했을때
                else if (!image.equals("이미지x") && !image.equals("이미지선택안함")) {
                    if (nick != null && image == null) {
                        System.out.println("4444");

                        book_user_gallery_update(shared_email, nick, null, null);
                        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("닉네임", nick);
                        editor.apply();
                    }
                    //이미지만 수정하기
                    else if (nick == null && image != null) {
                        System.out.println("55555");

                        book_user_gallery_update(shared_email, null, image, image_time);
                        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("프로필이미지", "/kakao_user_profile_image/" + image_time + ".jpg");
                        editor.apply();
                    }
                    //닉네임 이미지 수정하기
                    else {
                        System.out.println("66666");
                        book_user_gallery_update(shared_email, nick, image, image_time);
                        SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("닉네임", nick);
                        editor.putString("프로필이미지", "/kakao_user_profile_image/" + image_time + ".jpg");
                        editor.apply();
                    }
                }

                //닉네임만 수정했을때
                else if (image.equals("이미지선택안함")) {
                    book_user_gallery_update(shared_email, nick, shared_profile_image, image_time);
                    SharedPreferences pref = getSharedPreferences("현재유저", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("닉네임", nick);
                    editor.apply();
                }
            }
        });

        setting_update_nick_cancel_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting_update_nick_edittext.setText("");
            }
        });


        setting_update_gallery_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_change_dialog.getWindow().setGravity(Gravity.CENTER);
                image_change_dialog.show();

                //카메라
                Button camera_button = image_change_dialog.findViewById(R.id.setting_image_camera_dialog);
                camera_button.findViewById(R.id.setting_image_camera_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, TAKE_PICTURE);
                        image_change_dialog.dismiss();
                    }
                });

                //갤러리
                Button gallery_button = image_change_dialog.findViewById(R.id.setting_image_gallery_dialog);
                gallery_button.findViewById(R.id.setting_image_gallery_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectImage();
                        image_change_dialog.dismiss();
                    }
                });


                //기본 이미지
                Button image_x_button = image_change_dialog.findViewById(R.id.setting_image_x_dialog);
                image_x_button.findViewById(R.id.setting_image_x_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setting_update_profile_image_imageview.setVisibility(View.INVISIBLE);
                        setting_update_profile_image_cancel_imageview.setVisibility(View.VISIBLE);
                        image = "이미지x";
                        image_change_dialog.dismiss();
                    }
                });


                //취소 버튼
                Button image_cancel_button = image_change_dialog.findViewById(R.id.setting_image_cancel_dialog);
                image_cancel_button.findViewById(R.id.setting_image_cancel_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        image_change_dialog.dismiss();
                    }
                });
            }
        });

        setting_update_nick_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                nick = setting_update_nick_edittext.getText().toString();

                if (editable.length() == 0) {
                    setting_update_x_button.setVisibility(View.VISIBLE);
                    setting_update_button.setVisibility(View.INVISIBLE);

                    setting_update_nick_count_textview.setTextColor(Color.BLACK);
                    setting_update_nick_cancel_imageview.setVisibility(View.INVISIBLE);
                } else if (editable.length() >= 11) {
                    setting_update_nick_cancel_imageview.setVisibility(View.VISIBLE);
                    setting_update_x_button.setVisibility(View.VISIBLE);
                    setting_update_button.setVisibility(View.INVISIBLE);
                    setting_update_nick_count_textview.setTextColor(Color.parseColor("#FF4646"));
                } else if (editable.length() < 11) {
                    setting_update_nick_cancel_imageview.setVisibility(View.VISIBLE);
                    setting_update_x_button.setVisibility(View.INVISIBLE);
                    setting_update_button.setVisibility(View.VISIBLE);
                    setting_update_nick_count_textview.setTextColor(Color.BLACK);
                } else {
                    setting_update_nick_cancel_imageview.setVisibility(View.VISIBLE);
                }

                String editable_string = String.valueOf(editable.length());
                setting_update_nick_count_textview.setText(editable_string);

                setting_update_nick_cancel_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setting_update_nick_edittext.setText("");
                    }
                });
            }
        });

    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                setting_update_profile_image_imageview.setVisibility(View.VISIBLE);
                setting_update_profile_image_cancel_imageview.setVisibility(View.INVISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                Glide.with(setting_update.this)
                        .load(bitmap)
                        .apply(new RequestOptions().circleCrop())
                        .into(setting_update_profile_image_imageview);
            } catch (IOException e) {
                e.printStackTrace();
            }
            image = imageToString();
            image_null();
        } else if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK && data.hasExtra("data")) {
                setting_update_profile_image_imageview.setVisibility(View.VISIBLE);
                setting_update_profile_image_cancel_imageview.setVisibility(View.INVISIBLE);
                bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    Glide.with(setting_update.this)
                            .load(bitmap)
                            .apply(new RequestOptions().circleCrop())
                            .into(setting_update_profile_image_imageview);
                }
                image = imageToString();
                image_null();
            }
        }
    }


    private String imageToString() {
        for (int i = 0; true; i++) {
            if (bitmap == null) {
                if (image != null) {
                    return image;
                } else {
                    return null;
                }
            } else {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imgByte = byteArrayOutputStream.toByteArray();
                return Base64.encodeToString(imgByte, Base64.DEFAULT);
            }
        }
    }

    private Bitmap image_null() {
        return bitmap = null;
    }


    //티스토리 유저 업데이트
    @SuppressLint("LongLogTag")
    private void book_user_gallery_update(String email, String nick, String image, String image_time) {
        ApiInterface member_join = ApiClient.getClient().create(ApiInterface.class);
        Call<login_data> call = member_join.book_user_gallery_update(email, nick, image, image_time);
        call.enqueue(new Callback<login_data>() {
            @Override
            public void onResponse(Call<login_data> call, Response<login_data> response) {
                onBackPressed();
            }

            @Override
            public void onFailure(Call<login_data> call, Throwable t) {
                Log.e("t_story_make_user_insert()", "에러 : " + t.getMessage());
                onBackPressed();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void book_user_image_x_update(String email, String nick, String image) {
        ApiInterface member_join = ApiClient.getClient().create(ApiInterface.class);
        Call<login_data> call = member_join.book_user_image_x_update(email, nick, image);
        call.enqueue(new Callback<login_data>() {
            @Override
            public void onResponse(Call<login_data> call, Response<login_data> response) {
                onBackPressed();
            }

            @Override
            public void onFailure(Call<login_data> call, Throwable t) {
                Log.e("t_story_make_user_insert()", "에러 : " + t.getMessage());
                onBackPressed();
            }
        });
    }
}