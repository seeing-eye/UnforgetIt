package org.jasey.unforgetit.fragment;

import org.jasey.unforgetit.adapter.TaskViewAdapter;
import org.jasey.unforgetit.entity.TaskType;

public class TimeIsOutTaskViewFragment extends TaskViewFragment {
    @Override
    public void onStart() {
        super.onStart();
        mAdapter = TaskViewAdapter.getInstance(getActivity(), TaskType.TIME_IS_OUT_TASK);
        mRecyclerView.setAdapter(mAdapter);
    }
}
