
package com.jkva.android.attendanceapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class PersonListActivity extends AppCompatActivity {

    private String personGroupName;
    private RecyclerView mRecyclerView;

    class TrainPersonGroupTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            addLog("Request: Training group " + params[0]);

            // Get an instance of face service client.
            FaceServiceClient faceServiceClient = AttendanceApp.getFaceServiceClient();
            try{
                publishProgress("Training person group...");

                faceServiceClient.trainPersonGroup(params[0]);
                return params[0];
            } catch (Exception e) {
                publishProgress(e.getMessage());
                addLog(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            setUiDuringBackgroundTask(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
                addLog("Response: Success. Group " + result + " training completed");

                finish();
            }
        }
    }

    class DeletePersonTask extends AsyncTask<String, String, String> {
        String mPersonGroupId;
        DeletePersonTask(String personGroupId) {
            mPersonGroupId = personGroupId;
        }
        @Override
        protected String doInBackground(String... params) {
            // Get an instance of face service client.
            FaceServiceClient faceServiceClient = AttendanceApp.getFaceServiceClient();
            try{
                publishProgress("Deleting selected persons...");
                addLog("Request: Deleting person " + params[0]);

                UUID personId = UUID.fromString(params[0]);
                faceServiceClient.deletePerson(mPersonGroupId, personId);
                return params[0];
            } catch (Exception e) {
                publishProgress(e.getMessage());
                addLog(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            setUiDuringBackgroundTask(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
                setInfo("Person " + result + " successfully deleted");
                addLog("Response: Success. Deleting person " + result + " succeed");
            }
        }
    }

    // Show the status of background detection task on screen.
    private void setUiDuringBackgroundTask(String progress) {

        setInfo(progress);
    }

    public void addPerson(View view) {
            addPerson();
    }

    private void addPerson() {
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra("AddNewPerson", true);
        intent.putExtra("PersonName", "");
        intent.putExtra("PersonGroupId", personGroupId);
        startActivity(intent);
    }

    String personGroupId;

    PersonGridViewAdapter personGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_group);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            personGroupId = bundle.getString("PersonGroupId");
            personGroupName = bundle.getString("PersonGroupName");
        }

        //initializeGridView();

        mRecyclerView = (RecyclerView) findViewById(R.id.gridView_persons);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        personGridViewAdapter = new PersonGridViewAdapter();
        mRecyclerView.setAdapter(personGridViewAdapter);


        EditText editTextPersonGroupName = (EditText)findViewById(R.id.edit_person_group_name);
        editTextPersonGroupName.setText(personGroupName);

    }



    @Override
    protected void onResume() {
        super.onResume();

            mRecyclerView = (RecyclerView) findViewById(R.id.gridView_persons);
            personGridViewAdapter = new PersonGridViewAdapter();
            mRecyclerView.setAdapter(personGridViewAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("PersonGroupId", personGroupId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        personGroupId = savedInstanceState.getString("PersonGroupId");
    }

    public void doneAndSave(View view) {
            doneAndSave(true);
    }

    private void doneAndSave(boolean trainPersonGroup) {
        EditText editTextPersonGroupName = (EditText)findViewById(R.id.edit_person_group_name);
        String newPersonGroupName = editTextPersonGroupName.getText().toString();
        if (newPersonGroupName.equals("")) {
            setInfo("Person group name could not be empty");
            return;
        }

        StorageHelper.setPersonGroupName(personGroupId, newPersonGroupName, PersonListActivity.this);

        if (trainPersonGroup) {
            new TrainPersonGroupTask().execute(personGroupId);
        } else {
            finish();
        }
    }

    private void deleteItem(String personId) {

        List<String> personIdsToDelete = new ArrayList<>();

                personIdsToDelete.add(personId);
                new DeletePersonTask(personGroupId).execute(personId);


        StorageHelper.deletePersons(personIdsToDelete, personGroupId, this);

        personGridViewAdapter.personIdList.remove(personId);
        personGridViewAdapter.notifyDataSetChanged();
    }

    // Add a log item.
    private void addLog(String log) {
        LogHelper.addIdentificationLog(log);
    }

    // Set the information panel on screen.
    private void setInfo(String info) {
       Toast toast = Toast.makeText(PersonListActivity.this, info, Toast.LENGTH_LONG);
        toast.show();
    }

    private class PersonGridViewAdapter extends RecyclerView.Adapter<PersonGridViewAdapter.PersonViewHolder> {

        List<String> personIdList;


        PersonGridViewAdapter() {
            personIdList = new ArrayList<>();

            Set<String> personIdSet = StorageHelper.getAllPersonIds(personGroupId, PersonListActivity.this);
            for (String personId: personIdSet) {
                personIdList.add(personId);
            }
        }

        @Override
        public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutIdForListItem = R.layout.item_person;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            boolean shouldAttachToParentImmediately = false;
            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            PersonViewHolder viewHolder = new PersonViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.bind(position);
        }


        public Object getItem(int position) {
            return personIdList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return personIdList.size();
        }


        class PersonViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
            View convertView;
            public PersonViewHolder(View itemView) {
                super(itemView);
                convertView = itemView;
                convertView.setOnClickListener(this);
            }

            public void bind(final int position) {
                String personId = personIdList.get(position);
                Set<String> faceIdSet = StorageHelper.getAllFaceIds(personId, PersonListActivity.this);
                if (!faceIdSet.isEmpty()) {
                    Iterator<String> it = faceIdSet.iterator();
                    Uri uri = Uri.parse(StorageHelper.getFaceUri(it.next(), PersonListActivity.this));
                    ((ImageView)convertView.findViewById(R.id.image_person)).setImageURI(uri);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.select_image);
                    ((ImageView)convertView.findViewById(R.id.image_person)).setImageDrawable(drawable);
                }

                // set the text of the item
                String personName = StorageHelper.getPersonName(personId, personGroupId, PersonListActivity.this);
                ((TextView)convertView.findViewById(R.id.text_person)).setText(personName);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                            String personId = personGridViewAdapter.personIdList.get(position);
                            String personName = StorageHelper.getPersonName(
                                    personId, personGroupId, PersonListActivity.this);

                            Intent intent = new Intent(PersonListActivity.this, PersonActivity.class);
                            intent.putExtra("AddNewPerson", false);
                            intent.putExtra("PersonName", personName);
                            intent.putExtra("PersonId", personId);
                            intent.putExtra("PersonGroupId", personGroupId);
                            startActivity(intent);

            }
        }
    }
}
