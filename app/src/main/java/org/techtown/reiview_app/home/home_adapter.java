package org.techtown.reiview_app.home;

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

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class home_adapter extends RecyclerView.Adapter<home_adapter.image_board_ViewHolder> {
    private Context context;
    private List<home_data> lists;
    private final ItemClickListener itemClickListener;

    public home_adapter(Context context, List<home_data> lists, ItemClickListener itemClickListener) {
        this.context = context;
        this.lists = lists;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public image_board_ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false);
        return new image_board_ViewHolder(view, itemClickListener);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})

    @Override
    public void onBindViewHolder( image_board_ViewHolder holder, int position) {
        home_data data = lists.get(position);
        //전체 권수
        String book_count = String.valueOf(lists.size());
        String book_my_count = String.valueOf(position + 1);

        holder.home_book_count_textview.setText(book_count);

        //나의 책 권수
        holder.home_book_my_count_textview.setText(book_my_count);

        String subject = data.getBook_subject();
        if(subject.length() >30){
            holder.home_book_subejct_textview.setText(subject.substring(0, 30)+"....");
        }else {
            holder.home_book_subejct_textview.setText(data.getBook_subject());
        }

        //책 제목



        //책 메모갯수
        holder.home_memo_count_textview.setText(data.getMemo_count());


        System.out.println(data.getImage()+"data.getImage()data.getImage()");
        //책 이미지
        Glide.with(context)
                .load(data.getImage())
                .into(holder.home_book_image_imageview);


        class TIME_MAXIMUM {
            public static final int SEC = 60;
            public static final int MIN = 60;
            public static final int HOUR = 24;
            public static final int DAY = 30;
            public static final int MONTH = 12;
        }

        long 현재시간 = System.currentTimeMillis();
        long 시간계산 = (현재시간 - data.getPresent_time()) / 1000;
        long 시간 = data.getPresent_time();
        System.out.println(시간+"시간시간시간");
        String 시간값 = null;
        if (시간계산 < TIME_MAXIMUM.SEC) {
            시간값 = "방금";
        } else if ((시간계산 /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            시간값 = 시간계산 + "분전";
        } else if ((시간계산 /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            시간값 = (시간계산) + "시간전";
        } else if ((시간계산 /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            시간값 = (시간계산) + "일전";
        } else if ((시간계산 /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            시간값 = (시간계산) + "달전";
        } else {
            시간값 = (시간계산) + "년전";
        }

        if(시간 == 0){
            holder.home_book_time_textview.setText("안읽음");
        }else{
            holder.home_book_time_textview.setText(시간값+" 읽음");
        }


//독서시간 리사이클러뷰뷰
        int all_read_book_page_int = data.getRead_page();
        int book_page_int = Integer.parseInt(data.getBook_page());
        double percent = ((double) all_read_book_page_int / (double) book_page_int) * 100.0;

        System.out.println("percent : " + String.format("%.0f", percent) + "%");

        holder.home_book_my_percent_textview.setText(String.format("%.1f", percent)+"%");


    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class image_board_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        public TextView home_book_my_count_textview, home_book_count_textview, home_book_subejct_textview, home_book_time_textview, home_memo_count_textview,home_book_my_percent_textview;
        ImageView home_book_image_imageview;
        ItemClickListener itemClickListener;


        public image_board_ViewHolder( View view, ItemClickListener itemClickListener) {
            super(view);
            linearLayout = view.findViewById(R.id.linear_layout);
            home_book_my_count_textview = view.findViewById(R.id.home_book_my_count_textview);
            home_book_count_textview = view.findViewById(R.id.home_book_count_textview);
            home_book_subejct_textview = view.findViewById(R.id.home_book_subejct_textview);
            home_book_time_textview = view.findViewById(R.id.home_book_time_textview);
            home_memo_count_textview = view.findViewById(R.id.home_memo_count_textview);
            home_book_image_imageview = view.findViewById(R.id.home_book_image_imageview);

            home_book_my_percent_textview = view.findViewById(R.id.home_book_my_percent_textview);

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
