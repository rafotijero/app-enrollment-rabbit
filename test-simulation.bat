@echo off
chcp 65001 > nul
echo ========================================
echo  SIMULADOR DE MATRICULAS MASIVAS
echo  Sistema de Colas con RabbitMQ
echo ========================================
echo.

:menu
echo.
echo MENU PRINCIPAL:
echo.
echo 1. Ver informacion del sistema
echo 2. Ver cursos disponibles
echo 3. Matricular 5 estudiantes al curso CRS001 (Arquitectura de Software)
echo 4. Matricular 7 estudiantes al curso CRS002 (Bases de Datos Avanzadas)
echo 5. Matricular 10 estudiantes al curso CRS003 (Desarrollo Web Full Stack)
echo 6. Matricular 15 estudiantes al curso CRS004 (Machine Learning)
echo 7. Matricular 20 estudiantes al curso CRS005 (Seguridad Informatica)
echo 8. Matricula personalizada (elegir curso y cantidad)
echo 9. Ver RabbitMQ Management UI
echo 0. Salir
echo.
set /p option="Selecciona una opcion: "

if "%option%"=="1" goto info
if "%option%"=="2" goto courses
if "%option%"=="3" goto sim5
if "%option%"=="4" goto sim7
if "%option%"=="5" goto sim10
if "%option%"=="6" goto sim15
if "%option%"=="7" goto sim20
if "%option%"=="8" goto custom
if "%option%"=="9" goto rabbitmq
if "%option%"=="0" goto end
goto menu

:info
echo.
echo Obteniendo informacion del sistema...
echo.
curl -s http://localhost:8080/api/enrollment/info | python -m json.tool 2>nul || curl http://localhost:8080/api/enrollment/info
echo.
pause
goto menu

:courses
echo.
echo Listando cursos disponibles...
echo.
curl -s http://localhost:8080/api/enrollment/courses | python -m json.tool 2>nul || curl http://localhost:8080/api/enrollment/courses
echo.
pause
goto menu

:sim5
echo.
echo ========================================
echo Matriculando 5 estudiantes a CRS001...
echo ========================================
echo.
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS001\",\"numberOfStudents\":5}"
echo.
echo.
echo OBSERVA LOS LOGS:
echo docker logs -f enrollment-app
echo.
pause
goto menu

:sim7
echo.
echo ========================================
echo Matriculando 7 estudiantes a CRS002...
echo ========================================
echo.
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS002\",\"numberOfStudents\":7}"
echo.
echo.
echo OBSERVA LOS LOGS:
echo docker logs -f enrollment-app
echo.
pause
goto menu

:sim10
echo.
echo ========================================
echo Matriculando 10 estudiantes a CRS003...
echo ========================================
echo.
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS003\",\"numberOfStudents\":10}"
echo.
echo.
echo OBSERVA LOS LOGS:
echo docker logs -f enrollment-app
echo.
pause
goto menu

:sim15
echo.
echo ========================================
echo Matriculando 15 estudiantes a CRS004...
echo ========================================
echo.
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS004\",\"numberOfStudents\":15}"
echo.
echo.
echo OBSERVA LOS LOGS:
echo docker logs -f enrollment-app
echo.
pause
goto menu

:sim20
echo.
echo ========================================
echo Matriculando 20 estudiantes a CRS005...
echo ========================================
echo.
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"CRS005\",\"numberOfStudents\":20}"
echo.
echo.
echo OBSERVA LOS LOGS:
echo docker logs -f enrollment-app
echo.
pause
goto menu

:custom
echo.
echo ========================================
echo  MATRICULA PERSONALIZADA
echo ========================================
echo.
echo Cursos disponibles:
echo CRS001 - Arquitectura de Software
echo CRS002 - Bases de Datos Avanzadas
echo CRS003 - Desarrollo Web Full Stack
echo CRS004 - Machine Learning
echo CRS005 - Seguridad Informatica
echo CRS006 - Cloud Computing
echo CRS007 - DevOps y CI/CD
echo CRS008 - Microservicios
echo CRS009 - Testing y QA
echo CRS010 - Inteligencia Artificial
echo.
set /p courseId="Ingresa el ID del curso (ej: CRS001): "
set /p numStudents="Ingresa la cantidad de estudiantes (max 30): "
echo.
echo Matriculando %numStudents% estudiantes al curso %courseId%...
echo.
curl -X POST http://localhost:8080/api/enrollment/simulate -H "Content-Type: application/json" -d "{\"courseId\":\"%courseId%\",\"numberOfStudents\":%numStudents%}"
echo.
echo.
pause
goto menu

:rabbitmq
echo.
echo Abriendo RabbitMQ Management UI en tu navegador...
echo URL: http://localhost:15672
echo Usuario: guest
echo Contraseña: guest
echo.
start http://localhost:15672
echo.
pause
goto menu

:end
echo.
echo ========================================
echo  TIPS PARA OBSERVAR RABBITMQ:
echo ========================================
echo.
echo 1. LOGS EN TIEMPO REAL:
echo    docker logs -f enrollment-app
echo.
echo 2. RABBITMQ MANAGEMENT UI:
echo    http://localhost:15672 (guest/guest)
echo    - Queues ^> enrollment.queue
echo    - Mira las graficas de mensajes
echo    - Observa la velocidad de procesamiento
echo.
echo 3. ESTADISTICAS DE LA COLA:
echo    docker exec rabbitmq rabbitmqctl list_queues
echo.
echo ========================================
echo  GRACIAS POR USAR EL SIMULADOR!
echo ========================================
echo.
