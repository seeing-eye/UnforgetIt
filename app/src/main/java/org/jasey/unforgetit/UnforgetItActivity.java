package org.jasey.unforgetit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

import org.jasey.unforgetit.adapter.ActiveTaskViewAdapter;
import org.jasey.unforgetit.adapter.DoneTaskViewAdapter;
import org.jasey.unforgetit.adapter.TaskPagerAdapter;
import org.jasey.unforgetit.adapter.TaskViewAdapter;
import org.jasey.unforgetit.alarm.AlarmReceiver;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.fragment.AddTaskDialogFragment;
import org.jasey.unforgetit.fragment.EditTaskDialogFragment;
import org.jasey.unforgetit.fragment.TaskDialogFragment;
import org.jasey.unforgetit.repository.TaskRepository;

import java.util.Arrays;
import java.util.List;

public class UnforgetItActivity extends AppCompatActivity
        implements
        TaskDialogFragment.SaveTaskDialogListener,
        ActiveTaskViewAdapter.ActiveTaskAnimationListener,
        DoneTaskViewAdapter.DoneTaskAnimationListener,
        TaskViewAdapter.ContextMenuItemClickListener {

    private final static int PAGE_COUNT = 3;
    private final static int PERMISSION_REQUEST_CODE = 1;

    public static boolean sActivityVisible;

    private ViewPager mPager;
    private FragmentStatePagerAdapter mPagerAdapter;
    private FloatingActionButton mAddFAB;
    private AddTaskDialogFragment mAddDialog;
    private AlarmReceiver mAlarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unforget_it_activity);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new TaskPagerAdapter(getSupportFragmentManager(), getApplicationContext(), PAGE_COUNT);
        mPager.setAdapter(mPagerAdapter);
        mAlarmReceiver = new AlarmReceiver();


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mAddFAB = (FloatingActionButton) findViewById(R.id.add_fab);
        mAddFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddDialog = new AddTaskDialogFragment();
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.unforget_it_activity, mAddDialog)
                        .addToBackStack(null)
                        .commit();
                mPagerAdapter.notifyDataSetChanged();
            }
        });
    }

    public void checkPermissionsAndRequest(List<String> permissions, int requestCode) {
        List<String> notGranted = FluentIterable.from(permissions).filter(new Predicate<String>() {
            @Override
            public boolean apply(String permission) {
                return ActivityCompat.checkSelfPermission(UnforgetItActivity.this, permission) != PackageManager.PERMISSION_GRANTED;
            }
        }).toList();
        if (!notGranted.isEmpty()) {
            ActivityCompat.requestPermissions(this, Iterables.toArray(notGranted, String.class), requestCode);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissionsAndRequest(
                Arrays.asList(
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                        Manifest.permission.VIBRATE),
                PERMISSION_REQUEST_CODE
        );
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onSaveClick(Task task) {
        if (TaskRepository.getInstance(TaskRepository.Type.JPA, this).addNew(task)) {
            Toast.makeText(this, R.string.task_created_toast, Toast.LENGTH_LONG).show();
        } else {
            TaskRepository.getInstance(TaskRepository.Type.JPA, this).update(task);
            mAlarmReceiver.cancelAlarm(this, task);
            Toast.makeText(this, R.string.task_updated_toast, Toast.LENGTH_SHORT).show();
        }
        mAlarmReceiver.setAlarm(this, task);
        mPagerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActiveTaskImageClick(Task task) {
        task.setDone(true);
        TaskRepository.getInstance(TaskRepository.Type.JPA, this).update(task);
    }

    @Override
    public void updatePage() {
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDoneTaskImageClick(Task task) {
        task.setDone(false);
        TaskRepository.getInstance(TaskRepository.Type.JPA, this).update(task);
    }

    @Override
    public void onDeleteActionClick(Task task) {
        mAlarmReceiver.cancelAlarm(this, task);
        TaskRepository.getInstance(TaskRepository.Type.JPA, this).remove(task);
        mPagerAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.task_deleted_toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditItemClick(Task task) {
        EditTaskDialogFragment mEditDialog = EditTaskDialogFragment.getInstance(task);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.unforget_it_activity, mEditDialog)
                .addToBackStack(null)
                .commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        showAddButton();
        sActivityVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        sActivityVisible = false;
    }

    public void hideAddButton() {
        mAddFAB.hide();
    }

    public void showAddButton() {
        mAddFAB.show();
    }
}
