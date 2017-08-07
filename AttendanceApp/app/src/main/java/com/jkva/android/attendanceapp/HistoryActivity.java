package com.jkva.android.attendanceapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jkva.android.attendanceapp.adapters.HistoryAdapter;
import com.jkva.android.attendanceapp.databaseConnectivity.Contract;
import com.jkva.android.attendanceapp.databaseConnectivity.DBHelper;
import com.jkva.android.attendanceapp.databaseConnectivity.DatabaseUtils;
import com.jkva.android.attendanceapp.fragments.DatePickerFragment;

import java.text.SimpleDateFormat;

public class HistoryActivity extends AppCompatActivity{

    SQLiteDatabase db;
    RecyclerView rv;
    Button button;
    HistoryAdapter historyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        button = (Button) findViewById(R.id.button);
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        db=new DBHelper(this).getReadableDatabase();
        historyAdapter=new HistoryAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public void selectDate(View view){
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment dpf=new DatePickerFragment();
        dpf.show(fm,"datePickerFragment");
    }
    public void dateSelected(String date){
        button.setText(date);
        Cursor cursor = DatabaseUtils.getAll(db, Contract.TABLE_ATTENDANCE.COLUMN_NAME_DATE+"='"+date+"'");
        historyAdapter.swapCursor(cursor);
        rv.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
    }
}
