package org.jasey.unforgetit.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jasey.unforgetit.R;
import org.jasey.unforgetit.entity.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskDialogFragment extends DialogFragment implements View.OnClickListener {
    private EditText mTitle, mDatePicker, mTimePicker;
    private AddTaskDialogListener mListener;

    public interface AddTaskDialogListener {
        void onSaveClick(Task newTask);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.add_task_dialog_fragment, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_title);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        }
        setHasOptionsMenu(true);

        mTitle = (EditText) rootView.findViewById(R.id.task_title);

        mDatePicker = (EditText) rootView.findViewById(R.id.date_picker);
        mTimePicker = (EditText) rootView.findViewById(R.id.time_picker);

        mDatePicker.setOnClickListener(this);
        mTimePicker.setOnClickListener(this);

        return rootView;
    }

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
        View saveButton = getView().findViewById(R.id.action_save);

        int id = item.getItemId();

        if (id == R.id.action_save) {

            Task newTask = new Task();
            newTask.setPriorityLevel(getPriorityFromDialog());
            if (titleNotEmpty() && timeDateNotEmpty() && getDateFromDialog() != null) {
                newTask.setTitle(mTitle.getText().toString());
                newTask.setDate(getDateFromDialog());
            } else {
                return false;
            }

            mListener.onSaveClick(newTask);
            dismiss();
            return true;
        }
        else if (id == android.R.id.home) {
            if (StringUtils.isEmpty(mTitle.getText().toString())) {
                dismiss();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.exit_message)
                        .setPositiveButton(R.string.erase, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
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
        getActivity().findViewById(R.id.add_fab).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int mYear, mMonth, mDay, mHour, mMinute;

        if (v == mDatePicker) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    c.set(year, month, dayOfMonth);
                    mDatePicker.setText(DateFormatUtils.format(c, Task.DATE_FORMAT));
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == mTimePicker) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            mTimePicker.setText(DateFormatUtils.format(c, Task.TIME_FORMAT));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (AddTaskDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddTaskDialogListener");
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
                                mDatePicker.getText().toString());}
        catch (ParseException e) {
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
}
