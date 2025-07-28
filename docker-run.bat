@echo off
echo ========================================
echo    EJECUTANDO MICROSERVICIOS DOCKER
echo ========================================

echo.
echo Iniciando todos los servicios...
docker-compose up -d

echo.
echo ========================================
echo    SERVICIOS INICIADOS
echo ========================================
echo.
echo URLs de acceso:
echo - Eureka Dashboard: http://localhost:8761
echo - Gateway: http://localhost:8080
echo - Account Service: http://localhost:8082
echo - Client Service: http://localhost:8081
echo.
echo Para ver logs:
echo docker-compose logs -f
echo.
echo Para detener servicios:
echo docker-compose down
echo.
pause 