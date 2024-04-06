package org.techtown.reiview_app.search;


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
import org.techtown.reiview_app.login_data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class friends_adapter extends RecyclerView.Adapter<friends_adapter.image_board_ViewHolder> {
    private Context context;
    private List<login_data> lists;
    private final friends_adapter.ItemClickListener itemClickListener;

    public friends_adapter(Context context, List<login_data> lists, friends_adapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.lists = lists;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public friends_adapter.image_board_ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friends_record_item, parent, false);
        return new friends_adapter.image_board_ViewHolder(view, itemClickListener);
    }


    @Override
    public void onBindViewHolder( friends_adapter.image_board_ViewHolder holder, int position) {
        login_data data = lists.get(position);


        holder.friends_record_item_textview.setText(data.getNick());

        if(data.getProfile_image() == null){
            holder.friends_record_item_imagevieww.setVisibility(View.VISIBLE);
            holder.friends_record_item_imageview.setVisibility(View.INVISIBLE);


        }else{
            holder.friends_record_item_imagevieww.setVisibility(View.INVISIBLE);
            holder.friends_record_item_imageview.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load("http://52.79.180.89/kakao_story" + data.getProfile_image())
                    .apply(new RequestOptions().circleCrop())
                    .into(holder.friends_record_item_imageview);
        }
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void filterList(List<login_data> filteredList) {
        lists = filteredList;
        notifyDataSetChanged();
    }


    public class image_board_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout_friends;
        TextView friends_record_item_textview;
        ImageView friends_record_item_imageview,friends_record_item_imagevieww;
        friends_adapter.ItemClickListener itemClickListener;


        public image_board_ViewHolder( View view, friends_adapter.ItemClickListener itemClickListener) {
            super(view);
            linearLayout_friends = view.findViewById(R.id.linearLayout);
            friends_record_item_imageview = view.findViewById(R.id.friends_record_item_imageview);
            friends_record_item_imagevieww = view.findViewById(R.id.friends_record_item_imagevieww);
            friends_record_item_textview = view.findViewById(R.id.friends_record_item_textview);

            this.itemClickListener = itemClickListener;
            linearLayout_friends.setOnClickListener(this);

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
