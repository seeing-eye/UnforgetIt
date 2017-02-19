package org.jasey.unforgetit.repository;

import android.content.Context;
import android.util.Log;

import org.jasey.unforgetit.entity.Task;

import java.util.Collections;
import java.util.List;

class TaskRepositoryJPAImpl extends TaskRepository {
    static TaskRepository getInstance(Context context) {
        return new TaskRepositoryJPAImpl(context);
    }

    private DataBaseJPAHelper helper;

    private TaskRepositoryJPAImpl(Context context) {
        helper = DataBaseJPAHelper.getInstance(context);
    }

    @Override
    public boolean addNew(Task task) {
        long stopwatch = System.currentTimeMillis();
        try {
            if (task.getId() != null) {
                return false;
            }
            return task.equals(helper.getTaskDAO().createIfNotExists(task));
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
            return helper.getTaskDAO().delete(task) == 1;
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
            return helper.getTaskDAO().update(task) == 1;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), UPDATE + ERROR_MESSAGE, e);
            return false;
        } finally {
            Log.d(this.getClass().getName(), String.format(UPDATE + STOPWATCH, System.currentTimeMillis() - stopwatch));
        }
    }


    @Override
    public List<Task> getAll() {
        long stopwatch = System.currentTimeMillis();
        try {
            return helper.getTaskDAO().queryForAll();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), GET_ALL + ERROR_MESSAGE, e);
            return Collections.emptyList();
        } finally {
            Log.d(this.getClass().getName(), String.format(GET_ALL + STOPWATCH, System.currentTimeMillis() - stopwatch));
        }
    }
}
