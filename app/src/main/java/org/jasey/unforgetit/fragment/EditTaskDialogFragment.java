package org.jasey.unforgetit.fragment;

import android.os.Bundle;
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
        fragment.mTask = task;
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(ID, mTask.getId());
    }

    @Override
    protected void createMenu() {
        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.edit_title);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        }

        mTitle.setText(mTask.getTitle());
        mTimePicker.setText(DateFormatUtils.format(mTask.getDate(), Task.TIME_FORMAT));
        mDatePicker.setText(DateFormatUtils.format(mTask.getDate(), Task.DATE_FORMAT));

        RadioButton low = (RadioButton) mRootView.findViewById(R.id.priority_low);
        RadioButton normal = (RadioButton) mRootView.findViewById(R.id.priority_normal);
        RadioButton high = (RadioButton) mRootView.findViewById(R.id.priority_high);

        switch (mTask.getPriorityLevel()) {
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

        RadioButton noNot = (RadioButton) mRootView.findViewById(R.id.no_notification);
        RadioButton tenNot = (RadioButton) mRootView.findViewById(R.id.ten_min_notification);
        RadioButton thirtyNot = (RadioButton) mRootView.findViewById(R.id.thirty_min_notification);

        switch (mTask.getAlarmAdvanceTime()) {
            case Task.ALARM_ADVANCE_10:
                noNot.setChecked(false);
                tenNot.setChecked(true);
                thirtyNot.setChecked(false);
                break;
            case Task.ALARM_ADVANCE_30:
                noNot.setChecked(false);
                tenNot.setChecked(false);
                thirtyNot.setChecked(true);
                break;
            default:
                noNot.setChecked(true);
                tenNot.setChecked(false);
                thirtyNot.setChecked(false);
        }
    }
}
