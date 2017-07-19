package com.jkva.android.attendanceapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.microsoft.projectoxford.face.FaceServiceClient;


public class MainActivity extends AppCompatActivity {


    private PersonGroupsListAdapter personGroupsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeListView();
    }
    @Override
    protected void onResume() {
        super.onResume();

        ListView listView = (ListView) findViewById(R.id.list_person_groups);
        personGroupsListAdapter = new PersonGroupsListAdapter();
        listView.setAdapter(personGroupsListAdapter);
    }
    private void initializeListView() {
        ListView listView = (ListView) findViewById(R.id.list_person_groups);

        personGroupsListAdapter = new PersonGroupsListAdapter();
        listView.setAdapter(personGroupsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String personGroupId = personGroupsListAdapter.personGroupIdList.get(position);
                    String personGroupName = StorageHelper.getPersonGroupName(
                            personGroupId, MainActivity.this);

                    Intent intent = new Intent(MainActivity.this, PersonGroupActivity.class);
                    intent.putExtra("AddNewPersonGroup", false);
                    intent.putExtra("PersonGroupName", personGroupName);
                    intent.putExtra("PersonGroupId", personGroupId);
                    startActivity(intent);
                }

        });
    }


    public void addPersonGroup(View view){
        Intent intent = new Intent(this, AddPersonGroupActivity.class);
        startActivity(intent);
    }



    private class PersonGroupsListAdapter extends BaseAdapter {

        List<String> personGroupIdList;



        PersonGroupsListAdapter() {

            personGroupIdList = new ArrayList<>();


            Set<String> personGroupIds = StorageHelper.getAllPersonGroupIds(MainActivity.this);
            for (String personGroupId: personGroupIds) {
                personGroupIdList.add(personGroupId);

            }
        }

        @Override
        public int getCount() {
            return personGroupIdList.size();
        }

        @Override
        public Object getItem(int position) {
            return personGroupIdList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // set the item view
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.item_person_group, parent, false);
            }
            convertView.setId(position);

            // set the text of the item
            String personGroupName = StorageHelper.getPersonGroupName(
                    personGroupIdList.get(position), MainActivity.this);
            int personNumberInGroup = StorageHelper.getAllPersonIds(
                    personGroupIdList.get(position), MainActivity.this).size();
            ((TextView)convertView.findViewById(R.id.text_person_group)).setText(
                    String.format("%s (Person count: %d)", personGroupName, personNumberInGroup));

            return convertView;
        }
    }



}
