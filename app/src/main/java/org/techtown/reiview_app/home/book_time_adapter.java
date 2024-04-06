package org.techtown.reiview_app.home;


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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class book_time_adapter extends RecyclerView.Adapter<book_time_adapter.image_board_ViewHolder> {
    private Context context;
    private List<home_data> lists;
    int read_time;

    public book_time_adapter(Context context, List<home_data> lists) {
        this.context = context;
        this.lists = lists;
    }


    @Override
    public book_time_adapter.image_board_ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_time_item, parent, false);
        return new book_time_adapter.image_board_ViewHolder(view);
    }


    @Override
    public void onBindViewHolder( book_time_adapter.image_board_ViewHolder holder, int position) {
        home_data data = lists.get(position);


        int hrs = data.getToday_read_time() / 3600;
        int mins = (data.getToday_read_time() % 3600) / 60;
        int sec = data.getToday_read_time() % 60;
        String read_time_string = String.format("%02d:%02d:%02d", hrs, mins, sec);

        holder.book_time_time_textview.setText(read_time_string);
        holder.book_time_date_textview.setText(data.getStart_day());


//        holder.book_time_date_textview.setText(data.g);


    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class image_board_ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout_search;
        TextView book_time_date_textview, book_time_time_textview;
        ImageView book_time_time_imageview;


        public image_board_ViewHolder( View view) {
            super(view);
            linearLayout_search = view.findViewById(R.id.linearLayout_search);
            book_time_date_textview = view.findViewById(R.id.book_time_date_textview);
            book_time_time_textview = view.findViewById(R.id.book_time_time_textview);
            book_time_time_imageview = view.findViewById(R.id.book_time_time_imageview);
        }
    }
}
