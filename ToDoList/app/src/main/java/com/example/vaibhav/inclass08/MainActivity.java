
package com.example.vaibhav.inclass08;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener,RecyclerViewAdapter.TaskRecycler {
    DatabaseReference databaseReference;
    ArrayList<TaskData> taskData;
    ArrayList<TaskData> taskCompleted;
    ArrayList<TaskData> taskPending;
    int SHOW_OPTION = -1;
    TextView TASK_TV ;
    TextView LABEL_LIST_TYPE;
    Spinner PRIORITY_SPINNER ;
    int SELECTED_ID = 0 ;
    String[] PRIORITY_ARRAY ;
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    ValueEventListener postListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();



        postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskData = new ArrayList<>();
                taskCompleted.clear();
                taskPending.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    TaskData task = postSnapshot.getValue(TaskData.class);
                    if(task.isDone)
                        taskCompleted.add(task);
                        else
                        taskPending.add(task);
                }
                Collections.sort(taskCompleted, new TaskData());
                Collections.sort(taskPending, new TaskData());

                if(SHOW_OPTION== -1)
                {taskData.addAll(taskPending);
                taskData.addAll(taskCompleted);}
                if(SHOW_OPTION== 0)
                    taskData.addAll(taskPending);
                if(SHOW_OPTION== 1)
                    taskData.addAll(taskCompleted);

                setListTypeLabel();
                mAdapter = new RecyclerViewAdapter(taskData,MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error","Failing while fetching data");
                Toast.makeText(getApplicationContext(),"Error adding Task",Toast.LENGTH_LONG).show();
            }
        };


        databaseReference.child("Task").addValueEventListener(postListener);

        ((Button) findViewById(R.id.add_button)).setOnClickListener(this);

        PRIORITY_SPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTED_ID = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        taskData.clear();
        if(id == R.id.show_all)
        {   SHOW_OPTION = -1;
            taskData.addAll(taskPending);
        taskData.addAll(taskCompleted);}
        else if(id == R.id.show_completed) {SHOW_OPTION = 1; taskData.addAll(taskCompleted);}
        else if(id == R.id.show_pending) {SHOW_OPTION = 0;taskData.addAll(taskPending);}

        mAdapter.notifyDataSetChanged();

        setListTypeLabel();
        return super.onOptionsItemSelected(item);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    private void init()
    {
        taskData = new ArrayList<>();
        taskCompleted = new ArrayList<>();
        taskPending = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        TASK_TV = ((TextView) findViewById(R.id.task_tv));
        PRIORITY_SPINNER = ((Spinner) findViewById(R.id.priority));
        PRIORITY_ARRAY = getApplication().getResources().getStringArray(R.array.Priority);
        LABEL_LIST_TYPE = ((TextView) findViewById(R.id.menu_option_details));

        mRecyclerView = findViewById(R.id.task_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setSpinnerValues();
        setListTypeLabel();

    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    private void setListTypeLabel()
    {
        String prefix = "Showing ";
        if(taskData.size() <= 0)
            LABEL_LIST_TYPE.setText("No Tasks to Show");
        else if(SHOW_OPTION == -1)
            LABEL_LIST_TYPE.setText(prefix+ "all Tasks");
         else if(SHOW_OPTION == 1)
            LABEL_LIST_TYPE.setText(prefix+ "Completed Tasks");
         else if(SHOW_OPTION == 0)
            LABEL_LIST_TYPE.setText(prefix+ "Pending Tasks");
    }
    private void writeNewTask(String task, String priority, String scheduleTime, boolean isDone)
    {
        TaskData taskVal = new TaskData(task,priority, scheduleTime,isDone,scheduleTime);
        Map<String,Object> updateObject = new HashMap<>();
        updateObject.put(scheduleTime,taskVal);
        databaseReference.child("Task").updateChildren(updateObject);
        databaseReference.child("Task").addValueEventListener(postListener);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_button)
        {
            boolean isValid = true;
            if(TASK_TV.getText() == null || TASK_TV.getText().toString().trim().isEmpty()) {
                TASK_TV.setError("Add Task heading");
                isValid = false;
            }
            if(SELECTED_ID == 0 ) {
                Toast.makeText(getApplicationContext(),"Select Priority",Toast.LENGTH_LONG).show();
                isValid = false;
            }

            if(isValid)
            {

                writeNewTask(TASK_TV.getText().toString(),PRIORITY_ARRAY[SELECTED_ID]+"",new Date().toString(),false);
                Toast.makeText(getApplicationContext(),"Task added to Todo list",Toast.LENGTH_LONG).show();
                TASK_TV.setText("");
                setSpinnerValues();
                setListTypeLabel();
            }
        }
    }
    public int showOption()
    {
        return SHOW_OPTION;
    }
    private void setSpinnerValues()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.Priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        PRIORITY_SPINNER.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void updateIsDone(TaskData task)
    {
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Task").child(task.taskId);
        Map<String,Object> updateObject = new HashMap<>();
        updateObject.put("isDone",task.isDone);
        updateObject.put("scheduleTime",new Date().toString());
        r.updateChildren(updateObject);

        Toast.makeText(getApplicationContext(),task.task+" Updated",Toast.LENGTH_LONG).show();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteTask(final TaskData taskData) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete Task")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Task").child(taskData.taskId);
                        r.removeValue();
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, ""+taskData.task+ " has been deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
