package com.example.schedrpg.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schedrpg.databinding.ActivityTasksViewTabBinding;
import com.example.schedrpg.myfirebasetool.ChangeFirebaseData;
import com.example.schedrpg.user.User;
import com.example.schedrpg.user.UserTask;

public class TasksViewTab extends AppCompatActivity {

    ActivityTasksViewTabBinding binding;
    static User user = new User();
    @SuppressLint("StaticFieldLeak")
    static ListView listView;
    @SuppressLint("StaticFieldLeak")
    static Context context;
    static TaskListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTasksViewTabBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = getApplicationContext();

        User user = new User();
        listAdapter = new TaskListAdapter(TasksViewTab.this,user.getTasks());
        listView =binding.listview;
        listView.setAdapter(listAdapter);

    }

    public static void removeItem(UserTask task) {
        makeToast("Removed task: " + task.getName());
        user.removeTask(task);
        ChangeFirebaseData changeFirebaseData = new ChangeFirebaseData();
        changeFirebaseData.updateUserData(user);
        listView.setAdapter(listAdapter);
    }


    static Toast t;

    private static void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        t.show();
    }

}