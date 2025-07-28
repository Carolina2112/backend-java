@echo off
echo ========================================
echo    DETENIENDO TODOS LOS SERVICIOS
echo ========================================
echo.

echo Deteniendo procesos Java...
taskkill /f /im java.exe >nul 2>&1

echo Verificando que los puertos estén libres...
timeout /t 3 /nobreak >nul

echo Verificando puertos:
netstat -ano | findstr :8761 >nul 2>&1
if not errorlevel 1 (
    echo WARNING: Puerto 8761 aún está en uso
) else (
    echo ✓ Puerto 8761 (Eureka) liberado
)

netstat -ano | findstr :8081 >nul 2>&1
if not errorlevel 1 (
    echo WARNING: Puerto 8081 aún está en uso
) else (
    echo ✓ Puerto 8081 (Client Service) liberado
)

netstat -ano | findstr :8082 >nul 2>&1
if not errorlevel 1 (
    echo WARNING: Puerto 8082 aún está en uso
) else (
    echo ✓ Puerto 8082 (Account Service) liberado
)

netstat -ano | findstr :8080 >nul 2>&1
if not errorlevel 1 (
    echo WARNING: Puerto 8080 aún está en uso
) else (
    echo ✓ Puerto 8080 (Gateway) liberado
)

echo.
echo ========================================
echo SERVICIOS DETENIDOS
echo ========================================
echo.
echo Si algún puerto aún está en uso, puedes:
echo 1. Esperar unos segundos y ejecutar este script nuevamente
echo 2. Reiniciar tu computadora
echo 3. Usar el Administrador de tareas para matar procesos Java
echo.
pause 