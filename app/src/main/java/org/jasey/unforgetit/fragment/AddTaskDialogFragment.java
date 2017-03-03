package org.jasey.unforgetit.fragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.jasey.unforgetit.R;

public class AddTaskDialogFragment extends TaskDialogFragment {

    @Override
    protected void createMenu() {
        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_title);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        }
    }
}
