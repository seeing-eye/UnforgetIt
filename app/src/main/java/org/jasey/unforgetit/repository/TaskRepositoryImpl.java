package org.jasey.unforgetit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import org.jasey.unforgetit.entity.Task;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

public class TaskRepositoryImpl extends TaskRepository {
    static TaskRepositoryImpl getInstance(Context context) {
        return new TaskRepositoryImpl(context);
    }

    private DataBaseHelper helper;

    private TaskRepositoryImpl(Context context) {
        helper = DataBaseHelper.getInstance(context);
    }

    @Override
    public boolean addNew(Task task) {
        long stopwatch = System.currentTimeMillis();
        try {
            if (task.getId() != null) {
                return false;
            }

            ContentValues values = fillContentValues(task);

            long id = helper
                    .getWritableDatabase()
                    .insert(Task.TABLE_NAME, null, values);
            if (id != -1) {
                Field[] fields = task.getClass().getDeclaredFields();
                for (Field f : fields) {
                    if (f.isAnnotationPresent(Id.class)) {
                        f.setAccessible(true);
                        f.set(task, id);
                        break;
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), ADD + ERROR_MESSAGE, e);
            return false;
        } finally {
            Log.d(this.getClass().getName(), String.format(ADD + STOPWATCH, System.currentTimeMillis() - stopwatch));
        }
    }

    @Override
    public boolean remove(Task task) {
        long stopwatch = System.currentTimeMillis();
        try {
            return helper
                    .getWritableDatabase()
                    .delete(Task.TABLE_NAME, Task.ID + " = ? ", new String[]{String.valueOf(task.getId())}) != 0;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), REMOVE + ERROR_MESSAGE, e);
            return false;
        } finally {
            Log.d(this.getClass().getName(), String.format(REMOVE + STOPWATCH, System.currentTimeMillis() - stopwatch));
        }
    }

    @Override
    public boolean update(Task task) {
        long stopwatch = System.currentTimeMillis();
        try {
            ContentValues values = fillContentValues(task);

            return helper
                    .getWritableDatabase()
                    .update(Task.TABLE_NAME, values, null, null) != -1;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), UPDATE + ERROR_MESSAGE, e);
            return false;
        } finally {
            Log.d(this.getClass().getName(), String.format(UPDATE + STOPWATCH, System.currentTimeMillis() - stopwatch));
        }
    }

    @NonNull
    private ContentValues fillContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(Task.TASK_TITLE_COLUMN, task.getTitle());
        values.put(Task.TASK_DATE_COLUMN, task.getDate().getTime());
        values.put(Task.TASK_PRIORITY_COLUMN, task.getPriorityLevel());
        values.put(Task.TASK_DONE_COLUMN, task.isDone());
        values.put(Task.TASK_ALARM_ADVANCE_TIME_COLUMN, task.getAlarmAdvanceTime());
        return values;
    }

    @Override
    public List<Task> getAll() {

        List<Task> tasks = new ArrayList<>();

        long stopwatch = System.currentTimeMillis();
        try {
            Cursor cursor = helper
                    .getReadableDatabase()
                    .query(Task.TABLE_NAME, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Long id = cursor.getLong(cursor.getColumnIndex(Task.ID));
                    String title = cursor.getString(cursor.getColumnIndex(Task.TASK_TITLE_COLUMN));
                    Date date = new Date(cursor.getLong(cursor.getColumnIndex(Task.TASK_DATE_COLUMN)));
                    int priority = cursor.getInt(cursor.getColumnIndex(Task.TASK_PRIORITY_COLUMN));
                    boolean done = cursor.getInt(cursor.getColumnIndex(Task.TASK_DONE_COLUMN)) == 1;
                    int alarmAdvanceTime = cursor.getInt(cursor.getColumnIndex(Task.TASK_ALARM_ADVANCE_TIME_COLUMN));

                    tasks.add(Task.buildTask(id, title, date, priority, done, alarmAdvanceTime));
                } while (cursor.moveToNext());
            }
            cursor.close();

            return tasks;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), GET_ALL + ERROR_MESSAGE, e);
            return Collections.emptyList();
        } finally {
            Log.d(this.getClass().getName(), String.format(GET_ALL + STOPWATCH, System.currentTimeMillis() - stopwatch));
        }
    }
}
