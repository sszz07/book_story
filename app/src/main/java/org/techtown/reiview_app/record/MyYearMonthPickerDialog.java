package org.techtown.reiview_app.record;


import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import org.techtown.reiview_app.R;

import java.util.Calendar;


/*DialogFragment 다이얼로그를 사용할수 있는 프래그먼트*/
public class MyYearMonthPickerDialog extends DialogFragment {

    private static final int MAX_YEAR = 2030;
    private static final int MIN_YEAR = 2023;

    private DatePickerDialog.OnDateSetListener listener;
    public Calendar cal = Calendar.getInstance();

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    Button btnConfirm;
    Button btnCancel;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        /*getActivity() 프래그먼트에서 액티비티를 호출하기 위해서*/
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.activity_my_year_month_picker_dialog, null);

        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

//        score.setValue(cal.get(Calendar.MONTH) + 1);
        //취소버튼
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyYearMonthPickerDialog.this.getDialog().cancel();
            }
        });

        //확인버튼
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                //다이얼로그를 종료하는 코드
                MyYearMonthPickerDialog.this.getDialog().cancel();
            }
        });


        //처음으로 다이얼로그가 작동을 했을때
        if(record.month == 0){
            monthPicker.setMinValue(1);//1월
            monthPicker.setMaxValue(12);//2월
            monthPicker.setValue(cal.get(Calendar.MONTH) + 1);
        }
        //날짜를 선택하고 다이얼로그가 작동을 했을때
        else{
            monthPicker.setMinValue(1);//1월
            monthPicker.setMaxValue(12);//2월
            monthPicker.setValue(record.month);
        }


        if(record.years == 0){
            int year = cal.get(Calendar.YEAR);
            yearPicker.setMinValue(MIN_YEAR);
            yearPicker.setMaxValue(MAX_YEAR);
            yearPicker.setValue(year);
        }
        //날짜를 선택하고 다이얼로그가 작동을 했을때
        else{
            yearPicker.setMinValue(MIN_YEAR);
            yearPicker.setMaxValue(MAX_YEAR);
            yearPicker.setValue(record.years);
        }



        //프래그먼트에서 만든 다이얼로그 인플레이터 객체 세팅
        builder.setView(dialog);
        return builder.create();
    }
}