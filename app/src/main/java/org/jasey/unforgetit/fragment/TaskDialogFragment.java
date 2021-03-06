package org.jasey.unforgetit.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jasey.unforgetit.R;
import org.jasey.unforgetit.UnforgetItActivity;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.repository.TaskRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class TaskDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String ID = "ID";

    protected EditText mTitle;
    TextView mDatePicker, mTimePicker;
    protected View mRootView;
    private SaveTaskDialogListener mListener;
    protected Task mTask;

    public interface SaveTaskDialogListener {
        void onSaveClick(Task task);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((UnforgetItActivity) getActivity()).hideAddButton();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((UnforgetItActivity) getActivity()).hideAddButton();

        mRootView = inflater.inflate(R.layout.task_dialog_fragment, container, false);

        mTitle = (EditText) mRootView.findViewById(R.id.task_title);
        mTitle.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mTitle.setSingleLine(true);

        mDatePicker = (TextView) mRootView.findViewById(R.id.date_picker);
        mTimePicker = (TextView) mRootView.findViewById(R.id.time_picker);

        mDatePicker.setOnClickListener(this);
        mTimePicker.setOnClickListener(this);

        if (savedInstanceState != null) {
            final long id = savedInstanceState.getLong(ID, -1);
            mTask = FluentIterable.from(TaskRepository.getInstance(TaskRepository.Type.JPA, getContext()).getAll())
                    .filter(new Predicate<Task>() {
                        @Override
                        public boolean apply(Task task) {
                            return task.getId() == id;
                        }
                    }).first().or(new Task());
        }

        createMenu();
        setHasOptionsMenu(true);

        return mRootView;
    }

    protected abstract void createMenu();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.dialog_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Save case

        if (id == R.id.action_save) {
            Date date;
            if (titleNotEmpty() && timeDateNotEmpty() && (date = getDateFromDialog()) != null) {
                if (mTask == null) {
                    mTask = new Task();
                }
                mTask.setPriorityLevel(getPriorityFromDialog());
                mTask.setAlarmAdvanceTime(getAlarmAdvanceFromDialog());
                mTask.setTitle(mTitle.getText().toString());
                mTask.setDate(date);
            } else {
                return false;
            }

            mListener.onSaveClick(mTask);

            dismiss();
            return true;
        }

        //HOME case
        else if (id == android.R.id.home) {
            if (StringUtils.isEmpty(mTitle.getText().toString())) {
                dismiss();
                ((UnforgetItActivity) getActivity()).showAddButton();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.exit_message)
                        .setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                                ((UnforgetItActivity) getActivity()).showAddButton();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        int year, month, day, hour, minute;
        final Calendar c = Calendar.getInstance();
        if (mTask != null) {
            c.setTime(mTask.getDate());
        }

        if (v.equals(mDatePicker)) {

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    c.set(year, month, dayOfMonth);
                    mDatePicker.setText(DateFormatUtils.format(c, Task.DATE_FORMAT));
                }
            }, year, month, day);
            datePickerDialog.show();
        }

        if (v.equals(mTimePicker)) {
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            mTimePicker.setText(DateFormatUtils.format(c, Task.TIME_FORMAT));
                        }
                    }, hour, minute, true);
            timePickerDialog.show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SaveTaskDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SaveTaskDialogListener");
        }
    }

    private boolean titleNotEmpty() {
        if (mTitle.length() == 0) {
            Toast.makeText(getContext(), R.string.title_alert_toast, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean timeDateNotEmpty() {
        if (mDatePicker.length() == 0 || mTimePicker.length() == 0) {
            Toast.makeText(getContext(), R.string.date_time_alert_toast, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Date getDateFromDialog() {
        Date date = null;
        try {
            date = new SimpleDateFormat(
                    Task.TIME_FORMAT +
                            Task.DELIMITER +
                            Task.DATE_FORMAT,
                    Locale.getDefault())
                    .parse(
                            mTimePicker.getText().toString() +
                                    Task.DELIMITER +
                                    mDatePicker.getText().toString());
        } catch (ParseException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return date;
    }

    private int getPriorityFromDialog() {
        switch (((RadioGroup) getView().findViewById(R.id.priority_group))
                .getCheckedRadioButtonId()) {
            case R.id.priority_high:
                return Task.PRIORITY_HIGH;
            case R.id.priority_low:
                return Task.PRIORITY_LOW;
            default:
                return Task.PRIORITY_NORMAL;
        }
    }

    private int getAlarmAdvanceFromDialog() {
        switch (((RadioGroup) getView().findViewById(R.id.notification_types))
                .getCheckedRadioButtonId()) {
            case R.id.no_notification:
                return 0;
            case R.id.ten_min_notification:
                return Task.ALARM_ADVANCE_10;
            default:
                return Task.ALARM_ADVANCE_30;
        }
    }
}
