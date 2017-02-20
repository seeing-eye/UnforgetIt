package org.jasey.unforgetit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.jasey.unforgetit.fragment.TaskViewFragment;
import org.jasey.unforgetit.repository.TaskRepository;

public class UnforgetItActivity extends AppCompatActivity {  //TODO добавить интерфейс DBAccessable с методом getTaskRepository()
    //TODO UnforgetItActivity должно реализовывать этот интерфейс
    //TODO объявить здесь переменную
    //private TaskRepository mTaskRepository;
    //TODO сделать метод initTaskRepository в котором инициализировать репозиторий


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unforget_it_activity);


        //if (mTaskRepository == null) {
        //    initTaskRepository(this);
        //}

        if (getFragmentManager().findFragmentById(R.id.task_view_fragment) == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.task_view_fragment, new TaskViewFragment())//передать сюда this
                    .commit();
        }
    }

}
