package org.jasey.unforgetit.repository;

import android.support.annotation.NonNull;

import org.jasey.unforgetit.entity.Task;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public abstract class TaskRepositoryTest {

    protected TaskRepository repository;

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
    public void add_test() {
        Task task = getTask();
        Assert.assertTrue(repository.addNew(task));
        Assert.assertNotNull(task.getId());
        repository.remove(task);
    }

    @Test
    public void twice_add_one_task_test() {
        Task task = getTask();
        Assert.assertTrue(repository.addNew(task));
        Assert.assertNotNull(task.getId());
        Assert.assertFalse(repository.addNew(task));
        repository.remove(task);
    }

    @Test
    public void add_task_with_null_title_test() {
        Task task = getTask();
        task.setTitle(null);
        Assert.assertFalse(repository.addNew(task));
        repository.remove(task);
    }

    @Test
    public void add_task_with_null_date_test() {
        Task task = getTask();
        task.setDate(null);
        Assert.assertFalse(repository.addNew(task));
        repository.remove(task);
    }

    @Test
    public void remove_test() {
        Task task = getTask();
        repository.addNew(task);
        Assert.assertTrue(repository.remove(task));
    }

    @Test
    public void twice_remove_one_task_test() {
        Task task = getTask();
        repository.addNew(task);
        Assert.assertTrue(repository.remove(task));
        Assert.assertFalse(repository.remove(task));
    }

    @Test
    public void update_test() {
        Task task = getTask();
        repository.addNew(task);
        task.setDate(new Date());
        task.setTitle("new_test_title");
        task.setPriorityLevel(Task.PRIORITY_HIGH);
        task.setDone(true);
        Assert.assertTrue(repository.update(task));
        repository.remove(task);
    }

    @Test
    public void update_title_test() {
        Task task = getTask();
        repository.addNew(task);
        task.setTitle("new_test_title");
        repository.update(task);
        Assert.assertTrue(repository.getAll().contains(task));
        repository.remove(task);
    }

    @Test
    public void get_all_test() {
        Task task = getTask();
        repository.addNew(task);
        List<Task> tasks = repository.getAll();
        Assert.assertTrue(tasks.size() == 1);
        Assert.assertTrue(tasks.get(0).equals(task));
        repository.remove(task);
    }


}