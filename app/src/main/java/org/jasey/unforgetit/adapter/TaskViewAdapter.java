package org.jasey.unforgetit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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

import java.util.Date;

public class TaskViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;

    public static TaskViewAdapter getInstance(Context context) {
        return new TaskViewAdapter(context);
    }

    private TaskViewAdapter(Context mContext) {
        this.mContext = mContext;
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
        taskViewHolder.bind(TaskViewFragment.buildTaskArray()[position]);
    }

    @Override
    public int getItemCount() {
        return TaskViewFragment.buildTaskArray().length;
    }

    /*Класс TaskViewHolder держит на готове ссылки на элементы виджетов CardView, которые он может наполнить данными из объекта Task в методе bind.
    Этот класс используется только адаптером. Адаптер поручает ему работу по заполнению виджетов*/
    private class TaskViewHolder extends RecyclerView.ViewHolder {
        private CardView mCV;
        private TextView mTitleView;
        private TextView mDateView;
        private Task mTask;

        public TaskViewHolder(View v) {
            super(v);
            mCV = (CardView) v.findViewById(R.id.card_view);
            mTitleView = (TextView) v.findViewById(R.id.title);
            mDateView = (TextView) v.findViewById(R.id.date);
        }

        public void bind(Task task) {
            mTask = task;
            mTitleView.setText(mTask.getTitle());
            mDateView.setText(TaskUtil.formatDate(mTask.getDate()));

            switch (mTask.getPriorityLevel()) {
                case (Task.PRIORITY_LOW):
                    mCV.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBlue));
                    break;
                case (Task.PRIORITY_NORMAL):
                    mCV.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorYellow));
                    break;
                case (Task.PRIORITY_HIGH):
                    mCV.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorOrange));
                    break;
            }

            if (mTask.getDate().compareTo(new Date()) <= 0) {
                mCV.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorRed));
            }

            if (mTask.isDone()) {
                mCV.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
            }
        }
    }
}
