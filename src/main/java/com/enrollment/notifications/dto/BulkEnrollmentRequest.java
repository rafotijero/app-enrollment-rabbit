package com.enrollment.notifications.dto;

public class BulkEnrollmentRequest {
    private String courseId;
    private int numberOfStudents;

    public BulkEnrollmentRequest() {
    }

    public BulkEnrollmentRequest(String courseId, int numberOfStudents) {
        this.courseId = courseId;
        this.numberOfStudents = numberOfStudents;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
}
