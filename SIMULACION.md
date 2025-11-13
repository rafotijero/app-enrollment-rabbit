# Simulador de Matriculas Masivas - RabbitMQ

## Descripcion

Este sistema permite simular multiples matriculas de estudiantes a cursos para demostrar el funcionamiento de RabbitMQ como sistema de colas de mensajes.

## Datos Precargados

- **30 estudiantes** disponibles (STU001 a STU030)
- **10 cursos** disponibles (CRS001 a CRS010)

## Endpoints Disponibles

### 1. Informacion del Sistema
```bash
GET http://localhost:8080/api/enrollment/info
```

Respuesta:
```json
{
  "status": "success",
  "totalStudentsAvailable": 30,
  "totalCourses": 10,
  "message": "Sistema de simulacion de matriculas con RabbitMQ",
  "usage": "POST /api/enrollment/simulate con {\"courseId\": \"CRS001\", \"numberOfStudents\": 7}"
}
```

### 2. Listar Cursos Disponibles
```bash
GET http://localhost:8080/api/enrollment/courses
```

### 3. Simular Matriculas Masivas
```bash
POST http://localhost:8080/api/enrollment/simulate
Content-Type: application/json

{
  "courseId": "CRS001",
  "numberOfStudents": 7
}
```

## Cursos Disponibles

| ID | Nombre | Instructor |
|----|--------|------------|
| CRS001 | Arquitectura de Software | Prof. Carlos Mendez |
| CRS002 | Bases de Datos Avanzadas | Dra. Patricia Gomez |
| CRS003 | Desarrollo Web Full Stack | Ing. Roberto Silva |
| CRS004 | Machine Learning | Dr. Alberto Ruiz |
| CRS005 | Seguridad Informatica | Ing. Miguel Herrera |
| CRS006 | Cloud Computing | Dra. Laura Diaz |
| CRS007 | DevOps y CI/CD | Ing. Fernando Castro |
| CRS008 | Microservicios | Prof. Gabriela Ortiz |
| CRS009 | Testing y QA | Ing. Ricardo Flores |
| CRS010 | Inteligencia Artificial | Dr. Antonio Rojas |

## Como Probar

### Opcion 1: Script Interactivo (Recomendado)
```bash
test-simulation.bat
```

Este script te permite:
- Ver informacion del sistema
- Listar cursos
- Matricular 5, 7, 10, 15 o 20 estudiantes
- Matricula personalizada
- Abrir RabbitMQ UI

### Opcion 2: Script Rapido
```bash
quick-test.bat
```

Ejecuta automaticamente:
- 7 matriculas al curso CRS001
- 8 matriculas al curso CRS002

### Opcion 3: Comandos Manuales

#### Matricular 5 estudiantes a Arquitectura de Software:
```bash
curl -X POST http://localhost:8080/api/enrollment/simulate \
  -H "Content-Type: application/json" \
  -d "{\"courseId\":\"CRS001\",\"numberOfStudents\":5}"
```

#### Matricular 10 estudiantes a Machine Learning:
```bash
curl -X POST http://localhost:8080/api/enrollment/simulate \
  -H "Content-Type: application/json" \
  -d "{\"courseId\":\"CRS004\",\"numberOfStudents\":10}"
```

## Observar el Funcionamiento de RabbitMQ

### 1. Logs en Tiempo Real
```bash
docker logs -f enrollment-app
```

Busca estas lineas para ver el procesamiento:
```
=== INICIANDO SIMULACION DE MATRICULAS MASIVAS ===
Enviando 7 matriculas a la cola de RabbitMQ...
[1/7] Matricula encolada: ENR-XXXX - Ana Rodriguez en Arquitectura de Software
[2/7] Matricula encolada: ENR-YYYY - Luis Martinez en Arquitectura de Software
...
```

### 2. RabbitMQ Management UI
```
URL: http://localhost:15672
Usuario: guest
Contraseña: guest
```

Que observar:
- **Queues** > `enrollment.queue`
- **Messages Ready**: Mensajes esperando procesamiento
- **Messages Unacknowledged**: Mensajes siendo procesados
- **Message rates**: Velocidad de procesamiento (msg/s)
- **Graficas**: Visualizacion del flujo de mensajes

### 3. Estadisticas de la Cola
```bash
docker exec rabbitmq rabbitmqctl list_queues
```

## Validaciones del Sistema

El endpoint `/simulate` valida:

1. **Curso existe**: Retorna error 404 si el curso no existe
2. **Cantidad valida**: Debe ser > 0 y <= 30 (estudiantes disponibles)
3. **Respuesta inmediata**: El API responde sin esperar el procesamiento

## Ejemplo de Respuesta Exitosa

```json
{
  "status": "success",
  "message": "7 matriculas encoladas exitosamente",
  "courseId": "CRS001",
  "courseName": "Arquitectura de Software",
  "instructor": "Prof. Carlos Mendez",
  "studentsEnrolled": 7,
  "enrollmentIds": [
    "ENR-705532AC",
    "ENR-7EE7DA5C",
    "ENR-A5EA9AC0",
    "ENR-FA8AADB5",
    "ENR-AF52EC80",
    "ENR-B1234567",
    "ENR-C8901234"
  ],
  "notificationStatus": "queued",
  "info": "Revisa los logs y RabbitMQ Management UI para ver el procesamiento"
}
```

## Ventajas de RabbitMQ Demostradas

1. **Desacoplamiento**: El API responde inmediatamente, el envio de emails es asincrono
2. **Escalabilidad**: Puedes agregar mas consumidores para procesar mas rapido
3. **Resiliencia**: Los mensajes no se pierden si falla el envio (retry automatico)
4. **Orden garantizado**: FIFO (First In First Out) dentro de la cola
5. **Visibilidad**: Monitoreo en tiempo real del estado de la cola

## Escenarios de Prueba Sugeridos

### Carga Ligera
```bash
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS001\",\"numberOfStudents\":3}"
```

### Carga Media
```bash
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS002\",\"numberOfStudents\":10}"
```

### Carga Alta
```bash
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS003\",\"numberOfStudents\":20}"
```

### Carga Maxima
```bash
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS004\",\"numberOfStudents\":30}"
```

### Multiples Cursos Simultaneos
```bash
# Terminal 1
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS001\",\"numberOfStudents\":5}"

# Terminal 2
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS002\",\"numberOfStudents\":7}"

# Terminal 3
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS003\",\"numberOfStudents\":10}"
```

## Troubleshooting

### Error 404 - Curso no encontrado
```json
{
  "status": "error",
  "message": "Curso no encontrado: CRS999"
}
```
**Solucion**: Usa un ID de curso valido (CRS001 a CRS010)

### Error 400 - Demasiados estudiantes
```json
{
  "status": "error",
  "message": "Solo hay 30 estudiantes disponibles"
}
```
**Solucion**: Reduce el numero de estudiantes a 30 o menos

### Limite de emails (Mailtrap)
Si ves el error "Too many emails per second", es normal. El sistema sigue funcionando correctamente con reintentos automaticos.

## Limpieza y Reinicio

Para reiniciar completamente el sistema:

```bash
# Detener y limpiar
docker-compose down -v

# Limpiar cache de Docker
docker builder prune -f

# Reconstruir y levantar
docker-compose up --build
```
