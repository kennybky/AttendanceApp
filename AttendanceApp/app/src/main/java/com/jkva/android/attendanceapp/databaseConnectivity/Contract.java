package com.jkva.android.attendanceapp.databaseConnectivity;

import android.provider.BaseColumns;

public class Contract {

    //class to store column names in variables
    public static class TABLE_ATTENDANCE implements BaseColumns{
        public static final String TABLE_NAME = "StudentAttendance";

        public static final String COLUMN_NAME_STUDENT_NAME = "StudentName";
        public static final String COLUMN_NAME_CLASS_NAME = "ClassName";
        public static final String COLUMN_NAME_DATE = "Date";
    }
}
