package com.jkva.android.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by kenny on 7/18/2017.
 */

public class PersonGroupActivity extends AppCompatActivity {
    String groupId;
    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persongroup);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
                groupId = bundle.getString("PersonGroupId");
                groupName = bundle.getString("PersonGroupName");
        }
    }

    //Add saved instance states
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("PersonGroupId", groupId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        groupId = savedInstanceState.getString("PersonGroupId");

    }

    public void takeAttendance(View view) {
        Intent intent=new Intent(this, IdentificationActivity.class);
        intent.putExtra("PersonGroupId", groupId);
        intent.putExtra("PersonGroupName", groupName);
        startActivity(intent);
    }

    public void viewHistory(View view) {
    }

    public void viewStudents(View view) {
        Intent intent=new Intent(this, PersonListActivity.class);
        intent.putExtra("PersonGroupId", groupId);
        intent.putExtra("PersonGroupName", groupName);
        startActivity(intent);
    }
}
