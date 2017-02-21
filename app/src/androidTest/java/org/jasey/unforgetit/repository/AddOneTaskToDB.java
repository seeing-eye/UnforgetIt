package org.jasey.unforgetit.repository;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.j256.ormlite.table.TableUtils;

import org.jasey.unforgetit.entity.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.Date;

@RunWith(AndroidJUnit4.class)

public class AddOneTaskToDB {
    private TaskRepositoryJPAImpl repository;

    @Before
    public void setUp() throws SQLException {
        Context context = InstrumentationRegistry.getTargetContext();
        TableUtils.clearTable(DataBaseJPAHelper
                .getInstance(context)
                .getConnectionSource(), Task.class);
        repository = (TaskRepositoryJPAImpl) TaskRepositoryJPAImpl.getInstance(context);
    }

    @Test
    public void add_one_task_test() {
        Task task = new Task();
        task.setDate(new Date());
        task.setTitle("test_title");
        task.setPriorityLevel(Task.PRIORITY_LOW);
        task.setDone(false);
        repository.addNew(task);
        Assert.assertTrue(repository.getAll().size() == 1);
    }
}
