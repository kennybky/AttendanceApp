package com.jkva.android.attendanceapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.microsoft.projectoxford.face.FaceServiceClient;

import java.util.UUID;

public class AddPersonGroupActivity extends AppCompatActivity {

    EditText groupName;
    String name;
    String personGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_person_group);
        groupName=(EditText) findViewById(R.id.add_person_group_name);
        personGroupId = UUID.randomUUID().toString();

    }

    public void addPersonGroup(View view) {
        if(groupName.getText().equals("")) return;
        else {
            name = groupName.getText().toString();
            new AddPersonGroupTask().execute(personGroupId);
        }
    }

    class AddPersonGroupTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            FaceServiceClient faceServiceClient = AttendanceApp.getFaceServiceClient();
            try{
                faceServiceClient.createPersonGroup(
                        params[0],
                        "Group Name",
                        "Group Description");

                return params[0];
            } catch (Exception e) {
                Log.d("AddPersonGroupActivity", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {

                if(result !=null) {
                    Log.d("AddPersonGroupActivity", "Response: Success. Person group " + result + " created");
                    StorageHelper.setPersonGroupName(personGroupId, name, AddPersonGroupActivity.this);
                    finish();
                } else{
                    throw new NullPointerException("Something happened. Dont ask me why. Probably has something to do with ID");
            }
        }
    }
}
