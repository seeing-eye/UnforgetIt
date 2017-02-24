package org.jasey.unforgetit.adapter;

import android.content.Context;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import org.jasey.unforgetit.R;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.repository.TaskRepository;

import java.util.Date;
import java.util.List;

class ActiveTaskViewAdapter extends TaskViewAdapter {

    protected ActiveTaskViewAdapter(Context context) {
        super(context);
    }

    @Override
    protected List<Task> findTasks() {
            return FluentIterable.from(TaskRepository.getInstance(TaskRepository.Type.JPA, context).getAll())
                    .filter(new Predicate<Task>() {
                        @Override
                        public boolean apply(Task input) {
                            return input.getDate().after(new Date());
                        }
                    })
                    .toSortedList(Task.TASK_COMPARATOR);
    }

    @Override
    protected void bindHolderAndTask(TaskViewAdapter.TaskViewHolder taskViewHolder, Task task) {
        super.bindHolderAndTask(taskViewHolder, task);

        switch (taskViewHolder.task.getPriorityLevel()) {
            case (Task.PRIORITY_LOW):
                taskViewHolder.imageView.setImageResource(R.drawable.low_priority);
                break;
            case (Task.PRIORITY_NORMAL):
                taskViewHolder.imageView.setImageResource(R.drawable.normal_priority);
                break;
            case (Task.PRIORITY_HIGH):
                taskViewHolder.imageView.setImageResource(R.drawable.hight_priority);
                break;
        }
    }
}



