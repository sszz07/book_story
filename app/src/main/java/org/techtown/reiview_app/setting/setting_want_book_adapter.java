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

public class setting_want_book_adapter extends RecyclerView.Adapter<setting_want_book_adapter.image_board_ViewHolder> {
    private Context context;
    private List<home_data> lists;
    private final setting_want_book_adapter.ItemClickListener itemClickListener;


    public setting_want_book_adapter(Context context, List<home_data> lists, setting_want_book_adapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.lists = lists;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public setting_want_book_adapter.image_board_ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.want_book_item, parent, false);
        return new setting_want_book_adapter.image_board_ViewHolder(view, itemClickListener);
    }


    @Override
    public void onBindViewHolder( setting_want_book_adapter.image_board_ViewHolder holder, int position) {
        home_data data = lists.get(position);


        //책 이미지
        Glide.with(context)
                .load(data.getImage())
                .into(holder.want_book_imageview);



//        if(subject.length() > 10){
//            String subject2 = subject.substring(5);
//            holder.want_book_subject_textview.setText(subject2 + "...");
//        }else{
//            holder.want_book_subject_textview.setText(data.getBook_subject());
//        }



        String subject = data.getBook_subject();
        if(subject.length() >30){
            holder.want_book_subject_textview.setText(subject.substring(0, 30)+"....");
        }else {
            holder.want_book_subject_textview.setText(data.getBook_subject());
        }

        holder.want_book_writer_textview.setText(data.getWriter());
        holder.want_book_make_textview.setText(" | "+data.getMake());
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class image_board_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        TextView want_book_subject_textview,want_book_writer_textview,want_book_make_textview;
        ImageView want_book_imageview;
        setting_want_book_adapter.ItemClickListener itemClickListener;


        public image_board_ViewHolder( View view, setting_want_book_adapter.ItemClickListener itemClickListener) {
            super(view);
            linearLayout = view.findViewById(R.id.linearLayout);
            want_book_subject_textview = view.findViewById(R.id.want_book_subject_textview);
            want_book_writer_textview = view.findViewById(R.id.want_book_writer_textview);
            want_book_make_textview = view.findViewById(R.id.want_book_make_textview);
            want_book_imageview = view.findViewById(R.id.want_book_imageview);
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
