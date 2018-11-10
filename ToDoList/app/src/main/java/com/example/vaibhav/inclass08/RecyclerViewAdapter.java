package com.example.vaibhav.inclass08;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    ArrayList<TaskData> taskArrayListNew;
    TaskData TASK;
    TaskRecycler taskRecycler;
    DatabaseReference databaseReference;
    PrettyTime p;
    public View v;

    public RecyclerViewAdapter(ArrayList<TaskData> messageList, TaskRecycler taskRecycler) {
        this.taskArrayListNew = messageList;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        this.taskRecycler = taskRecycler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task_custom, parent, false);
        this.v = v;
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        TASK = taskArrayListNew.get(position);
        p = new PrettyTime();

            holder.taskData.setText(TASK.task);

        switch (TASK.priority){
            case 3: holder.taskPriority.setText("High"); break;
            case 2: holder.taskPriority.setText("Medium"); break;
            case 1: holder.taskPriority.setText("Low"); break;
        }

         holder.taskDate.setText(p.format(new Date(TASK.scheduleTime)));
         holder.isDoneCheckBox.setChecked(TASK.isDone);

    }

    @Override
    public int getItemCount() {
        return taskArrayListNew.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView taskData;
        TextView taskPriority;
        TextView taskDate;
        CheckBox isDoneCheckBox;
        Task CurrentTask;

        public MyViewHolder(View v) {
            super(v);
            this.taskData = v.findViewById(R.id.taskText);
            this.taskPriority = v.findViewById(R.id.taskPriority);
            this.isDoneCheckBox = (CheckBox) v.findViewById(R.id.doneCheckBox);
            this.taskDate = v.findViewById(R.id.taskDate);

            ((CheckBox) v.findViewById(R.id.doneCheckBox)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskArrayListNew.get(getPosition()).isDone = !taskArrayListNew.get(getPosition()).isDone;
                    taskRecycler.updateIsDone(taskArrayListNew.get(getPosition()));
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    taskRecycler.deleteTask(taskArrayListNew.get(getPosition()));
                    return false;
                }
            });

        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("demo", "long press");
            return false;
        }
    }


    public interface TaskRecycler {
        void updateIsDone(TaskData task);

        void deleteTask(TaskData taskData);

        int showOption();
    }


}