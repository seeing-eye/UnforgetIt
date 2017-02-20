package org.jasey.unforgetit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.jasey.unforgetit.fragment.TaskViewFragment;

public class UnforgetItActivity extends AppCompatActivity {
    private TaskViewFragment taskViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unforget_it_activity);

        taskViewFragment = new TaskViewFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.task_view_fragment, taskViewFragment)
                .commit();
    }

}
