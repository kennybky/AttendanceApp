package com.jkva.android.attendanceapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.CAMERA)) {
                }

                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        9982);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_person_groups);
        personGroupsListAdapter = new PersonGroupsListAdapter();
        recyclerView.setAdapter(personGroupsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addPersonGroup(View view){
        Intent intent = new Intent(this, AddPersonGroupActivity.class);
        startActivity(intent);
    }

    private class PersonGroupsListAdapter extends RecyclerView.Adapter<PersonGroupsListAdapter.ItemHolder> {

        List<String> personGroupIdList;

        PersonGroupsListAdapter() {

            personGroupIdList = new ArrayList<>();
            Set<String> personGroupIds = StorageHelper.getAllPersonGroupIds(MainActivity.this);
            for (String personGroupId: personGroupIds) {
                personGroupIdList.add(personGroupId);
            }
        }
        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.item_main_activity, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.bind(holder, position);
        }

        @Override
        public int getItemCount() {
            return personGroupIdList.size();
        }

        public class ItemHolder extends RecyclerView.ViewHolder {
            TextView textView;
            String personGroupName;
            String personGroupId;
            View itemView;

            public ItemHolder(View itemView) {
                super(itemView);
                textView=(TextView) itemView.findViewById(R.id.text_person_group);
                this.itemView=itemView;
            }

            public void bind(ItemHolder holder, int pos) {


                personGroupName = StorageHelper.getPersonGroupName(
                        personGroupIdList.get(pos), MainActivity.this);

                int personNumberInGroup = StorageHelper.getAllPersonIds(
                        personGroupIdList.get(pos), MainActivity.this).size();

                personGroupId = personGroupIdList.get(pos);
                textView.setText(String.format("%s (Person count: %d)", personGroupName, personNumberInGroup));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(MainActivity.this, PersonGroupActivity.class);
                        intent.putExtra("AddNewPersonGroup", false);
                        intent.putExtra("PersonGroupName", personGroupName);
                        intent.putExtra("PersonGroupId", personGroupId);
                        startActivity(intent);
                    }
                });

            }
        }
    }



}
