package com.jkva.android.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
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
                groupId = bundle.getString("personGroupId");
                groupName = bundle.getString("personGroupName");
        }
    }
    //Adds person
    public void addPerson(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddPersonFragment newFragment = new AddPersonFragment();
        newFragment.show(fm, "tag");
    }

    public void takeAttendance(View view) {

    }

    public void viewHistory(View view) {
    }
}
