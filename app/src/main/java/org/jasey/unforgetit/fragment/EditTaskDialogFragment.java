package org.jasey.unforgetit.fragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jasey.unforgetit.R;
import org.jasey.unforgetit.entity.Task;

public class EditTaskDialogFragment extends TaskDialogFragment {
    public static EditTaskDialogFragment getInstance(Task task) {
        EditTaskDialogFragment fragment = new EditTaskDialogFragment();
        fragment.task = task;
        return fragment;
    }

    @Override
    protected void createMenu() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.edit_title);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        }

        mTitle.setText(task.getTitle());
        mTimePicker.setText(DateFormatUtils.format(task.getDate(), Task.TIME_FORMAT));
        mDatePicker.setText(DateFormatUtils.format(task.getDate(), Task.DATE_FORMAT));

        RadioButton low = (RadioButton) rootView.findViewById(R.id.priority_low);
        RadioButton normal = (RadioButton) rootView.findViewById(R.id.priority_normal);
        RadioButton high = (RadioButton) rootView.findViewById(R.id.priority_high);

        switch (task.getPriorityLevel()) {
            case Task.PRIORITY_LOW:
                low.setChecked(true);
                normal.setChecked(false);
                high.setChecked(false);
                break;
            case Task.PRIORITY_NORMAL:
                low.setChecked(false);
                normal.setChecked(true);
                high.setChecked(false);
                break;
            default:
                low.setChecked(false);
                normal.setChecked(false);
                high.setChecked(true);
        }
    }
}
