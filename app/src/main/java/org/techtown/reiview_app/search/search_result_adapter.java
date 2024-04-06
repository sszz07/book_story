package org.techtown.reiview_app.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.reiview_app.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class search_result_adapter extends RecyclerView.Adapter<search_result_adapter.ViewHolder> {
    private ArrayList<book_data> book_data_list = new ArrayList<>();

    private Context context;
    String itemPage;

    //===== [Click 이벤트 구현을 위해 추가된 코드] ==========================
    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(int position, String image,String book_subject,String make,String date,String book_content,String isbn,String book_page,String author,String itemId);
    }

    // OnItemClickListener 참조 변수 선언
    private OnItemClickListener itemClickListener;

    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }
    //======================================================================


    // 아이템 뷰를 저장하는 viewholder 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView search_result_title_textview;     // 영화이름
        TextView search_result_make_center_textview;     // 누적관객수
        TextView search_result_date_textview;     // 영화개봉일
        TextView search_result_writer_textview;
        ImageView search_result_book_image_imageview;

        public ViewHolder( View itemView) {
            super(itemView);

            search_result_title_textview = itemView.findViewById(R.id.search_result_title_textview);
            search_result_make_center_textview = itemView.findViewById(R.id.search_result_make_center_textview);
            search_result_date_textview = itemView.findViewById(R.id.search_result_date_textview);
            search_result_writer_textview = itemView.findViewById(R.id.search_result_writer_textview);
            search_result_book_image_imageview = itemView.findViewById(R.id.search_result_book_image_imageview);
        }


    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 return

    @Override
    public search_result_adapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.search_book_item, parent, false);
        search_result_adapter.ViewHolder viewHolder = new search_result_adapter.ViewHolder(view);

        //===== [Click 이벤트 구현을 위해 추가된 코드] =====================
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                book_data data = book_data_list.get(position);
                if (position != RecyclerView.NO_POSITION) {
                    String urlAddress = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbsszz071522003&itemIdType=ISBN&ItemId="+data.getIsbn()+"&output=js&Version=20131101&OptResult=ebookList,usedList,reviewList";
                   System.out.println(urlAddress+"urlAddress2");

                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(urlAddress);

                                InputStream is = url.openStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader reader = new BufferedReader(isr);

                                StringBuffer buffer = new StringBuffer();
                                String line = reader.readLine();
                                while (line != null) {
                                    buffer.append(line + "\n");
                                    line = reader.readLine();
                                }
                                String jsonData = buffer.toString();

                                // jsonData를 먼저 JSONObject 형태로 바꾼다.
                                JSONObject obj = new JSONObject(jsonData);

                                // boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출
                                JSONArray dailyBoxOfficeList = (JSONArray) obj.get("item");

                                JSONObject temp = dailyBoxOfficeList.getJSONObject(0);

                                JSONObject temp2 = temp.getJSONObject("subInfo");



                                // list의 json 값들을 넣는다.
                                itemPage = temp2.getString("itemPage");
                                itemClickListener.onItemClicked(position, data.getBook_image(),data.getTitle(),data.getMake_center(),data.getDate(),data.getBook_content(), data.getIsbn(),itemPage,data.getWirter(),data.getItemId());

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
        //==================================================================

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( search_result_adapter.ViewHolder holder, int position) {
        book_data data = book_data_list.get(position);

        holder.search_result_title_textview.setText(data.getTitle());
        holder.search_result_make_center_textview.setText(data.getMake_center());
        holder.search_result_date_textview.setText(data.getDate());
        holder.search_result_writer_textview.setText(data.getWirter()+"  |");

        Glide.with(context)
                .load(data.getBook_image())
                .into(holder.search_result_book_image_imageview);
    }


    // 사이즈
    @Override
    public int getItemCount() {
        return book_data_list.size();
    }


    // data 모델의 객체들을 list에 저장
    public void setmovieList(Context context,ArrayList<book_data> list) {
        this.context = context;
        this.book_data_list = list;
        notifyDataSetChanged();
    }
}
