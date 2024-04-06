package org.techtown.reiview_app.record;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import org.techtown.reiview_app.R;
import org.techtown.reiview_app.home.home_data;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class record_book_count_adapter extends RecyclerView.Adapter<record_book_count_adapter.image_board_ViewHolder> {
    private Context context;
    private List<home_data> lists;
    private final record_book_count_adapter.ItemClickListener itemClickListener;

    public record_book_count_adapter(Context context, List<home_data> lists, record_book_count_adapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.lists = lists;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public record_book_count_adapter.image_board_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_book_count_item, parent, false);
        return new record_book_count_adapter.image_board_ViewHolder(view, itemClickListener);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})

    @Override
    public void onBindViewHolder(record_book_count_adapter.image_board_ViewHolder holder, int position) {
        home_data data = lists.get(position);

        //순서
        holder.record_book_count_item_number_textview.setText(String.valueOf(position+1));

        String subject = data.getSubject();
        if(subject.length() >30){
            holder.record_book_count_item_textview.setText(subject.substring(0, 30)+"....");
        }else {
            holder.record_book_count_item_textview.setText(data.getSubject());
        }

        //책 이미지
        Glide.with(context)
                .load(data.getImage())
                .into(holder.record_book_count_item_imageview);


        //읽은시간
        int hrs = data.getRead_time() / 3600;
        int mins = (data.getRead_time() % 3600) / 60;
        int sec = data.getRead_time() % 60;

//        if(hrs == 0){
//            String time = String.format("%2d분 %2d초", mins, sec);
//            holder.record_book_count_item_time_textview.setText(time);
//        }else if(mins == 0){
//            String time = String.format("%2d초", sec);
//            holder.record_book_count_item_time_textview.setText(time);
//        }else{
//            String time = String.format("%2d시 %2d분 %2d초",hrs, mins, sec);
//            holder.record_book_count_item_time_textview.setText(time);
//        }
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class image_board_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        public TextView record_book_count_item_number_textview, record_book_count_item_textview, record_book_count_item_time_textview;
        ImageView record_book_count_item_imageview;
        record_book_count_adapter.ItemClickListener itemClickListener;


        public image_board_ViewHolder( View view, record_book_count_adapter.ItemClickListener itemClickListener) {
            super(view);
            linearLayout = view.findViewById(R.id.linearLayout);
            record_book_count_item_number_textview = view.findViewById(R.id.record_book_count_item_number_textview);
            record_book_count_item_textview = view.findViewById(R.id.record_book_count_item_textview);
//            record_book_count_item_time_textview = view.findViewById(R.id.record_book_count_item_time_textview);
            record_book_count_item_imageview = view.findViewById(R.id.record_book_count_item_imageview);

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
}
