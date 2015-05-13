package com.piaojin.myview;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.TimeZone;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/5/12.
 */

//日期选择
public class DateDialog extends DialogFragment{


    private String yeartext;
    private String daytext;
    private Button selectTime;
    private Button cancel;
    private View view;
    private DatePicker datepicker;
    private TimePicker timepicker;
    private EditText editText;
    private Context context;

    public DateDialog(Context context, EditText editText) {
        this.context = context;
        this.editText = editText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.common_datetime, null);
        init(view);
        return view;
    }

    private void init(View view){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        selectTime=(Button)view.findViewById(R.id.selectTime);
        timepicker=(TimePicker)view.findViewById(R.id.timepicker);
        timepicker.setOnTimeChangedListener(new MyOnTimeChangedListener());
        timepicker.setIs24HourView(true);
        datepicker=(DatePicker)view.findViewById(R.id.datepicker);
        datepicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),new MyOnDateChangedListener());
        cancel=(Button)view.findViewById(R.id.cancel);
        selectTime.setOnClickListener(new MyOnClickListener());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener{


        @Override
        public void onClick(View view) {

            yeartext=datepicker.getYear()+"-"+(datepicker.getMonth()+1)+"-"+datepicker.getDayOfMonth()+" ";
            daytext=timepicker.getCurrentHour()+":"+timepicker.getCurrentMinute()+":0";
            editText.setText(yeartext + daytext);
                    dismiss();
        }
    }

    private class MyOnDateChangedListener implements DatePicker.OnDateChangedListener {


        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

            yeartext=datepicker.getYear()+"-"+datepicker.getMonth()+"-"+datepicker.getDayOfMonth()+"";
        }
    }

    private class MyOnTimeChangedListener implements TimePicker.OnTimeChangedListener{


        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            daytext=timepicker.getCurrentHour()+":"+timepicker.getCurrentMinute()+":0";
        }
    }

}
