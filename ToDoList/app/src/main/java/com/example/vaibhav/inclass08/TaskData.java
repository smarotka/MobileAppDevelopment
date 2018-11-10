package com.example.vaibhav.inclass08;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskData implements Comparator<TaskData> {


    public String task;
    public int priority;
    public String scheduleTime;
    public  boolean isDone;
    public String taskId;



    public TaskData() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("task", this.task);
        result.put("priority", this.priority);
        result.put("scheduleTime", this.scheduleTime);
        result.put("isDone", this.isDone);
        result.put("taskId", this.taskId);

        return result;
    }
    @Override
    public String toString() {
        return "TaskData{" +
                "task='" + task + '\'' +
                ", priority='" + priority + '\'' +
                ", scheduleTime=" + scheduleTime +
                ", isDone=" + isDone +
                ", taskId='" + taskId + '\'' +
                '}';
    }
    public TaskData(String task, String priority, String scheduleTime, boolean isDone,String taskID) {
        this.task = task;
        String strPriority = priority.toUpperCase().trim();
        switch (strPriority){
            case "HIGH": this.priority = 3;
                            break;
            case "MEDIUM": this.priority = 2;
                            break;

            case "LOW": this.priority = 1;
                        break;
        }
        this.scheduleTime = scheduleTime;
        this.isDone = isDone;
        this.taskId = taskID;
    }


    @Override
    public int compare(TaskData o1, TaskData o2) {
        return o2.priority -  o1.priority;
    }
}
