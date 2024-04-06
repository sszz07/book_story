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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class search_adapter extends RecyclerView.Adapter<search_adapter.image_board_ViewHolder> {
    private Context context;
    private List<search_data> lists;
    private final search_adapter.ItemClickListener itemClickListener;
    String email;

    public search_adapter(Context context, List<search_data> lists, search_adapter.ItemClickListener itemClickListener, String email) {
        this.context = context;
        this.lists = lists;
        this.email = email;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public search_adapter.image_board_ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_record_item, parent, false);
        return new search_adapter.image_board_ViewHolder(view, itemClickListener);
    }


    @Override
    public void onBindViewHolder( search_adapter.image_board_ViewHolder holder, int position) {
        search_data data = lists.get(position);


        holder.search_textview.setText(data.getBook_name());


        holder.search_item_cancel_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_info_delete(email, data.getBook_name());
                lists.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, lists.size());
            }
        });
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class image_board_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout_search;
        TextView search_textview;
        ImageView search_item_cancel_imageview;
        search_adapter.ItemClickListener itemClickListener;


        public image_board_ViewHolder( View view, search_adapter.ItemClickListener itemClickListener) {
            super(view);
            linearLayout_search = view.findViewById(R.id.linearLayout_search);
            search_textview = view.findViewById(R.id.search_textview);
            search_item_cancel_imageview = view.findViewById(R.id.search_item_cancel_imageview);

            this.itemClickListener = itemClickListener;
            linearLayout_search.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    private void search_info_delete(String email, String book_name) {
        ApiInterface search_delete = ApiClient.getClient().create(ApiInterface.class);
        Call<search_data> call = search_delete.search_info_delete(email, book_name);
        call.enqueue(new Callback<search_data>() {
            @Override
            public void onResponse( Call<search_data> call,  Response<search_data> response) {

            }

            @Override
            public void onFailure(Call<search_data> call, Throwable t) {

            }
        });
    }
}
