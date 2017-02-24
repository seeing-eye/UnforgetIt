package org.jasey.unforgetit.fragment;

import org.jasey.unforgetit.adapter.TaskViewAdapter;

public class TimeIsOutTaskViewFragment extends TaskViewFragment {
    @Override
    public void onStart() {
        super.onStart();
        mAdapter = TaskViewAdapter.getInstance(getActivity(), TaskViewType.TIME_IS_OUT_VIEW);
        mRecyclerView.setAdapter(mAdapter);
    }
}
