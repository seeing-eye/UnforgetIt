package org.jasey.unforgetit.repository;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jasey.unforgetit.entity.Task;

class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_SCRIPT =
            "CREATE TABLE " + Task.TABLE_NAME +
                    " (" + Task.TASK_DATE_COLUMN + " BIGINT NOT NULL , " +
                    Task.TASK_DONE_COLUMN + " SMALLINT NOT NULL , " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    Task.TASK_PRIORITY_COLUMN + " INTEGER NOT NULL , " +
                    Task.TASK_TITLE_COLUMN + " VARCHAR NOT NULL" +
                    ");";

    private static DataBaseHelper helper;

    synchronized static DataBaseHelper getInstance(Context context) {
        return (helper = helper == null ? new DataBaseHelper(context) : helper);
    }

    private DataBaseHelper(Context context) {
        super(context, TaskRepository.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(this.getClass().getName(), "onCreate");
            db.execSQL(CREATE_TABLE_SCRIPT);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.d(this.getClass().getName(), "onUpgrade");
            db.execSQL("DROP TABLE" + Task.TABLE_NAME);
            onCreate(db);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }
}
