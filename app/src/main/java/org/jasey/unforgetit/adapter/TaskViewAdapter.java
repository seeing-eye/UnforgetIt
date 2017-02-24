package org.jasey.unforgetit.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jasey.unforgetit.R;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.fragment.TaskViewFragment;
import org.jasey.unforgetit.utils.TaskUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class TaskViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    public static TaskViewAdapter getInstance(Context context, TaskViewFragment.TaskViewType type) {
        switch (type) {
            case ACTIVE_TASK_VIEW: 
                return new ActiveTaskViewAdapter(context);
            case TIME_IS_OUT_VIEW:
                return new TimeIsOutTaskViewAdapter(context);
            default:
                return null;
        }
    }

    TaskViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
        bindHolderAndTask(taskViewHolder, findTasks().get(position));
    }

    @Override
    public int getItemCount() {
        return findTasks().size();
    }

    abstract List<Task> findTasks();
    
    void bindHolderAndTask(TaskViewHolder taskViewHolder, Task task) {
        taskViewHolder.task = task;
        taskViewHolder.titleView.setText(taskViewHolder.task.getTitle());
        taskViewHolder.dateView.setText(TaskUtil.formatDate(taskViewHolder.task.getDate()));
    }
        
    /*Класс TaskViewHolder держит на готове ссылки на элементы виджетов CardView, которые он может наполнить данными из объекта Task в методе bindHolderAndTask.
    Этот класс используется только адаптером. Адаптер поручает ему работу по заполнению виджетов*/
    class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        CircleImageView imageView;
        TextView titleView;
        TextView dateView;
        Task task;

        TaskViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.card_view);
            imageView = (CircleImageView) v.findViewById(R.id.image_circle);
            titleView = (TextView) v.findViewById(R.id.title);
            dateView = (TextView) v.findViewById(R.id.date);
        }
    }
}
