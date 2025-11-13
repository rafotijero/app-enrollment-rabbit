package com.enrollment.notifications.dto;

import java.time.LocalDateTime;

public class NotificationMessage {
    private String enrollmentId;
    private String studentName;
    private String studentEmail;
    private String courseName;
    private String instructor;
    private LocalDateTime enrollmentDate;

    public NotificationMessage() {
    }

    public NotificationMessage(String enrollmentId, String studentName, String studentEmail,
                               String courseName, String instructor, LocalDateTime enrollmentDate) {
        this.enrollmentId = enrollmentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.courseName = courseName;
        this.instructor = instructor;
        this.enrollmentDate = enrollmentDate;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}
