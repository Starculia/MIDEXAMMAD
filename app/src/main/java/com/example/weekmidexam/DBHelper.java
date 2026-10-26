package com.example.weekmidexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student_case2.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE = "students";
    public static final String COL_ID = "student_id";
    public static final String COL_NAME = "name";
    public static final String COL_ENROLLED = "enrolled";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + COL_ID + " TEXT PRIMARY KEY, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_ENROLLED + " INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // for add
    public boolean addStudent(String studentId, String name, boolean enrolled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, studentId);
        cv.put(COL_NAME, name);
        cv.put(COL_ENROLLED, enrolled ? 1 : 0);
        return db.insert(TABLE, null, cv) != -1;
    }

    // for edit
    public boolean updateStudent(String studentId, String name, boolean enrolled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_ENROLLED, enrolled ? 1 : 0);
        return db.update(TABLE, cv, COL_ID + "=?", new String[]{studentId}) > 0;
    }

    // for delete
    public boolean deleteStudent(String studentId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE, COL_ID + "=?", new String[]{studentId}) > 0;
    }

    // for view
    public Cursor getAllStudents() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE + " ORDER BY " + COL_NAME, null);
    }
}
