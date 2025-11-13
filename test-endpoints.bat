@echo off
echo ========================================
echo  PRUEBA DE ENDPOINTS - ENROLLMENT APP
echo ========================================
echo.

echo 1. Health Check...
curl http://localhost:8080/api/enrollment/health
echo.
echo.

echo 2. Test de Matricula Rapido...
curl http://localhost:8080/api/enrollment/test
echo.
echo.

echo 3. Matricula de Estudiante Personalizada...
curl -X POST http://localhost:8080/api/enrollment/enroll -H "Content-Type: application/json" -d "{\"studentId\":\"STU999\",\"studentName\":\"Pedro Garcia\",\"studentEmail\":\"pedro@example.com\",\"courseId\":\"CRS999\",\"courseName\":\"Testing con RabbitMQ\",\"instructor\":\"Prof. Testing\"}"
echo.
echo.

echo ========================================
echo  PRUEBAS COMPLETADAS
echo ========================================
echo.
echo Para ver los emails enviados, revisa:
echo - RabbitMQ UI: http://localhost:15672 (guest/guest)
echo - Logs: docker logs -f enrollment-app
echo.
pause
