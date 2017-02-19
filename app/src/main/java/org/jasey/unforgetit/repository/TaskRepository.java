package org.jasey.unforgetit.repository;

import android.content.Context;

import org.jasey.unforgetit.entity.Task;

import java.util.List;

public abstract class TaskRepository {
    static String DATABASE_NAME = "unforget_it_db";

    static String ADD = "addNew";
    static String REMOVE = "remove";
    static String UPDATE = "update";
    static String GET_ALL = "getAll";
    static String ERROR_MESSAGE = " failed";
    static String STOPWATCH = " took %d ms.";

    public static TaskRepository getInstance(Type type, Context context) {
        switch (type) {
            case DEFAULT:
                return TaskRepositoryImpl.getInstance(context);
            case JPA:
                return TaskRepositoryJPAImpl.getInstance(context);
            default:
                return null;
        }
    }

    public abstract boolean addNew(Task task);

    public abstract boolean remove(Task task);

    public abstract boolean update(Task task);

    public abstract List<Task> getAll();

    public enum Type {
        DEFAULT,
        JPA
    }
}
