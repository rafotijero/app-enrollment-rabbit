package com.enrollment.notifications.model;

public class Course {
    private String id;
    private String name;
    private String instructor;

    public Course() {
    }

    public Course(String id, String name, String instructor) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
