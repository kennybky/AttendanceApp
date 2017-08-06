package com.jkva.android.attendanceapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.jkva.android.attendanceapp.HistoryActivity;
import com.jkva.android.attendanceapp.R;
import com.jkva.android.attendanceapp.databaseConnectivity.Contract;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private DatePicker dp;
    private Button add;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);
        dp = (DatePicker) view.findViewById(R.id.datePicker);
        add = (Button) view.findViewById(R.id.add);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dp.updateDate(year, month, day);
        final HistoryActivity historyActivity=(HistoryActivity)getActivity();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyActivity.dateSelected(String.format("%02d-%02d-%04d",dp.getMonth()+1,dp.getDayOfMonth(),dp.getYear()));
                DatePickerFragment.this.dismiss();
            }
        });

        return view;
    }

}
