package org.jasey.unforgetit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.jasey.unforgetit.adapter.TaskPagerAdapter;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.fragment.AddTaskDialogFragment;
import org.jasey.unforgetit.repository.TaskRepository;

public class UnforgetItActivity extends AppCompatActivity implements AddTaskDialogFragment.AddTaskDialogListener {
    private final static int PAGE_COUNT = 3;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private FloatingActionButton mAddFAB;
    private AddTaskDialogFragment mAddDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unforget_it_activity);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new TaskPagerAdapter(getSupportFragmentManager(), getApplicationContext(), PAGE_COUNT);
        mPager.setAdapter(mPagerAdapter);

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
                mAddFAB.setVisibility(View.INVISIBLE);
                mAddDialog = new AddTaskDialogFragment();
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.unforget_it_activity, mAddDialog)
                        .addToBackStack(null)
                        .commit();
            }
        });
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
    public void onSaveClick(Task newTask) {
        if (TaskRepository.getInstance(TaskRepository.Type.JPA, this).addNew(newTask)) {
            mPagerAdapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.task_created_toast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.task_already_exist_toast, Toast.LENGTH_SHORT).show();
        }
    }
}
