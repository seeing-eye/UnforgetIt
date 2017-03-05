package org.jasey.unforgetit.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import org.jasey.unforgetit.R;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.repository.TaskRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ActiveTaskViewAdapter extends TaskViewAdapter {
    private static final BlockingQueue<Task> ACTIVE_TASKS_QUEUE = new LinkedBlockingQueue<>();
    private static final AtomicInteger ACTIVE_TASK_COUNTER = new AtomicInteger(0);

    protected ActiveTaskViewAdapter(Context context) {
        super(context);
    }

    public interface ActiveTaskAnimationListener {
        void onActiveTaskImageClick(Task task);
        void updatePage();
    }

    @Override
    protected List<Task> findTasks() {
        return FluentIterable.from(TaskRepository.getInstance(TaskRepository.Type.JPA, context).getAll())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean apply(Task input) {
                        return input.getDate().after(new Date()) && !input.isDone();
                    }
                })
                .toSortedList(Task.TASK_COMPARATOR);
    }

    @Override
    protected void bindHolderAndTask(final TaskViewAdapter.TaskViewHolder taskViewHolder, final Task task) {
        super.bindHolderAndTask(taskViewHolder, task);
        switch (task.getPriorityLevel()) {
            case (Task.PRIORITY_LOW):
                taskViewHolder.imageView.setImageResource(R.mipmap.ic_active_low);
                break;
            case (Task.PRIORITY_NORMAL):
                taskViewHolder.imageView.setImageResource(R.mipmap.ic_active);
                break;
            case (Task.PRIORITY_HIGH):
                taskViewHolder.imageView.setImageResource(R.mipmap.ic_active_high);
                break;
        }

        taskViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ACTIVE_TASK_COUNTER.incrementAndGet();
                ACTIVE_TASKS_QUEUE.offer(task);
                ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(taskViewHolder.imageView, "rotationY", -180f, 0f);
                rotateAnimator.setDuration(700);
                rotateAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        taskViewHolder.imageView.setImageResource(R.mipmap.ic_done);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

                final ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(taskViewHolder.itemView, "translationX", 0f, taskViewHolder.itemView.getWidth());
                translateAnimator.setDuration(700);
                translateAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (ACTIVE_TASK_COUNTER.decrementAndGet() == 0) {
                            Task currentTask;
                            while (ACTIVE_TASK_COUNTER.get() == 0 && (currentTask = ACTIVE_TASKS_QUEUE.poll()) != null) {
                                ((ActiveTaskAnimationListener) context).onActiveTaskImageClick(currentTask);
                            }
                            if (ACTIVE_TASK_COUNTER.get() == 0) {
                                ((ActiveTaskAnimationListener) context).updatePage();
                            }
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(rotateAnimator, translateAnimator);
                animatorSet.start();
            }
        });
    }
}



