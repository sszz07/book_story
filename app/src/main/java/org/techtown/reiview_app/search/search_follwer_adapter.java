package org.techtown.reiview_app.search;


import android.content.Context;
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
import org.techtown.reiview_app.login_data;

import java.util.List;

public class search_follwer_adapter extends RecyclerView.Adapter<search_follwer_adapter.image_board_ViewHolder> {
    private Context context;
    private List<login_data> lists;

    public search_follwer_adapter(Context context, List<login_data> lists ) {
        this.context = context;
        this.lists = lists;
    }


    @Override
    public search_follwer_adapter.image_board_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friends_record_item, parent, false);
        return new search_follwer_adapter.image_board_ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(search_follwer_adapter.image_board_ViewHolder holder, int position) {
        login_data data = lists.get(position);


        holder.friends_record_item_textview.setText(data.getNick());

        if (data.getImage().equals("이미지x")) {
            holder.friends_record_item_imagevieww.setVisibility(View.VISIBLE);
            holder.friends_record_item_imageview.setVisibility(View.INVISIBLE);
        } else {
            holder.friends_record_item_imagevieww.setVisibility(View.INVISIBLE);
            holder.friends_record_item_imageview.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load("http://52.79.180.89/kakao_story" + data.getImage())
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


    public class image_board_ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout_friends;
        TextView friends_record_item_textview;
        ImageView friends_record_item_imageview, friends_record_item_imagevieww;


        public image_board_ViewHolder(View view ) {
            super(view);
            linearLayout_friends = view.findViewById(R.id.linearLayout);
            friends_record_item_imageview = view.findViewById(R.id.friends_record_item_imageview);
            friends_record_item_imagevieww = view.findViewById(R.id.friends_record_item_imagevieww);
            friends_record_item_textview = view.findViewById(R.id.friends_record_item_textview);
        }
    }

}
