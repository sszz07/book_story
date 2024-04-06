package org.techtown.reiview_app.home;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class memo_adapter extends RecyclerView.Adapter<memo_adapter.ItemViewHolder> {
    private Context context;
    private List<home_data> lists;
    Dialog dialog;
    String nick;

    public memo_adapter(Context context, List<home_data> lists, Dialog dialog) {
        this.context = context;
        this.lists = lists;
        this.dialog = dialog;
    }



    @Override
    public ItemViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.memo_item, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( ItemViewHolder holder, int position) {
        home_data data = lists.get(position);
        /*
         * 시간계산 되는 방법
         *
         * 왜 long으로 계산할까?
         * currentTimeMillis() api 자체가 long으로 반환을 한다
         *
         * System.currentTimeMillis():현재 시간의 값을 long으로 반환을 해준다
         *
         * 어떻게 계산이 되는가?
         * 조건문에 초는 60 분은 60 시간은 24 일 30 순서대로 계산하면서 내려가게 된다
         *
         * */

        class TIME_MAXIMUM {
            public static final int SEC = 60;
            public static final int MIN = 60;
            public static final int HOUR = 24;
            public static final int DAY = 30;
            public static final int MONTH = 12;
        }




        holder.memo_time_textview.setText(data.getTime());
        holder.memo_textview.setText(data.getMemo());



        holder.memo_dialog_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.show();

//                // 아니오 버튼
//                // *주의할 점: findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.
                Button update_button = dialog.findViewById(R.id.board_my_select_update);
                update_button.findViewById(R.id.board_my_select_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, memo_update.class);
                        intent.putExtra("idx", data.getIdx());
                        intent.putExtra("memo", data.getMemo());
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });


                Button delete_button = dialog.findViewById(R.id.board_my_select_delete);
                delete_button.findViewById(R.id.board_my_select_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        memo_info_delete(data.getIdx(),data.getBook_subject());
                        lists.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, lists.size());
                        dialog.dismiss();
                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView memo_textview, memo_time_textview;
        public ImageView memo_dialog_imageview;

        public ItemViewHolder( View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.board_reply_layout);
            memo_textview = itemView.findViewById(R.id.memo_textview);
            memo_time_textview = itemView.findViewById(R.id.memo_time_textview);
            memo_dialog_imageview = itemView.findViewById(R.id.memo_dialog_imageview);
        }
    }


    //댓글 삭제하기
    private void memo_info_delete(int idx,String book_subject) {
        ApiInterface reply_delete = ApiClient.getClient().create(ApiInterface.class);
        Call<home_data> call = reply_delete.memo_info_delete(idx,book_subject);
        call.enqueue(new Callback<home_data>() {
            @Override
            public void onResponse( Call<home_data> call,  Response<home_data> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure( Call<home_data> call,  Throwable t) {
                Log.e("t_story_board_number_select_subject()", "에러 : " + t.getMessage());
            }
        });
    }
}
