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

public class TaskViewFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        mAdapter = TaskViewAdapter.getInstance(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }


}
