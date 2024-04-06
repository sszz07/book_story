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
import android.widget.RatingBar;
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

public class setting_finish_book_adapter extends RecyclerView.Adapter<setting_finish_book_adapter.image_board_ViewHolder> {
    private Context context;
    private List<home_data> lists;
    private final setting_finish_book_adapter.ItemClickListener itemClickListener;


    public setting_finish_book_adapter(Context context, List<home_data> lists, setting_finish_book_adapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.lists = lists;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public setting_finish_book_adapter.image_board_ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.finish_book_item, parent, false);
        return new setting_finish_book_adapter.image_board_ViewHolder(view, itemClickListener);
    }


    @Override
    public void onBindViewHolder( setting_finish_book_adapter.image_board_ViewHolder holder, int position) {
        home_data data = lists.get(position);


        //책 이미지
        Glide.with(context)
                .load(data.getImage())
                .into(holder.finish_book_imageview);




        //subject의 길이의 값이 10 초과가 된다면
        //ex)
        //String substring() = "0123456789"
        // str.substring(5)의 값은 56789 이다

//        if(subject.length() > 10){
//
//            //substring()-문자열의 앞에서 부터 몇번째 위치까지 지정하는 값이다
//            String subject2 = subject.substring(5);
//            holder.finish_book_subject_textview.setText(subject2 + "...");
//        }

        String subject = data.getBook_subject();
        if(subject.length() >30){
            holder.finish_book_subject_textview.setText(subject.substring(0, 30)+"....");
        }else {
            holder.finish_book_subject_textview.setText(data.getBook_subject());
        }

        holder.finish_book_my_content_textview.setText(data.getMy_content());
        holder.finish_book_date_textview.setText(data.getMy_content());
        holder.finish_book_rating_bar.setRating(data.getBook_star());

        holder.finish_book_date_textview.setText(data.getFinish());
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class image_board_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        TextView finish_book_subject_textview,finish_book_my_content_textview,finish_book_date_textview;
        RatingBar finish_book_rating_bar;
        ImageView finish_book_imageview;
        setting_finish_book_adapter.ItemClickListener itemClickListener;


        public image_board_ViewHolder( View view, setting_finish_book_adapter.ItemClickListener itemClickListener) {
            super(view);
            linearLayout = view.findViewById(R.id.linearLayout);
            finish_book_subject_textview = view.findViewById(R.id.finish_book_subject_textview);
            finish_book_my_content_textview = view.findViewById(R.id.finish_book_my_content_textview);
            finish_book_date_textview = view.findViewById(R.id.finish_book_date_textview);
            finish_book_imageview = view.findViewById(R.id.finish_book_imageview);
            finish_book_rating_bar = view.findViewById(R.id.finish_book_rating_bar);
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

