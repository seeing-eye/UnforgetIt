package org.jasey.unforgetit.fragment;

import org.jasey.unforgetit.adapter.TaskViewAdapter;
import org.jasey.unforgetit.entity.TaskType;

public class ActiveTaskViewFragment extends TaskViewFragment {
    @Override
    public void onStart() {
        super.onStart();
        mAdapter = TaskViewAdapter.getInstance(getActivity(), TaskType.ACTIVE_TASK);
        mRecyclerView.setAdapter(mAdapter);
    }
}
