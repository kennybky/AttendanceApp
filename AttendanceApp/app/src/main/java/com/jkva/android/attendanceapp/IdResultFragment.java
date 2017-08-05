package com.jkva.android.attendanceapp;

import android.app.ListFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.microsoft.projectoxford.face.contract.IdentifyResult;

import java.util.List;

/**
 * Created by kenny on 7/31/2017.
 */

public class IdResultFragment extends ListFragment {


    List<IdentifyResult> mIdentifyResults;

    List<Bitmap> faceThumbnails;
    public IdResultFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {

        return null;
    }
}
