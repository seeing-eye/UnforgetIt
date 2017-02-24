package org.jasey.unforgetit.fragment;

import org.jasey.unforgetit.adapter.TaskViewAdapter;

public class ActiveTaskViewFragment extends TaskViewFragment {
    @Override
    public void onStart() {
        super.onStart();
        mAdapter = TaskViewAdapter.getInstance(getActivity(), TaskViewType.ACTIVE_TASK_VIEW);
        mRecyclerView.setAdapter(mAdapter);
    }
}
