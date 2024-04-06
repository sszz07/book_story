package org.techtown.reiview_app.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class book_diary_adapter extends RecyclerView.Adapter<book_diary_adapter.image_board_ViewHolder> {
    private Context context;
    private List<home_data> lists;
    private final ItemClickListener itemClickListener;
    Dialog dialog;
    public book_diary_adapter(Context context, List<home_data> lists, ItemClickListener itemClickListener,Dialog dialog) {
        this.context = context;
        this.lists = lists;
        this.itemClickListener = itemClickListener;
        this.dialog = dialog;
    }


    @Override
    public image_board_ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.diary_item, parent, false);
        return new image_board_ViewHolder(view, itemClickListener);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})

    @Override
    public void onBindViewHolder( image_board_ViewHolder holder, int position) {
        home_data data = lists.get(position);
        holder.book_diary_item_number_textview.setText(String.valueOf(position+1)+". ");
        holder.book_diary_item_date_textview.setText(data.getTime());
        holder.book_diary_item_read_start_TextView.setText(""+data.getStart_page()+"p");
        holder.book_diary_item_read_end_TextView.setText(data.getEnd_page()+"p");


        holder.book_diary_item_option_imageview.setOnClickListener(new View.OnClickListener() {
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
                        Intent intent = new Intent(context, book_diary_update.class);
                        intent.putExtra("idx", data.getIdx());
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });


                Button delete_button = dialog.findViewById(R.id.board_my_select_delete);
                delete_button.findViewById(R.id.board_my_select_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        diary_info_delete(data.getIdx());
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


    public class image_board_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        public TextView book_diary_item_number_textview, book_diary_item_date_textview,book_diary_item_read_start_TextView,book_diary_item_read_end_TextView;
        public ImageView book_diary_item_option_imageview;
        ItemClickListener itemClickListener;


        public image_board_ViewHolder( View view, ItemClickListener itemClickListener) {
            super(view);
            linearLayout = itemView.findViewById(R.id.board_reply_layout);
            book_diary_item_number_textview = itemView.findViewById(R.id.book_diary_item_number_textview);
            book_diary_item_date_textview = itemView.findViewById(R.id.book_diary_item_date_textview);
            book_diary_item_read_start_TextView = itemView.findViewById(R.id.book_diary_item_read_start_TextView);
            book_diary_item_read_end_TextView = itemView.findViewById(R.id.book_diary_item_read_end_TextView);
            book_diary_item_option_imageview = itemView.findViewById(R.id.book_diary_item_option_imageview);

            this.itemClickListener = itemClickListener;
            linearLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
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
