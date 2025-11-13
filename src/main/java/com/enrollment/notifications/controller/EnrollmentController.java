package com.enrollment.notifications.controller;

import com.enrollment.notifications.dto.BulkEnrollmentRequest;
import com.enrollment.notifications.dto.EnrollmentRequest;
import com.enrollment.notifications.dto.NotificationMessage;
import com.enrollment.notifications.model.Course;
import com.enrollment.notifications.model.Student;
import com.enrollment.notifications.producer.NotificationProducer;
import com.enrollment.notifications.service.MockDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private MockDataService mockDataService;

    @PostMapping("/enroll")
    public ResponseEntity<Map<String, Object>> enrollStudent(@RequestBody EnrollmentRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("Procesando matricula: estudiante={}, curso={}",
                    request.getStudentName(), request.getCourseName());

            // Generar ID de matricula
            String enrollmentId = "ENR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // Crear mensaje de notificacion
            NotificationMessage notification = new NotificationMessage(
                    enrollmentId,
                    request.getStudentName(),
                    request.getStudentEmail(),
                    request.getCourseName(),
                    request.getInstructor(),
                    LocalDateTime.now()
            );

            // Enviar notificacion a la cola de RabbitMQ
            notificationProducer.sendEnrollmentNotification(notification);

            // Responder inmediatamente al cliente
            response.put("status", "success");
            response.put("message", "Matricula procesada exitosamente");
            response.put("enrollmentId", enrollmentId);
            response.put("studentName", request.getStudentName());
            response.put("courseName", request.getCourseName());
            response.put("notificationStatus", "queued");

            logger.info("Matricula procesada exitosamente: {}", enrollmentId);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error al procesar matricula: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Error al procesar matricula: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEnrollment() {
        Map<String, Object> response = new HashMap<>();

        try {
            EnrollmentRequest testRequest = new EnrollmentRequest();
            testRequest.setStudentId("STU001");
            testRequest.setStudentName("Juan Perez");
            testRequest.setStudentEmail("test@example.com");
            testRequest.setCourseId("CRS001");
            testRequest.setCourseName("Introduccion a Spring Boot");
            testRequest.setInstructor("Dr. Maria Garcia");

            String enrollmentId = "ENR-TEST-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

            NotificationMessage notification = new NotificationMessage(
                    enrollmentId,
                    testRequest.getStudentName(),
                    testRequest.getStudentEmail(),
                    testRequest.getCourseName(),
                    testRequest.getInstructor(),
                    LocalDateTime.now()
            );

            notificationProducer.sendEnrollmentNotification(notification);

            response.put("status", "success");
            response.put("message", "Matricula de prueba procesada");
            response.put("enrollmentId", enrollmentId);
            response.put("notificationStatus", "queued");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error en matricula de prueba: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Enrollment Notification Service");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para simular matriculas masivas
     * POST /api/enrollment/simulate
     * Body: { "courseId": "CRS001", "numberOfStudents": 7 }
     */
    @PostMapping("/simulate")
    public ResponseEntity<Map<String, Object>> simulateBulkEnrollment(@RequestBody BulkEnrollmentRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("=== INICIANDO SIMULACION DE MATRICULAS MASIVAS ===");
            logger.info("Curso ID: {}, Cantidad de estudiantes: {}", request.getCourseId(), request.getNumberOfStudents());

            // Validar curso
            Course course = mockDataService.getCourse(request.getCourseId());
            if (course == null) {
                response.put("status", "error");
                response.put("message", "Curso no encontrado: " + request.getCourseId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Validar cantidad de estudiantes
            int maxStudents = mockDataService.getTotalStudentsAvailable();
            if (request.getNumberOfStudents() <= 0) {
                response.put("status", "error");
                response.put("message", "La cantidad de estudiantes debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (request.getNumberOfStudents() > maxStudents) {
                response.put("status", "error");
                response.put("message", "Solo hay " + maxStudents + " estudiantes disponibles");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Obtener estudiantes
            List<Student> students = mockDataService.getStudents(request.getNumberOfStudents());
            List<String> enrollmentIds = new ArrayList<>();

            logger.info("Enviando {} matriculas a la cola de RabbitMQ...", students.size());

            // Crear y enviar notificaciones para cada estudiante
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                String enrollmentId = "ENR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                NotificationMessage notification = new NotificationMessage(
                        enrollmentId,
                        student.getName(),
                        student.getEmail(),
                        course.getName(),
                        course.getInstructor(),
                        LocalDateTime.now()
                );

                // Enviar a la cola
                notificationProducer.sendEnrollmentNotification(notification);
                enrollmentIds.add(enrollmentId);

                logger.info("[{}/{}] Matricula encolada: {} - {} en {}",
                    i + 1, students.size(), enrollmentId, student.getName(), course.getName());
            }

            logger.info("=== {} MATRICULAS ENVIADAS A LA COLA EXITOSAMENTE ===", students.size());

            // Respuesta exitosa
            response.put("status", "success");
            response.put("message", students.size() + " matriculas encoladas exitosamente");
            response.put("courseId", course.getId());
            response.put("courseName", course.getName());
            response.put("instructor", course.getInstructor());
            response.put("studentsEnrolled", students.size());
            response.put("enrollmentIds", enrollmentIds);
            response.put("notificationStatus", "queued");
            response.put("info", "Revisa los logs y RabbitMQ Management UI para ver el procesamiento");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error en simulacion masiva: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Error al procesar simulacion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para listar cursos disponibles
     * GET /api/enrollment/courses
     */
    @GetMapping("/courses")
    public ResponseEntity<Map<String, Object>> listCourses() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("courses", mockDataService.getAllCourses());
        response.put("totalCourses", mockDataService.getAllCourses().size());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obtener informacion del sistema
     * GET /api/enrollment/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalStudentsAvailable", mockDataService.getTotalStudentsAvailable());
        response.put("totalCourses", mockDataService.getAllCourses().size());
        response.put("message", "Sistema de simulacion de matriculas con RabbitMQ");
        response.put("usage", "POST /api/enrollment/simulate con {\"courseId\": \"CRS001\", \"numberOfStudents\": 7}");
        return ResponseEntity.ok(response);
    }
}
