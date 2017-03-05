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
import org.jasey.unforgetit.UnforgetItActivity;
import org.jasey.unforgetit.entity.TaskType;

public abstract class TaskViewFragment extends Fragment {
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static Fragment getInstance(TaskType type) {
        switch (type) {
            case ACTIVE_TASK: return new ActiveTaskViewFragment();
            case TIME_IS_OUT_TASK: return new TimeIsOutTaskViewFragment();
            case DONE_TASK: return new DoneTaskViewFragment();
            default:return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((UnforgetItActivity) getActivity()).showAddButton();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((UnforgetItActivity) getActivity()).showAddButton();
        return inflater.inflate(R.layout.task_view_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}


