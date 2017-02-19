package org.jasey.unforgetit.repository;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.jasey.unforgetit.entity.Task;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TaskRepositoryImplTest extends TaskRepositoryTest {

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        DataBaseHelper
                .getInstance(context)
                .getWritableDatabase()
                .delete(Task.TABLE_NAME, null, null);

        repository = TaskRepositoryImpl.getInstance(context);
    }
}