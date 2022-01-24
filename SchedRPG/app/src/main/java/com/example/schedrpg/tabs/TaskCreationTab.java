package com.example.schedrpg.tabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.schedrpg.authentification.LogIn;
import com.example.schedrpg.databinding.ActivityTaskCreationTabBinding;
import com.example.schedrpg.myfirebasetool.ChangeFirebaseData;
import com.example.schedrpg.user.User;
import com.example.schedrpg.user.UserTask;

public class TaskCreationTab extends AppCompatActivity {

    ActivityTaskCreationTabBinding binding;
    User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskCreationTabBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String[] stats = new String[]{"Strength" ,
                "Dexterity" ,
                "Constitution" ,
                "Wisdom" ,
                "Intelligence" ,
                "Charisma"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stats);
        binding.createTaskType.setAdapter(adapter);

        String[] difficulty = new String[]{"very easy", "easy", "medium", "hard", "very hard"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, difficulty);
        binding.createTaskDifficulty.setAdapter(adapter2);

        binding.createTaskSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskToFirebase();
            }
        });

        binding.createTaskBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskCreationTab.this, LogInSuccess.class));
            }
        });

    }

    private void addTaskToFirebase()
    {
        String name = binding.createTaskName.getText().toString().trim();
        String description = binding.createTaskDescription.getText().toString().trim();
        int difficulty = binding.createTaskDifficulty.getSelectedItemPosition();
        int type = binding.createTaskType.getSelectedItemPosition();

        if(name.isEmpty())
        {
            binding.createTaskName.setError("Name is required.");
            binding.createTaskName.requestFocus();
            return;
        }

        UserTask userTask = new UserTask(type,difficulty,name,description);
        user.addTask(userTask);
        System.out.println(user.toString());

        ChangeFirebaseData changeFirebaseData = new ChangeFirebaseData();
        changeFirebaseData.updateUserData(user);




    }
}