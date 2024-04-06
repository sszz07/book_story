package org.techtown.reiview_app.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class book_diary_select extends AppCompatActivity {
    TextView book_diary_select_start_page_textview, book_diary_select_end_page_textview, book_diary_select_memory_text_textview, book_diary_select_think_text_textview, book_diary_select_time_textview;
    TextView book_diary_select_memory_textview, book_diary_select_think_textview;
    int idx;
    Dialog dialog_update_delete;
    ImageButton book_diary_select_update_delete_imagebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_diary_select);

        book_diary_select_start_page_textview = (TextView) findViewById(R.id.book_diary_select_start_page_textview);
        book_diary_select_end_page_textview = (TextView) findViewById(R.id.book_diary_select_end_page_textview);
        book_diary_select_memory_text_textview = (TextView) findViewById(R.id.book_diary_select_memory_text_textview);
        book_diary_select_think_text_textview = (TextView) findViewById(R.id.book_diary_select_think_text_textview);
        book_diary_select_time_textview = (TextView) findViewById(R.id.book_diary_select_time_textview);
        book_diary_select_memory_textview = (TextView) findViewById(R.id.book_diary_select_memory_textview);
        book_diary_select_think_textview = (TextView) findViewById(R.id.book_diary_select_think_textview);
        book_diary_select_update_delete_imagebutton = (ImageButton) findViewById(R.id.book_diary_select_update_delete_imagebutton);


        dialog_update_delete = new Dialog(book_diary_select.this);       // Dialog 초기화
        dialog_update_delete.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog_update_delete.setContentView(R.layout.update_delete_dialog);

        Intent intent = getIntent();
        idx = intent.getIntExtra("idx", 0);
//        book_diary_select_second(idx);

        book_diary_select_update_delete_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_update_delete.getWindow().setGravity(Gravity.CENTER);
                dialog_update_delete.show();

//                // 아니오 버튼
//                // *주의할 점: findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.
                Button update_button = dialog_update_delete.findViewById(R.id.board_my_select_update);
                update_button.findViewById(R.id.board_my_select_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(book_diary_select.this, book_diary_update.class);
                        intent.putExtra("idx", idx);
                        startActivity(intent);
                        dialog_update_delete.dismiss();
                    }
                });


                Button delete_button = dialog_update_delete.findViewById(R.id.board_my_select_delete);
                delete_button.findViewById(R.id.board_my_select_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        diary_info_delete(idx);
                        dialog_update_delete.dismiss();
                    }
                });
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
                    try{
                        book_diary_select_start_page_textview.setText(response.body().get(0).getStart_page()+"p ");
                        book_diary_select_end_page_textview.setText(response.body().get(0).getEnd_page()+"p");

                        if(response.body().get(0).getMemory_content().equals("")){
                            book_diary_select_memory_text_textview.setText("글이 없습니다");
                        }else{
                            book_diary_select_memory_text_textview.setText(response.body().get(0).getMemory_content());

                        }
                        if(response.body().get(0).getAfter_content().equals("")){
                            book_diary_select_think_text_textview.setText("글이 없습니다");
                        }else{
                            book_diary_select_think_text_textview.setText(response.body().get(0).getAfter_content());
                        }

                        book_diary_select_time_textview.setText(response.body().get(0).getTime());

                        //글씨 흰색으로 만들기
                        book_diary_select_memory_textview.setText(response.body().get(0).getMemory_content());
                        book_diary_select_think_textview.setText(response.body().get(0).getAfter_content());
                    }catch (IndexOutOfBoundsException e){

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

    private void diary_info_delete(int idx) {
        System.out.println(idx+"idx");
        ApiInterface reply_delete = ApiClient.getClient().create(ApiInterface.class);
        Call<home_data> call = reply_delete.book_diary_delete(idx);
        call.enqueue(new Callback<home_data>() {
            @Override
            public void onResponse( Call<home_data> call,  Response<home_data> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    onBackPressed();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure( Call<home_data> call,  Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
                onBackPressed();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        book_diary_select_second(idx);
    }
}