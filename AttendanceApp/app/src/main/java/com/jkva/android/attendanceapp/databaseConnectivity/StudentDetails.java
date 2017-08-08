package com.jkva.android.attendanceapp.databaseConnectivity;

import java.util.Date;

/**
 * Created by Ammar on 6/29/2017.
 */

// Model class to store all data
public class StudentDetails {
    String studentName, className;
    Date date;

    public StudentDetails(String studentName, String className, Date date) {
        this.studentName = studentName;
        this.className = className;
        this.date = date;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
