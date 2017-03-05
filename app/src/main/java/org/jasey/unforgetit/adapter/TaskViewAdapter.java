package org.jasey.unforgetit.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jasey.unforgetit.R;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.entity.TaskType;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class TaskViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ID_EDIT = 101;
    private static final int ID_DELETE = 102;
    protected static final AtomicBoolean IS_BUSY = new AtomicBoolean(false);

    private ContextMenuItemClickListener mContextMenuItemClickListener;

    public interface ContextMenuItemClickListener {
        void onDeleteActionClick(Task task);
        void onEditItemClick(Task task);
    }

    public static TaskViewAdapter getInstance(Context context, TaskType type) {
        switch (type) {
            case ACTIVE_TASK:
                return new ActiveTaskViewAdapter(context);
            case TIME_IS_OUT_TASK:
                return new TimeIsOutTaskViewAdapter(context);
            case DONE_TASK:
                return new DoneTaskViewAdapter(context);
            default:
                return null;
        }
    }

    protected Context context;

    protected TaskViewAdapter(Context context) {
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
        taskViewHolder.cv.setLongClickable(true);
        bindHolderAndTask(taskViewHolder, findTasks().get(position));
    }

    @Override
    public int getItemCount() {
        return findTasks().size();
    }

    protected abstract List<Task> findTasks();

    protected void bindHolderAndTask(final TaskViewHolder taskViewHolder, final Task task) {
        taskViewHolder.titleView.setText(task.getTitle());
        taskViewHolder.dateView.setText(DateFormatUtils.format(task.getDate(), Task.TIME_FORMAT + Task.DELIMITER + Task.DATE_FORMAT));
    }

    /*Класс TaskViewHolder держит на готове ссылки на элементы виджетов CardView, которые он может наполнить данными из объекта Task в методе bindHolderAndTask.
        Этот класс используется только адаптером. Адаптер поручает ему работу по заполнению виджетов*/
    protected class TaskViewHolder extends RecyclerView.ViewHolder
            implements
            View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {
        CardView cv;
        CircleImageView imageView;
        TextView titleView;
        TextView dateView;

        TaskViewHolder(View v) {
            super(v);
            v.setOnCreateContextMenuListener(this);
            cv = (CardView) v.findViewById(R.id.card_view);
            imageView = (CircleImageView) v.findViewById(R.id.image_circle);
            titleView = (TextView) v.findViewById(R.id.title);
            dateView = (TextView) v.findViewById(R.id.date);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            mContextMenuItemClickListener = (ContextMenuItemClickListener) context;
            switch (item.getItemId()) {
                case ID_DELETE:
                    mContextMenuItemClickListener.onDeleteActionClick(findTasks().get(getLayoutPosition()));
                    return true;
                case ID_EDIT:
                    mContextMenuItemClickListener.onEditItemClick(findTasks().get(getLayoutPosition()));
                    return true;
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.context_menu_title);
            MenuItem editMenuItem = menu.add(0, ID_EDIT, 0, R.string.edit);
            MenuItem deleteMenuItem = menu.add(0, ID_DELETE, 0, R.string.delete);

            editMenuItem.setOnMenuItemClickListener(this);
            deleteMenuItem.setOnMenuItemClickListener(this);
        }
    }
}
