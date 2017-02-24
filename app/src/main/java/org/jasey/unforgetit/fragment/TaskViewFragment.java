package org.jasey.unforgetit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jasey.unforgetit.R;

public abstract class TaskViewFragment extends Fragment {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    public static Fragment getInstance(TaskViewType type) {
        switch (type) {
            case ACTIVE_TASK_VIEW: return new ActiveTaskViewFragment();
            case TIME_IS_OUT_VIEW: return new TimeIsOutTaskViewFragment();
            default:return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_view_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public enum TaskViewType {
        ACTIVE_TASK_VIEW,
        TIME_IS_OUT_VIEW
    }
}


