
package com.jkva.android.attendanceapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.CreatePersonResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class PersonActivity extends AppCompatActivity {
    class AddPersonTask extends AsyncTask<String, String, String> {
        boolean mAddFace;

        AddPersonTask (boolean addFace) {
            mAddFace = addFace;
        }

        @Override
        protected String doInBackground(String... params) {
            FaceServiceClient faceServiceClient = AttendanceApp.getFaceServiceClient();
            try{
                CreatePersonResult createPersonResult = faceServiceClient.createPerson(
                        params[0],
                        getString(R.string.user_provided_person_name),
                        getString(R.string.user_provided_description_data));

                return createPersonResult.personId.toString();
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
                addLog("Response: Success. Person " + result + " created.");
                personId = result;
                setInfo("Successfully Synchronized!");

                if (mAddFace) {
                    addFace();
                } else {
                    doneAndSave();
                }
            }
        }
    }

    class DeleteFaceTask extends AsyncTask<String, String, String> {
        String mPersonGroupId;
        UUID mPersonId;

        DeleteFaceTask(String personGroupId, String personId) {
            mPersonGroupId = personGroupId;
            mPersonId = UUID.fromString(personId);
        }

        @Override
        protected String doInBackground(String... params) {
            FaceServiceClient faceServiceClient = AttendanceApp.getFaceServiceClient();
            try{
                publishProgress("Deleting selected faces...");
                addLog("Request: Deleting face " + params[0]);

                UUID faceId = UUID.fromString(params[0]);
                faceServiceClient.deletePersonFace(personGroupId, mPersonId, faceId);
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
                setInfo("Face " + result + " successfully deleted");
                addLog("Response: Success. Deleting face " + result + " succeed");
            }
        }
    }

    private void setUiDuringBackgroundTask(String progress) {
        setInfo(progress);
    }

    boolean addNewPerson;
    String personId;
    String personGroupId;
    String oldPersonName;

    private static final int REQUEST_SELECT_IMAGE = 0;

    FaceGridViewAdapter faceGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            addNewPerson = bundle.getBoolean("AddNewPerson");
            personGroupId = bundle.getString("PersonGroupId");
            oldPersonName = bundle.getString("PersonName");
            if (!addNewPerson) {
                personId = bundle.getString("PersonId");
            }
        }

        GridView gridview = (GridView) findViewById(R.id.gridView_faces);
        gridview.setAdapter(new FaceGridViewAdapter());

        EditText editTextPersonName = (EditText) findViewById(R.id.edit_person_name);
        editTextPersonName.setText(oldPersonName);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("AddNewPerson", addNewPerson);
        outState.putString("PersonId", personId);
        outState.putString("PersonGroupId", personGroupId);
        outState.putString("OldPersonName", oldPersonName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GridView gridview = (GridView) findViewById(R.id.gridView_faces);
        gridview.setAdapter(new FaceGridViewAdapter());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        addNewPerson = savedInstanceState.getBoolean("AddNewPerson");
        personId = savedInstanceState.getString("PersonId");
        personGroupId = savedInstanceState.getString("PersonGroupId");
        oldPersonName = savedInstanceState.getString("OldPersonName");
    }

    public void doneAndSave(View view) {
        if (personId == null) {
            new AddPersonTask(false).execute(personGroupId);
        } else {
            doneAndSave();
        }
    }

    public void addFace(View view) {
        if (personId == null) {
            new AddPersonTask(true).execute(personGroupId);
        } else {
            addFace();
        }
    }

    private void doneAndSave() {
        TextView textWarning = (TextView)findViewById(R.id.info);
        EditText editTextPersonName = (EditText)findViewById(R.id.edit_person_name);
        String newPersonName = editTextPersonName.getText().toString();
        if (newPersonName.equals("")) {
            textWarning.setText("Person name cannot be empty");
            return;
        }

        StorageHelper.setPersonName(personId, newPersonName, personGroupId, PersonActivity.this);

        finish();
    }

    private void addFace() {
        setInfo("");
        Intent intent = new Intent(this, SelectImageActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("hello1",requestCode+" "+resultCode);
        switch (requestCode)
        {
            case REQUEST_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri uriImagePicked = data.getData();
                    Log.d("hello2",uriImagePicked+" ");
                    Intent intent = new Intent(this, AddFaceToPersonActivity.class);
                    intent.putExtra("PersonId", personId);
                    intent.putExtra("PersonGroupId", personGroupId);
                    intent.putExtra("ImageUriStr", uriImagePicked.toString());
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void deleteSelectedItems() {
        List<String> newFaceIdList = new ArrayList<>();
        List<Boolean> newFaceChecked = new ArrayList<>();
        List<String> faceIdsToDelete = new ArrayList<>();
        for (int i = 0; i < faceGridViewAdapter.faceChecked.size(); ++i) {
            boolean checked = faceGridViewAdapter.faceChecked.get(i);
            if (checked) {
                String faceId = faceGridViewAdapter.faceIdList.get(i);
                faceIdsToDelete.add(faceId);
                new DeleteFaceTask(personGroupId, personId).execute(faceId);
            } else {
                newFaceIdList.add(faceGridViewAdapter.faceIdList.get(i));
                newFaceChecked.add(false);
            }
        }

        StorageHelper.deleteFaces(faceIdsToDelete, personId, this);

        faceGridViewAdapter.faceIdList = newFaceIdList;
        faceGridViewAdapter.faceChecked = newFaceChecked;
        faceGridViewAdapter.notifyDataSetChanged();
    }

    // Add a log item.
    private void addLog(String log) {
        LogHelper.addIdentificationLog(log);
    }

    // Set the information panel on screen.
    private void setInfo(String info) {
        TextView textView = (TextView) findViewById(R.id.info);
        textView.setText(info);
    }

    private class FaceGridViewAdapter extends BaseAdapter {
        List<String> faceIdList;
        List<Boolean> faceChecked;
        boolean longPressed;


        FaceGridViewAdapter() {
            longPressed = false;
            faceIdList = new ArrayList<>();
            faceChecked = new ArrayList<>();

            Set<String> faceIdSet = StorageHelper.getAllFaceIds(personId, PersonActivity.this);
            for (String faceId: faceIdSet) {
                faceIdList.add(faceId);
                faceChecked.add(false);
            }
        }

        @Override
        public int getCount() {
            return faceIdList.size();
        }

        @Override
        public Object getItem(int position) {
            return faceIdList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // set the item view
           ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(parent.getContext());
                imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            Uri uri = Uri.parse(StorageHelper.getFaceUri(
                    faceIdList.get(position), PersonActivity.this));
            imageView.setImageURI(uri);

            return imageView;
        }
    }
}
