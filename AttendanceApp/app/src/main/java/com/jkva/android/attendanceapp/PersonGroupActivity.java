package com.jkva.android.attendanceapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

}
