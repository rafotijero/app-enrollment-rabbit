package com.enrollment.notifications.service;

import com.enrollment.notifications.model.Course;
import com.enrollment.notifications.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MockDataService {

    private final Map<String, Course> courses = new HashMap<>();
    private final List<Student> students = new ArrayList<>();

    public MockDataService() {
        initializeCourses();
        initializeStudents();
    }

    private void initializeCourses() {
        courses.put("CRS001", new Course("CRS001", "Arquitectura de Software", "Prof. Carlos Mendez"));
        courses.put("CRS002", new Course("CRS002", "Bases de Datos Avanzadas", "Dra. Patricia Gomez"));
        courses.put("CRS003", new Course("CRS003", "Desarrollo Web Full Stack", "Ing. Roberto Silva"));
        courses.put("CRS004", new Course("CRS004", "Machine Learning", "Dr. Alberto Ruiz"));
        courses.put("CRS005", new Course("CRS005", "Seguridad Informatica", "Ing. Miguel Herrera"));
        courses.put("CRS006", new Course("CRS006", "Cloud Computing", "Dra. Laura Diaz"));
        courses.put("CRS007", new Course("CRS007", "DevOps y CI/CD", "Ing. Fernando Castro"));
        courses.put("CRS008", new Course("CRS008", "Microservicios", "Prof. Gabriela Ortiz"));
        courses.put("CRS009", new Course("CRS009", "Testing y QA", "Ing. Ricardo Flores"));
        courses.put("CRS010", new Course("CRS010", "Inteligencia Artificial", "Dr. Antonio Rojas"));
    }

    private void initializeStudents() {
        students.add(new Student("STU001", "Ana Rodriguez", "ana.rodriguez@university.com"));
        students.add(new Student("STU002", "Luis Martinez", "luis.martinez@university.com"));
        students.add(new Student("STU003", "Maria Fernandez", "maria.fernandez@university.com"));
        students.add(new Student("STU004", "Carlos Gomez", "carlos.gomez@university.com"));
        students.add(new Student("STU005", "Sofia Torres", "sofia.torres@university.com"));
        students.add(new Student("STU006", "Diego Ramirez", "diego.ramirez@university.com"));
        students.add(new Student("STU007", "Valentina Lopez", "valentina.lopez@university.com"));
        students.add(new Student("STU008", "Andres Morales", "andres.morales@university.com"));
        students.add(new Student("STU009", "Isabella Vargas", "isabella.vargas@university.com"));
        students.add(new Student("STU010", "Sebastian Navarro", "sebastian.navarro@university.com"));
        students.add(new Student("STU011", "Emma Watson", "emma.watson@university.com"));
        students.add(new Student("STU012", "Oliver Smith", "oliver.smith@university.com"));
        students.add(new Student("STU013", "Sophia Brown", "sophia.brown@university.com"));
        students.add(new Student("STU014", "Liam Johnson", "liam.johnson@university.com"));
        students.add(new Student("STU015", "Ava Williams", "ava.williams@university.com"));
        students.add(new Student("STU016", "Noah Davis", "noah.davis@university.com"));
        students.add(new Student("STU017", "Isabella Miller", "isabella.miller@university.com"));
        students.add(new Student("STU018", "Ethan Wilson", "ethan.wilson@university.com"));
        students.add(new Student("STU019", "Mia Moore", "mia.moore@university.com"));
        students.add(new Student("STU020", "James Taylor", "james.taylor@university.com"));
        students.add(new Student("STU021", "Charlotte Anderson", "charlotte.anderson@university.com"));
        students.add(new Student("STU022", "Benjamin Thomas", "benjamin.thomas@university.com"));
        students.add(new Student("STU023", "Amelia Jackson", "amelia.jackson@university.com"));
        students.add(new Student("STU024", "Lucas White", "lucas.white@university.com"));
        students.add(new Student("STU025", "Harper Harris", "harper.harris@university.com"));
        students.add(new Student("STU026", "Mason Martin", "mason.martin@university.com"));
        students.add(new Student("STU027", "Evelyn Thompson", "evelyn.thompson@university.com"));
        students.add(new Student("STU028", "Logan Garcia", "logan.garcia@university.com"));
        students.add(new Student("STU029", "Abigail Martinez", "abigail.martinez@university.com"));
        students.add(new Student("STU030", "Alexander Robinson", "alexander.robinson@university.com"));
    }

    public Course getCourse(String courseId) {
        return courses.get(courseId);
    }

    public List<Student> getStudents(int count) {
        if (count > students.size()) {
            count = students.size();
        }
        return students.subList(0, count);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public int getTotalStudentsAvailable() {
        return students.size();
    }
}
