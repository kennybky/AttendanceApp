package com.jkva.android.attendanceapp;

import android.app.Application;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

public class AttendanceApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        sFaceServiceClient = new FaceServiceRestClient(getString(R.string.endpoint), getString(R.string.subscription_key));
    }

    public static FaceServiceClient getFaceServiceClient() {
        return sFaceServiceClient;
    }

    private static FaceServiceClient sFaceServiceClient;
}
