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

public class setting_my_memo_adapter extends RecyclerView.Adapter<setting_my_memo_adapter.image_board_ViewHolder> {
    private Context context;
    private List<home_data> lists;
    private final setting_my_memo_adapter.ItemClickListener itemClickListener;


    public setting_my_memo_adapter(Context context, List<home_data> lists, setting_my_memo_adapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.lists = lists;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public setting_my_memo_adapter.image_board_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_my_memo_item, parent, false);
        return new setting_my_memo_adapter.image_board_ViewHolder(view, itemClickListener);
    }


    @Override
    public void onBindViewHolder(setting_my_memo_adapter.image_board_ViewHolder holder, int position) {
        home_data data = lists.get(position);


        //책 이미지
        Glide.with(context)
                .load(data.getImage())
                .into(holder.setting_my_memo_book_image_imageview);


        String subject = data.getBook_subject();
        if (subject.length() > 22) {
            holder.setting_my_memo_book_subject_textview.setText(subject.substring(0, 22) + "....");
        } else {
            holder.setting_my_memo_book_subject_textview.setText(data.getBook_subject());
        }

        if(data.getMemo_count() != null){
            holder.setting_my_memo_count_textview.setText(data.getMemo_count() + "개의 메모");
        }else{
            int hrs = data.getRead_time() / 3600;
            int mins = (data.getRead_time() % 3600) / 60;
            int sec = data.getRead_time() % 60;
            String time = String.format("%02d:%02d:%02d", hrs, mins, sec);
            holder.setting_my_memo_count_textview.setText("총 - "+time);

        }
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class image_board_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        TextView setting_my_memo_book_subject_textview, setting_my_memo_count_textview;
        ImageView setting_my_memo_book_image_imageview;
        setting_my_memo_adapter.ItemClickListener itemClickListener;


        public image_board_ViewHolder(View view, setting_my_memo_adapter.ItemClickListener itemClickListener) {
            super(view);
            linearLayout = view.findViewById(R.id.linearLayout);
            setting_my_memo_book_subject_textview = view.findViewById(R.id.setting_my_memo_book_subject_textview);
            setting_my_memo_count_textview = view.findViewById(R.id.setting_my_memo_count_textview);
            setting_my_memo_book_image_imageview = view.findViewById(R.id.setting_my_memo_book_image_imageview);


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
