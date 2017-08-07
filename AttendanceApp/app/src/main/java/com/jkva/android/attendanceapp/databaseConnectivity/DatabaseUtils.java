package com.jkva.android.attendanceapp.databaseConnectivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.jkva.android.attendanceapp.databaseConnectivity.Contract.TABLE_ATTENDANCE.*;
import static com.jkva.android.attendanceapp.databaseConnectivity.Contract.TABLE_ATTENDANCE.COLUMN_NAME_STUDENT_NAME;
import static com.jkva.android.attendanceapp.databaseConnectivity.Contract.TABLE_ATTENDANCE.TABLE_NAME;

// created to add or delete data from/to the database
public class DatabaseUtils {

    public static Cursor getAll(SQLiteDatabase db, String where) {
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                where,
                null,
                null,
                null,
                COLUMN_NAME_DATE + " DESC"
        );
        return cursor;
    }

    public static void insert(SQLiteDatabase db, String className, String studentName){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_STUDENT_NAME, studentName);
        cv.put(COLUMN_NAME_CLASS_NAME, className);
        cv.put(COLUMN_NAME_DATE, new SimpleDateFormat("MM-dd-yyyy").format(new Date()));
        db.insert(TABLE_NAME, null, cv);
    }

    //deletes whole table
    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

}
