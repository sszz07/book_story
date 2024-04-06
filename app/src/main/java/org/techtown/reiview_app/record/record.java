package org.techtown.reiview_app.record;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.techtown.reiview_app.ApiClient;
import org.techtown.reiview_app.ApiInterface;
import org.techtown.reiview_app.R;
import org.techtown.reiview_app.home.home_adapter;
import org.techtown.reiview_app.home.home_data;
import org.techtown.reiview_app.home.home_select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class record extends Fragment {
    TextView record_year_month_textview, record_book_count_textview, record_this_month_textview, record_graph_this_month_textview, record_graph_book_count_textview, record_graph_book_textview;
    RecyclerView record_book_recyclerview;
    public static int month;
    static int years;
    public Calendar cal = Calendar.getInstance();
    View view;
    String email;
    List<home_data> list = new ArrayList<>();
    record_book_count_adapter adapter;
    record_book_count_adapter.ItemClickListener itemClickListener;

    private BarChart barChart;
    ArrayList<String> xVals = new ArrayList<String>(); // 변환할 String 형태 x축 값
    ArrayList<String> yVals = new ArrayList<String>(); // 변환할 String 형태 y축 값
    ArrayList<String> record_date_array = new ArrayList<String>();
    ArrayList<Integer> read_time_array = new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);


        record_graph_book_textview = (TextView) view.findViewById(R.id.record_graph_book_textview);
        record_year_month_textview = (TextView) view.findViewById(R.id.record_year_month_textview);
        record_book_count_textview = (TextView) view.findViewById(R.id.record_book_count_textview);
        record_this_month_textview = (TextView) view.findViewById(R.id.record_this_month_textview);
        record_graph_this_month_textview = (TextView) view.findViewById(R.id.record_graph_this_month_textview);
        record_graph_book_count_textview = (TextView) view.findViewById(R.id.record_graph_book_count_textview);


        record_book_recyclerview = (RecyclerView) view.findViewById(R.id.record_book_recyclerview);


        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        record_year_month_textview.setText(String.valueOf(year) + "년 " + month + "월 ▽");

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM");
        String getTime = sdf.format(date);

        if ((String.valueOf(year) + " " + String.valueOf(month)).equals(getTime)) {
            record_this_month_textview.setText("이번달은");
        } else {
            record_this_month_textview.setText(String.valueOf(month) + "월은");
        }


        record_book_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        record_year_month_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyYearMonthPickerDialog pd = new MyYearMonthPickerDialog();//객체 만듬
                pd.setListener(d);//MyYearMonthPickerDialog에 setListener 생성자로 만듬

                /*getSupportFragmentManager()는 액티비티에 프래그먼트를 호출하기 위해서 사용하는것
                 * 왜 사용? public class MyYearMonthPickerDialog extends DialogFragment 클래스에서 상속한것을 보면 DialogFragment 다이얼로그 프래그먼트로 되어있다*/
                pd.show(getActivity().getSupportFragmentManager(), "YearMonthPickerTest");
            }
        });

        SharedPreferences pref = getActivity().getSharedPreferences("현재유저", MODE_PRIVATE);
        email = pref.getString("이메일", "");
        book_record_select(email, String.valueOf(year) + "-" + String.valueOf(month));
        book_record_graph_select(email, year, month);

        itemClickListener = new record_book_count_adapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int idx = list.get(position).getIdx();
                String book_subject = list.get(position).getBook_subject();
                String subject = list.get(position).getSubject();
                String make = list.get(position).getMake();
                String writer = list.get(position).getWriter();
                String image = list.get(position).getImage();
                int read_time = list.get(position).getRead_time();
                int read_page = list.get(position).getRead_page();
                String book_page = list.get(position).getBook_page();

                Log.e("idx 데이터 확인", String.valueOf(idx));

                Intent intent = new Intent(getContext(), home_select.class);
                intent.putExtra("book_subject", subject);
                intent.putExtra("make", make);
                intent.putExtra("writer", writer);
                intent.putExtra("image", image);
                intent.putExtra("idx", idx);
                intent.putExtra("read_time", read_time);
                intent.putExtra("read_page", String.valueOf(read_page));
                intent.putExtra("book_page", book_page);

                startActivity(intent);
            }
        };

        return view;
    }


    //년도와 월별 값을 데이터값을 받는곳
    /*DatePickerDialog 예약 기능에서 많이 사용되는 날짜를 사용할수있는 라이브러리 이다*/
    /*OnDateSetListener 사용자가 날짜 선택을 완료했음을 알리는 인터페이스*/
    /*onDateSet는 사용자가 선택한 날짜를 데이터로 전달 받아서 사용할수 있게 해준다*/
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM");
            String getTime = sdf.format(date);

            if ((String.valueOf(year) + " " + String.valueOf(monthOfYear)).equals(getTime)) {
                record_this_month_textview.setText("이번달은");
                record_graph_this_month_textview.setText("이번달은");
            } else {
                record_this_month_textview.setText(String.valueOf(monthOfYear) + "월은");
                record_graph_this_month_textview.setText(String.valueOf(monthOfYear) + "월은");
            }

            Log.d("YearMonthPickerTest", "year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);
            book_record_select(email, String.valueOf(year) + "-" + String.valueOf(monthOfYear));
            book_record_graph_select(email, year, monthOfYear);
            record_year_month_textview.setText(String.valueOf(year) + "년 " + monthOfYear + "월 ▽");
            month = monthOfYear;
            years = year;
        }
    };


    // INSERT 구문 은 WHERE 절을 지원하지 않으므로 쿼리가 실패합니다. id열이 고유하거나 기본 키 라고 가정합니다 .
    private void book_record_select(String email, String record_date) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<home_data>> call = home_select.book_record_select(email, record_date);
        call.enqueue(new Callback<List<home_data>>() {
            @Override
            public void onResponse(Call<List<home_data>> call, Response<List<home_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println(String.valueOf(response.body().size() + "권"));
                    record_book_count_textview.setText(String.valueOf(response.body().size() + "권"));
                    onGetResult(response.body());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<home_data>> call, Throwable t) {
                Log.e("record_recyclerview_select()", "에러 : " + t.getMessage());
            }
        });
    }

    private void onGetResult(List<home_data> lists) {
        if (lists.size() == 0) {
            record_book_recyclerview.setVisibility(View.GONE);
        } else {
            record_book_recyclerview.setVisibility(View.VISIBLE);
            adapter = new record_book_count_adapter(getActivity(), lists, itemClickListener);
            adapter.notifyDataSetChanged();
            record_book_recyclerview.setAdapter(adapter);
            list = lists;
        }
    }


    private void book_record_graph_select(String email, int year_graph, int month_graph) {
        ApiInterface home_select = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Record_data>> call = home_select.book_record_graph_select(email);
        call.enqueue(new Callback<List<Record_data>>() {
            @Override
            public void onResponse(Call<List<Record_data>> call, Response<List<Record_data>> response) {
                //response.body()가 null이 나온 이유는 서버에서 json_encode를 해주지 않아서 이다 그리고 mysqli_query($con, $sql) 연결이 되어있는지 확인하기
                if (response.isSuccessful() && response.body() != null) {

                    try {
                        for (int i = 0; i <= response.body().size(); i++) {
                            int time = response.body().get(i).getRead_time();
                            String record_date = response.body().get(i).getRecord_date();
                            if (record_date.equals(year_graph + "-" + month_graph)) {
                                if (time < 60) {
                                    int sec = time % 60;
                                    String time_second = String.format("%2d초", sec);
                                    record_graph_book_count_textview.setText(time_second);

                                } else if (time < 3600) {
                                    int mins = (time % 3600) / 60;
                                    int sec = time % 60;
                                    String time_second = String.format("%2d분 %02d초", mins, sec);
                                    record_graph_book_count_textview.setText(time_second);

                                } else {
                                    int hrs = time / 3600;
                                    int mins = (time % 3600) / 60;
                                    int sec = time % 60;
                                    String time_second = String.format("%02d시 %2d분 %02d초", hrs, mins, sec);
                                    record_graph_book_count_textview.setText(time_second);
                                }
                                break;
                            }else{
                                record_graph_book_count_textview.setText("0초");
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {

                    }


                    setChart(response.body(), year_graph, month_graph);
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<Record_data>> call, Throwable t) {
                Log.e("record_recyclerview_select()", "에러 : " + t.getMessage());
            }
        });
    }

    @SuppressLint("ResourceType")
    private void setChart(List<Record_data> records, int year_graph, int month_graph) {

//        ArrayList<BarEntry> entry_chart = new ArrayList<>(); // 데이터를 담을 Arraylist
//
//        barChart = (BarChart) view.findViewById(R.id.chart);
//
//        BarData barData = new BarData(); // 차트에 담길 데이터
//
////        int p = 2;
////        entry_chart.add(new BarEntry(1, 2)); //entry_chart1에 좌표 데이터를 담는다.
////        entry_chart.add(new BarEntry(2, 2));
////        entry_chart.add(new BarEntry(3, 3));
////        entry_chart.add(new BarEntry(4, 4));
////        entry_chart.add(new BarEntry(5, 2));
////        entry_chart.add(new BarEntry(6, 8));
//
//        for (int i = 0; i <= 11; i++) {
//            String xValue = i + 1 + "월";
//            Log.d("x값 확인", xValue);
//            entry_chart.add(new BarEntry(i, i));
//            xVals.add(xValue);
//        }
//
//        BarDataSet barDataSet = new BarDataSet(entry_chart, ""); // 데이터가 담긴 Arraylist 를 BarDataSet 으로 변환한다.
//        barDataSet.setColors(new int[] {Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE,Color.BLUE,Color.BLUE,Color.GRAY,Color.BLUE,Color.BLUE,});
////        barDataSet.setColors(1,Color.BLACK);
//        barData.addDataSet(barDataSet); // 해당 BarDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.
//        barChart.setData(barData); // 차트에 위의 DataSet 을 넣는다.
//        barChart.invalidate(); // 차트 업데이트
//        barChart.setTouchEnabled(false); // 차트 터치 불가능하게
//        barData.setDrawValues(false);//그래프의 속성값 없애는 메소드
//
//
//        barDataSet.setColors(new int[]{ContextCompat.getColor(getContext(),R.color.black),
//                ContextCompat.getColor(getContext(),R.color.black);
//
//        XAxis xAxis = barChart.getXAxis(); // XAxis : x축 속성 설정하기 위해서 xAxis 객체 만들어줌
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals)); // String 형태의 x 값이 들어있는 배열을 Formmatter 인자값으로 넣어줌]
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 아래쪽
//        xAxis.setTextSize(12f); // x축에 표출되는 텍스트의 크기
//        xAxis.setDrawGridLines(false); //x축의 그리드 라인을 없앰
//        xAxis.setLabelCount(12); //x축의 데이터를 최대 몇 개 까지 나타낼지에 대한 설정
//
//
//        // YAxis(Right) (왼쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
//        YAxis yAxisRight = barChart.getAxisRight();
//        yAxisRight.setDrawLabels(false); //y축에 있는 데이터 없애기
//        yAxisRight.setDrawAxisLine(false); //y축 선 없애기
//        yAxisRight.setDrawGridLines(false);//그리드 업애기
////
////
////        lineChart.invalidate(); // 차트 업데이트
////
////
//        YAxis yAxisLeft = barChart.getAxisLeft();
////        yAxisLeft.setValueFormatter(new IndexAxisValueFormatter(yVals)); // String 형태의 x 값이 들어있는 배열을 Formmatter 인자값으로 넣어줌]
////        yAxisLeft.setTextSize(11f); // x축에 표출되는 텍스트의 크기
////        yAxisLeft.setDrawGridLines(false); //x축의 그리드 라인을 없앰
////        yAxisLeft.setLabelCount(10); //x축의 데이터를 최대 몇 개 까지 나타낼지에 대한 설정
//
//        yAxisLeft.setDrawLabels(false); //y축에 있는 데이터 없애기
//        yAxisLeft.setDrawAxisLine(false); //y축 선 없애기
//        yAxisLeft.setDrawGridLines(false);//그리드 업애기)

        BarChart chart = view.findViewById(R.id.chart);

        // Some made up data - replace these with your
        //  queryYdata, queryXdata, and queryZdata methods
        List<Integer> credits = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13));
        List<BarEntry> entries = new ArrayList<>();


        //사이즈가 5라면 5까지 값을 넣고

        try {
            for (int i = 0; i <= records.size(); i++) {
                record_date_array.add(i, records.get(i).getRecord_date());
            }
        } catch (IndexOutOfBoundsException e) {

        }
        for (int i = records.size() ; i <= 11; i++) {
            record_date_array.add(i, null);
        }




        try {
            for (int i = 0; i <= records.size(); i++) {
                read_time_array.add(i, records.get(i).getRead_time());
            }
        } catch (IndexOutOfBoundsException e) {

        }

        for (int i = records.size(); i <= 11; i++) {
            read_time_array.add(i, 0);
        }


        if(records.size() == 0){
            chart.setVisibility(View.GONE);
            record_graph_book_count_textview.setText("0초");
        }


        for (int i = 0; i <= 11; i++) {
            String xValue = i + 1 + "월";
            int j = i + 1;
            System.out.println(j + "jjjjjj");
            String value = year_graph + "-" + j;

            if(record_date_array.contains(value)){
                int index = record_date_array.indexOf(value);
                entries.add(new BarEntry(i, read_time_array.get(index)));
                xVals.add(xValue);
            }else{
                entries.add(new BarEntry(i, 0));
                xVals.add(xValue);
            }
        }


        float textSize = 13f;

        MyBarDataset dataSet = new MyBarDataset(entries, "data", credits, month_graph);
        dataSet.setColors(ContextCompat.getColor(getContext(), R.color.blue),
                ContextCompat.getColor(getContext(), R.color.gray));
        BarData data = new BarData(dataSet);
        data.setDrawValues(false);


        //x축 라인
        chart.setData(data);
        chart.setFitBars(true);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setTextSize(textSize);
        chart.getAxisLeft().setTextSize(textSize);
        chart.setExtraBottomOffset(10f);
        chart.getXAxis().setLabelCount(12);


        chart.getAxisRight().setEnabled(false);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);


        //y축 라인
        YAxis yAxisLeft = chart.getAxisLeft();
//        yAxisLeft.setValueFormatter(new IndexAxisValueFormatter(yVals)); // String 형태의 x 값이 들어있는 배열을 Formmatter 인자값으로 넣어줌]
//        yAxisLeft.setTextSize(11f); // x축에 표출되는 텍스트의 크기
//        yAxisLeft.setDrawGridLines(false); //x축의 그리드 라인을 없앰
//        yAxisLeft.setLabelCount(10); //x축의 데이터를 최대 몇 개 까지 나타낼지에 대한 설정

        yAxisLeft.setDrawLabels(false); //y축에 있는 데이터 없애기
        yAxisLeft.setDrawAxisLine(false); //y축 선 없애기
        yAxisLeft.setDrawGridLines(false);//그리드 업애기)

        chart.invalidate();
    }

    public class MyBarDataset extends BarDataSet {

        private List<Integer> credits;
        int month_graph;

        MyBarDataset(List<BarEntry> yVals, String label, List<Integer> credits, int month_graph) {
            super(yVals, label);
            this.credits = credits;
            this.month_graph = month_graph;
        }

        @Override
        public int getColor(int index) {
            float c = credits.get(index);

            if (c == month_graph) {
                return mColors.get(0);
            } else {
                return mColors.get(1);
            }
        }
    }
}