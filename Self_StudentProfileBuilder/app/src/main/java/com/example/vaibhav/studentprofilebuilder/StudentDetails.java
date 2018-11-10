package com.example.vaibhav.studentprofilebuilder;

import java.io.Serializable;

public class StudentDetails implements Serializable {
    String firstName;
    String lastName;
    String studentId;
    String department;
    String avatarId;

    public StudentDetails(String firstName,String lastName, String studentId, String department,String avatarId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
        this.department = department;
        this.avatarId = avatarId;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
