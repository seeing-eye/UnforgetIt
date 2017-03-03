package org.jasey.unforgetit.repository;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.j256.ormlite.table.TableUtils;

import org.jasey.unforgetit.entity.Task;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;

@RunWith(AndroidJUnit4.class)
public class DropTableTest {
    private TaskRepositoryJPAImpl repository;

    @Test
    public void drop() throws SQLException{
        Context context = InstrumentationRegistry.getTargetContext();
        TableUtils.dropTable(DataBaseJPAHelper
                .getInstance(context)
                .getConnectionSource(), Task.class, true);
    }
}
