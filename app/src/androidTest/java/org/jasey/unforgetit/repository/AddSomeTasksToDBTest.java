package org.jasey.unforgetit.repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.j256.ormlite.table.TableUtils;

import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.utils.TaskUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.Date;

@RunWith(AndroidJUnit4.class)

public class AddSomeTasksToDBTest {
    private TaskRepositoryJPAImpl repository;

    @Before
    public void setUp() throws SQLException {
        Context context = InstrumentationRegistry.getTargetContext();
        TableUtils.clearTable(DataBaseJPAHelper
                .getInstance(context)
                .getConnectionSource(), Task.class);
        repository = (TaskRepositoryJPAImpl) TaskRepositoryJPAImpl.getInstance(context);
    }

    @NonNull
    private Task getTask() {
        Task task = new Task();
        task.setDate(new Date());
        task.setTitle("test_title");
        task.setPriorityLevel(Task.PRIORITY_LOW);
        task.setDone(false);
        return task;
    }

    @Test
    public void add_one_task_test() {

        Task low_task = getTask();
        low_task.setDate(TaskUtil.getDateOnFebruary2017(28, 22, 0));
        low_task.setPriorityLevel(Task.PRIORITY_LOW);
        low_task.setTitle("Сходить в магазин.");
        repository.addNew(low_task);

        Task normal_task = getTask();
        normal_task.setDate(TaskUtil.getDateOnFebruary2017(28, 22, 0));
        normal_task.setPriorityLevel(Task.PRIORITY_NORMAL);
        normal_task.setTitle("Полить помидорки.");
        repository.addNew(normal_task);

        Task hight_task = getTask();
        hight_task.setDate(TaskUtil.getDateOnFebruary2017(28, 22, 0));
        hight_task.setPriorityLevel(Task.PRIORITY_HIGH);
        hight_task.setTitle("Покормить кота!");
        repository.addNew(hight_task);

        Task refused_task = getTask();
        refused_task.setDate(TaskUtil.getDateOnFebruary2017(28, 22, 0));
        refused_task.setTitle("Позвонить тёще.");
        repository.addNew(refused_task);
        refused_task.setDate(TaskUtil.getDateOnFebruary2017(16, 9, 0));
        refused_task.setTitle("Позвонить тёще.");
        repository.addNew(refused_task);
        refused_task.setDate(TaskUtil.getDateOnFebruary2017(17, 9, 0));
        refused_task.setTitle("Позвонить тёще.");
        repository.addNew(refused_task);

        Task done_task = getTask();
        done_task.setDate(TaskUtil.getDateOnFebruary2017(28, 22, 0));
        done_task.setTitle("Посадить дерево.");
        done_task.setDone(true);
        repository.addNew(done_task);
        done_task.setDate(TaskUtil.getDateOnFebruary2017(28, 22, 0));
        done_task.setTitle("Построить дом.");
        done_task.setDone(true);
        repository.addNew(done_task);

        Assert.assertTrue(repository.getAll().size() == 5);
    }
}
