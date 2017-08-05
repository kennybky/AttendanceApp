package com.jkva.android.attendanceapp.databaseConnectivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.jkva.android.attendanceapp.databaseConnectivity.Contract.TABLE_ATTENDANCE.*;
import static com.jkva.android.attendanceapp.databaseConnectivity.Contract.TABLE_ATTENDANCE.COLUMN_NAME_STUDENT_NAME;
import static com.jkva.android.attendanceapp.databaseConnectivity.Contract.TABLE_ATTENDANCE.TABLE_NAME;

// created to add or delete data from/to the database
public class DatabaseUtils {

    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME_DATE + " DESC"
        );
        return cursor;
    }

    public static void bulkInsert(SQLiteDatabase db, ArrayList<StudentDetails> studentDetails) {

        db.beginTransaction();
        try {
            for (StudentDetails n : studentDetails) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME_STUDENT_NAME, n.getStudentName());
                cv.put(COLUMN_NAME_CLASS_NAME, n.getClassName());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    //deletes whole table
    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

}
