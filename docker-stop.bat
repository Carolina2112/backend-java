@echo off
echo ========================================
echo    DETENIENDO MICROSERVICIOS DOCKER
echo ========================================

echo.
echo Deteniendo todos los servicios...
docker-compose down

echo.
echo Limpiando contenedores e imágenes...
docker system prune -f

echo.
echo ========================================
echo    SERVICIOS DETENIDOS
echo ========================================
echo.
pause 