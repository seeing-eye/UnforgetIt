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

import java.util.List;

public class DoneTaskViewAdapter extends TaskViewAdapter {
    private DoneTaskAnimationListener mDoneTaskAnimationListener;

    public interface DoneTaskAnimationListener {
        void onDoneTaskImageClick(Task task);
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
        taskViewHolder.imageView.setImageResource(R.drawable.done);
        taskViewHolder.imageView.setBorderColorResource(R.color.colorGreen);

        taskViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(taskViewHolder.imageView, "rotationY", -180f, 0f);
                rotateAnimator.setDuration(500);
                rotateAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        switch (task.getPriorityLevel()) {
                            case (Task.PRIORITY_LOW):
                                taskViewHolder.imageView.setImageResource(R.drawable.low_priority);
                                taskViewHolder.imageView.setBorderColorResource(R.color.colorBlue);
                                break;
                            case (Task.PRIORITY_NORMAL):
                                taskViewHolder.imageView.setImageResource(R.drawable.normal_priority);
                                taskViewHolder.imageView.setBorderColorResource(R.color.colorYellow);
                                break;
                            case (Task.PRIORITY_HIGH):
                                taskViewHolder.imageView.setImageResource(R.drawable.hight_priority);
                                taskViewHolder.imageView.setBorderColorResource(R.color.colorRed);
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

                ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(taskViewHolder.itemView, "translationX", 0f, -taskViewHolder.itemView.getWidth());
                translateAnimator.setDuration(500);
                translateAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mDoneTaskAnimationListener = (DoneTaskViewAdapter.DoneTaskAnimationListener) context;
                        mDoneTaskAnimationListener.onDoneTaskImageClick(task);
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
