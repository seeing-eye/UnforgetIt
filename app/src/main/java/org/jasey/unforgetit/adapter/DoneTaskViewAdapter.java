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

public class DoneTaskViewAdapter extends TaskViewAdapter {
    private static final BlockingQueue<Task> DONE_TASKS_QUEUE = new LinkedBlockingQueue<>();
    private static final AtomicInteger DONE_TASK_COUNTER = new AtomicInteger(0);

    public interface DoneTaskAnimationListener {
        void onDoneTaskImageClick(Task task);

        void updatePage();
    }

    public DoneTaskViewAdapter(Context context) {
        super(context);
    }

    @Override
    protected List<Task> findTasks() {
        return FluentIterable.from(TaskRepository.getInstance(TaskRepository.Type.JPA, context).getAll())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean apply(Task input) {
                        return input.isDone();
                    }
                })
                .toSortedList(Task.TASK_COMPARATOR);
    }

    @Override
    protected void bindHolderAndTask(final TaskViewHolder taskViewHolder, final Task task) {
        super.bindHolderAndTask(taskViewHolder, task);
        taskViewHolder.imageView.setImageResource(R.mipmap.ic_done);

        taskViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DONE_TASK_COUNTER.incrementAndGet();
                DONE_TASKS_QUEUE.offer(task);
                ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(taskViewHolder.imageView, "rotationY", -180f, 0f);
                rotateAnimator.setDuration(400);
                rotateAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

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
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                ObjectAnimator translateAnimator;
                if (task.getDate().before(new Date())) {
                    translateAnimator = ObjectAnimator.ofFloat(taskViewHolder.itemView, "translationX", 0f, taskViewHolder.itemView.getWidth());
                } else {
                    translateAnimator = ObjectAnimator.ofFloat(taskViewHolder.itemView, "translationX", 0f, -taskViewHolder.itemView.getWidth());
                }
                translateAnimator.setDuration(400);
                translateAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (DONE_TASK_COUNTER.decrementAndGet() == 0) {
                            Task currentTask;
                            while (DONE_TASK_COUNTER.get() == 0 && (currentTask = DONE_TASKS_QUEUE.poll()) != null) {
                                ((DoneTaskAnimationListener) context).onDoneTaskImageClick(currentTask);
                            }
                            if (DONE_TASK_COUNTER.get() == 0) {
                                ((DoneTaskAnimationListener) context).updatePage();
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
