# Guia Rapida de Inicio

## Desplegar en Docker Desktop (Windows)

### 1. Abrir Docker Desktop
Asegurate de que Docker Desktop este corriendo.

### 2. Abrir Terminal en el Directorio del Proyecto
```bash
cd D:\Rafo\ArquitecturaSoftware\notifications\app-enrollment-rabbit
```

### 3. Levantar Servicios
```bash
docker-compose up --build
```

Esto levantara:
- RabbitMQ en puerto 5672 (AMQP) y 15672 (Management UI)
- Aplicacion Spring Boot en puerto 8080

### 4. Esperar a que Inicien los Servicios
Veras logs indicando que los servicios estan listos:
```
enrollment-app | Started EnrollmentNotificationApplication
```

---

## Probar la Aplicacion

### Opcion 1: Usando cURL

```bash
curl -X POST http://localhost:8080/api/enrollment/enroll -H "Content-Type: application/json" -d "{\"studentId\":\"STU001\",\"studentName\":\"Maria Rodriguez\",\"studentEmail\":\"maria@example.com\",\"courseId\":\"CRS101\",\"courseName\":\"Arquitectura de Software\",\"instructor\":\"Dr. Carlos\"}"
```

### Opcion 2: Test Rapido

```bash
curl http://localhost:8080/api/enrollment/test
```

### Opcion 3: Postman

1. **POST** a `http://localhost:8080/api/enrollment/enroll`
2. Headers: `Content-Type: application/json`
3. Body (raw JSON):
```json
{
  "studentId": "STU001",
  "studentName": "Maria Rodriguez",
  "studentEmail": "maria@example.com",
  "courseId": "CRS101",
  "courseName": "Arquitectura de Software",
  "instructor": "Dr. Carlos"
}
```

---

## Monitorear RabbitMQ

1. Abrir navegador: http://localhost:15672
2. Usuario: `guest`
3. Password: `guest`
4. Ir a **Queues** para ver mensajes

---

## Ver Logs

```bash
# Logs de la aplicacion
docker logs -f enrollment-app

# Logs de RabbitMQ
docker logs -f enrollment-rabbitmq
```

---

## Detener Servicios

```bash
# Ctrl+C en la terminal donde corre docker-compose
# O en otra terminal:
docker-compose down
```

---

## ENDPOINTS DISPONIBLES

### 1. Matricular Estudiante
- **POST** `http://localhost:8080/api/enrollment/enroll`
- Body: JSON con datos del estudiante y curso

### 2. Test de Matricula
- **GET** `http://localhost:8080/api/enrollment/test`

### 3. Health Check
- **GET** `http://localhost:8080/api/enrollment/health`
