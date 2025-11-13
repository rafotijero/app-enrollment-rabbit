# App Enrollment Rabbit - Sistema de Notificaciones de Matricula

Sistema de notificaciones en tiempo real para matriculas de estudiantes utilizando RabbitMQ y Spring Boot.

## Descripcion

Cuando un estudiante se matricula en un curso, el sistema envia automaticamente una notificacion por correo electronico de forma asincrona usando RabbitMQ.

## Arquitectura

```
Cliente → POST /api/enrollment/enroll → EnrollmentController
                                              ↓
                                    NotificationProducer
                                              ↓
                                    RabbitMQ (enrollment.queue)
                                              ↓
                                    NotificationConsumer
                                              ↓
                                    EmailService → Mailtrap
```

## Tecnologias

- **Spring Boot 3.5.7**
- **Java 17**
- **RabbitMQ 3.12**
- **Mailtrap** (SMTP para testing)
- **Docker & Docker Compose**

## Instalacion y Ejecucion

### Opcion 1: Docker Compose (Recomendado)

```bash
# 1. Ir al directorio del proyecto
cd app-enrollment-rabbit

# 2. Construir y levantar servicios
docker-compose up --build

# 3. La aplicacion estara disponible en:
# - API: http://localhost:8080
# - RabbitMQ Management: http://localhost:15672 (guest/guest)
```

### Opcion 2: Ejecucion Local

```bash
# 1. Levantar RabbitMQ
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# 2. Compilar y ejecutar
mvn clean install
mvn spring-boot:run
```

## Endpoints Disponibles

### 1. Matricular Estudiante (Endpoint Principal)

**POST** `http://localhost:8080/api/enrollment/enroll`

**Body (JSON):**
```json
{
  "studentId": "STU001",
  "studentName": "Maria Rodriguez",
  "studentEmail": "maria.rodriguez@example.com",
  "courseId": "CRS101",
  "courseName": "Arquitectura de Software",
  "instructor": "Dr. Carlos Mendez"
}
```

**Respuesta Exitosa (201 Created):**
```json
{
  "status": "success",
  "message": "Matricula procesada exitosamente",
  "enrollmentId": "ENR-A3F5D8C2",
  "studentName": "Maria Rodriguez",
  "courseName": "Arquitectura de Software",
  "notificationStatus": "queued"
}
```

**Comportamiento:**
- Crea una matricula inmediatamente
- Encola notificacion en RabbitMQ
- Responde al cliente sin esperar el envio del email
- El email se procesa en segundo plano

---

### 2. Test de Matricula

**GET** `http://localhost:8080/api/enrollment/test`

**Respuesta:**
```json
{
  "status": "success",
  "message": "Matricula de prueba procesada",
  "enrollmentId": "ENR-TEST-7A4B2C",
  "notificationStatus": "queued"
}
```

**Comportamiento:**
- Crea una matricula de prueba con datos predefinidos
- Util para testing rapido

---

### 3. Health Check

**GET** `http://localhost:8080/api/enrollment/health`

**Respuesta:**
```json
{
  "status": "UP",
  "service": "Enrollment Notification Service"
}
```

---

## Ejemplos de Uso con cURL

### Matricular un estudiante

```bash
curl -X POST http://localhost:8080/api/enrollment/enroll \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "STU002",
    "studentName": "Juan Perez",
    "studentEmail": "juan.perez@example.com",
    "courseId": "CRS202",
    "courseName": "Desarrollo con Spring Boot",
    "instructor": "Ing. Ana Lopez"
  }'
```

### Test rapido

```bash
curl http://localhost:8080/api/enrollment/test
```

### Health check

```bash
curl http://localhost:8080/api/enrollment/health
```

---

## Ejemplos con Postman

### Matricular Estudiante

1. Metodo: **POST**
2. URL: `http://localhost:8080/api/enrollment/enroll`
3. Headers:
   - `Content-Type: application/json`
4. Body (raw JSON):
```json
{
  "studentId": "STU003",
  "studentName": "Laura Martinez",
  "studentEmail": "laura.martinez@example.com",
  "courseId": "CRS303",
  "courseName": "Microservicios con RabbitMQ",
  "instructor": "Dr. Roberto Silva"
}
```

---

## Monitoreo de RabbitMQ

### RabbitMQ Management Console

- **URL:** http://localhost:15672
- **Usuario:** guest
- **Password:** guest

### Verificar Colas

1. Ir a **Queues** tab
2. Observar:
   - `enrollment.notification.queue` - Cola principal
   - `enrollment.notification.dlq` - Dead Letter Queue

### Metricas a Observar

- **Ready:** Mensajes esperando procesamiento
- **Unacked:** Mensajes en procesamiento
- **Total:** Total de mensajes

---

## Flujo de Notificacion

1. Cliente hace POST a `/api/enrollment/enroll`
2. Controller genera ID de matricula unico
3. Producer encola mensaje en RabbitMQ
4. Controller responde inmediatamente (HTTP 201)
5. Consumer procesa mensaje en background
6. EmailService envia correo via Mailtrap
7. Consumer confirma con ACK si exitoso
8. Si falla 3 veces → mensaje va a DLQ

---

## Ejemplo de Email Enviado

```
Estimado/a Maria Rodriguez,

Te confirmamos que has sido matriculado exitosamente en el curso:

Curso: Arquitectura de Software
Instructor: Dr. Carlos Mendez
Fecha de matricula: 09/11/2025 17:30
Codigo de matricula: ENR-A3F5D8C2

Te deseamos mucho exito en tu aprendizaje.

Saludos,
Sistema de Matriculas
```

---

## Configuracion de Mailtrap

El proyecto usa Mailtrap para testing de emails sin enviarlos a destinatarios reales.

### Ver Emails Recibidos

1. Ir a: https://mailtrap.io
2. Login con las credenciales del proyecto
3. Ver bandeja de entrada

---

## Detener Servicios

```bash
# Detener contenedores
docker-compose down

# Detener y eliminar volumenes
docker-compose down -v
```

---

## Troubleshooting

### Error: Connection refused to RabbitMQ

**Solucion:** Verifica que RabbitMQ este corriendo
```bash
docker ps | grep rabbitmq
```

### Mensajes quedan en "Ready"

**Solucion:** Verifica que el Consumer este activo
```bash
docker logs enrollment-app
```

### Ver logs de la aplicacion

```bash
docker logs -f enrollment-app
```

### Ver logs de RabbitMQ

```bash
docker logs -f enrollment-rabbitmq
```

---

## Estructura del Proyecto

```
app-enrollment-rabbit/
├── src/main/java/com/enrollment/notifications/
│   ├── config/
│   │   └── RabbitMQConfig.java
│   ├── controller/
│   │   └── EnrollmentController.java
│   ├── consumer/
│   │   └── NotificationConsumer.java
│   ├── producer/
│   │   └── NotificationProducer.java
│   ├── service/
│   │   └── EmailService.java
│   ├── model/
│   │   ├── Student.java
│   │   ├── Course.java
│   │   └── Enrollment.java
│   ├── dto/
│   │   ├── EnrollmentRequest.java
│   │   └── NotificationMessage.java
│   └── EnrollmentNotificationApplication.java
├── src/main/resources/
│   └── application.properties
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Caracteristicas

✅ **Procesamiento Asincrono** - No bloquea la respuesta HTTP
✅ **Reintentos Automaticos** - Hasta 3 intentos en caso de fallo
✅ **Dead Letter Queue** - Almacena mensajes fallidos
✅ **Docker Ready** - Desplegable con un solo comando
✅ **Healthchecks** - Monitoreo automatico de servicios
✅ **Logs Detallados** - Trazabilidad completa

---

## Resumen de Endpoints

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| POST | `/api/enrollment/enroll` | Matricular estudiante y enviar notificacion |
| GET | `/api/enrollment/test` | Prueba rapida de matricula |
| GET | `/api/enrollment/health` | Estado del servicio |

---

## Contacto y Soporte

Para mas informacion sobre RabbitMQ:
- Management Console: http://localhost:15672
- Documentacion: https://www.rabbitmq.com/documentation.html
