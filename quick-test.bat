@echo off
chcp 65001 > nul
echo ========================================
echo  PRUEBA RAPIDA - SIMULACION RABBITMQ
echo ========================================
echo.

echo 1. Verificando sistema...
curl -s http://localhost:8080/api/enrollment/info
echo.
echo.

echo 2. Simulando 7 matriculas al curso CRS001...
echo.
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS001\",\"numberOfStudents\":7}"
echo.
echo.

echo 3. Esperando 3 segundos...
timeout /t 3 /nobreak > nul

echo.
echo 4. Simulando 8 matriculas al curso CRS002...
echo.
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS002\",\"numberOfStudents\":8}"
echo.
echo.

echo ========================================
echo  PRUEBAS COMPLETADAS
echo ========================================
echo.
echo Se han encolado 15 matriculas en total (7 + 8)
echo.
echo Para ver el procesamiento:
echo.
echo 1. Logs:
echo    docker logs -f enrollment-app
echo.
echo 2. RabbitMQ UI:
echo    http://localhost:15672 (guest/guest)
echo.
echo 3. Ver cursos:
echo    curl http://localhost:8080/api/enrollment/courses
echo.
pause
