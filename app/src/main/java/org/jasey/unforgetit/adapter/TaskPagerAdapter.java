package org.jasey.unforgetit.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.jasey.unforgetit.R;
import org.jasey.unforgetit.entity.TaskType;
import org.jasey.unforgetit.fragment.TaskViewFragment;

public class TaskPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private int pagesCount;

    public TaskPagerAdapter(FragmentManager fm, Context context, int pagesCount) {
        super(fm);
        mContext = context;
        this.pagesCount = pagesCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TaskViewFragment.getInstance(TaskType.ACTIVE_TASK);
            case 1:
                return TaskViewFragment.getInstance(TaskType.DONE_TASK);
            case 2:
                return TaskViewFragment.getInstance(TaskType.TIME_IS_OUT_TASK);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pagesCount;
    }

    @Override
    public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.active_tasks_title);
            case 1:
                return mContext.getString(R.string.done_title);
            case 2:
                return mContext.getString(R.string.time_is_out_title);
            default:
                return null;
        }
    }
}
