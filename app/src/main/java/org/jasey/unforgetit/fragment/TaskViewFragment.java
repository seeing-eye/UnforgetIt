package org.jasey.unforgetit.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jasey.unforgetit.R;
import org.jasey.unforgetit.adapter.TaskViewAdapter;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.utils.TaskUtil;

public class TaskViewFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static Task[] mTaskArray ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_view_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = TaskViewAdapter.getInstance(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    public static Task[] buildTaskArray() {
        mTaskArray = TaskUtil.getTestTaskArray();
        return mTaskArray;
    }
}
