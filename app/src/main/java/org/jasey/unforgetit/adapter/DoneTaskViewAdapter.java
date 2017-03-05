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
        taskViewHolder.imageView.setImageResource(R.mipmap.ic_done);

        taskViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IS_BUSY.compareAndSet(false, true)) {
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
                            mDoneTaskAnimationListener = (DoneTaskViewAdapter.DoneTaskAnimationListener) context;
                            mDoneTaskAnimationListener.onDoneTaskImageClick(task);
                            IS_BUSY.compareAndSet(true, false);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            IS_BUSY.compareAndSet(true, false);
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });


                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playSequentially(rotateAnimator, translateAnimator);
                    animatorSet.start();
                }
            }
        });
    }
}
