package org.techtown.reiview_app.setting;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.home.home_data;
import org.techtown.reiview_app.search.search_data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class setting_read_book_adapter extends RecyclerView.Adapter<setting_read_book_adapter.image_board_ViewHolder> {
    private Context context;
    private List<home_data> lists;
    private final setting_read_book_adapter.ItemClickListener itemClickListener;


    public setting_read_book_adapter(Context context, List<home_data> lists, setting_read_book_adapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.lists = lists;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public setting_read_book_adapter.image_board_ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.read_book_item, parent, false);
        return new setting_read_book_adapter.image_board_ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder( setting_read_book_adapter.image_board_ViewHolder holder, int position) {
        home_data data = lists.get(position);


        //책 이미지
        Glide.with(context)
                .load(data.getImage())
                .into(holder.read_book_imageview);



        String subject = data.getBook_subject();
        if(subject.length() >30){
            holder.read_book_subject_textview.setText(subject.substring(0, 30)+"....");
        }else {
            holder.read_book_subject_textview.setText(data.getBook_subject());
        }
//독서시간 리사이클러뷰뷰
        int all_read_book_page_int = data.getRead_page();
        holder.read_book_percent_textview.setText(String.valueOf(all_read_book_page_int)+" page");

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
            holder.read_book_date_textview.setText("안읽음 | ");
        }else{
            holder.read_book_date_textview.setText(시간값+" 읽음 | ");
        }
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class image_board_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        TextView read_book_subject_textview,read_book_percent_textview,read_book_date_textview;
        ImageView read_book_imageview;
        setting_read_book_adapter.ItemClickListener itemClickListener;


        public image_board_ViewHolder( View view, setting_read_book_adapter.ItemClickListener itemClickListener) {
            super(view);
            linearLayout = view.findViewById(R.id.linearLayout);
            read_book_date_textview = view.findViewById(R.id.read_book_date_textview);
            read_book_subject_textview = view.findViewById(R.id.read_book_subject_textview);
            read_book_percent_textview = view.findViewById(R.id.read_book_percent_textview);
            read_book_imageview = view.findViewById(R.id.read_book_imageview);
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
