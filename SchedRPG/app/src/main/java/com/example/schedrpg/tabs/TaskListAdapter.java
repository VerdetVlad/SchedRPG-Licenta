package com.example.schedrpg.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schedrpg.R;
import com.example.schedrpg.myfirebasetool.ChangeFirebaseData;
import com.example.schedrpg.user.User;
import com.example.schedrpg.user.UserTask;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TaskListAdapter extends ArrayAdapter<UserTask> {


    public TaskListAdapter(Context context, ArrayList<UserTask> userArrayList){

        super(context, R.layout.task_view_item,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        UserTask userTask = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_view_item,parent,false);

        }

        TextView name = convertView.findViewById(R.id.taskItemName);
        TextView difficulty = convertView.findViewById(R.id.taskItemDifficulty);
        TextView type = convertView.findViewById(R.id.taskItemType);
        TextView description = convertView.findViewById(R.id.taskItemDescription);
        Button deleteButton = convertView.findViewById(R.id.deleteTaskItem);

        name.setText(userTask.getName());
        difficulty.setText(userTask.dificultyStringValue());
        type.setText(userTask.typeStringValue());
        description.setText(userTask.getDescription());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TasksViewTab.removeItem(userTask);
            }
        });


        return convertView;
    }




}