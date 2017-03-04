package org.jasey.unforgetit.adapter;

import android.content.Context;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import org.jasey.unforgetit.R;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.repository.TaskRepository;

import java.util.Date;
import java.util.List;

class TimeIsOutTaskViewAdapter extends TaskViewAdapter {

    protected TimeIsOutTaskViewAdapter(Context context) {
        super(context);
    }

    @Override
    protected List<Task> findTasks() {
            return FluentIterable.from(TaskRepository.getInstance(TaskRepository.Type.JPA, context).getAll())
                    .filter(new Predicate<Task>() {
                        @Override
                        public boolean apply(Task input) {
                            return input.getDate().compareTo(new Date()) <= 0 && !input.isDone();
                        }
                    })
                    .toSortedList(Task.TASK_COMPARATOR);
    }

    @Override
    protected void bindHolderAndTask(TaskViewHolder taskViewHolder, Task task) {
        super.bindHolderAndTask(taskViewHolder, task);
            taskViewHolder.imageView.setImageResource(R.mipmap.ic_overdeu);
    }
}
