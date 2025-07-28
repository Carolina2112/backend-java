@echo off
echo ========================================
echo    SISTEMA DE MICROSERVICIOS BANCARIOS
echo ========================================
echo.

echo Verificando Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java no está instalado o no está en el PATH
    echo Por favor instala Java 17 y configura JAVA_HOME
    pause
    exit /b 1
)

echo Java encontrado. Verificando puertos...
echo.

REM Verificar puertos en uso
echo Verificando puertos disponibles...
netstat -ano | findstr :8761 >nul 2>&1
if not errorlevel 1 (
    echo ERROR: Puerto 8761 (Eureka) ya está en uso
    echo Por favor detén el proceso que usa este puerto
    pause
    exit /b 1
)

netstat -ano | findstr :8081 >nul 2>&1
if not errorlevel 1 (
    echo ERROR: Puerto 8081 (Client Service) ya está en uso
    echo Por favor detén el proceso que usa este puerto
    pause
    exit /b 1
)

netstat -ano | findstr :8082 >nul 2>&1
if not errorlevel 1 (
    echo ERROR: Puerto 8082 (Account Service) ya está en uso
    echo Por favor detén el proceso que usa este puerto
    pause
    exit /b 1
)

netstat -ano | findstr :8080 >nul 2>&1
if not errorlevel 1 (
    echo ERROR: Puerto 8080 (Gateway) ya está en uso
    echo Por favor detén el proceso que usa este puerto
    pause
    exit /b 1
)

echo Todos los puertos están disponibles.
echo.

echo ========================================
echo INICIANDO SERVICIOS EN ORDEN...
echo ========================================
echo.

echo [1/4] Iniciando Eureka Server (Puerto 8761)...
start "Eureka Server" cmd /k "cd /d %~dp0eureka-server && gradlew.bat bootRun"
timeout /t 10 /nobreak >nul

echo [2/4] Iniciando Client Service (Puerto 8081)...
start "Client Service" cmd /k "cd /d %~dp0client-person-service && gradlew.bat bootRun"
timeout /t 15 /nobreak >nul

echo [3/4] Iniciando Account Service (Puerto 8082)...
start "Account Service" cmd /k "cd /d %~dp0account-transaction-service && gradlew.bat bootRun"
timeout /t 15 /nobreak >nul

echo [4/4] Iniciando Gateway Service (Puerto 8080)...
start "Gateway Service" cmd /k "cd /d %~dp0gateway-service && gradlew.bat bootRun"
timeout /t 15 /nobreak >nul

echo.
echo ========================================
echo TODOS LOS SERVICIOS INICIADOS
echo ========================================
echo.
echo URLs de acceso:
echo - Eureka Dashboard: http://localhost:8761
echo - API Gateway: http://localhost:8080
echo - Client Service: http://localhost:8081
echo - Account Service: http://localhost:8082
echo.
echo Health Checks:
echo - Gateway: http://localhost:8080/health
echo - Client: http://localhost:8081/health
echo - Account: http://localhost:8082/health
echo.
echo Bases de datos H2:
echo - Client DB: http://localhost:8081/h2-console
echo - Account DB: http://localhost:8082/h2-console
echo.
echo Presiona cualquier tecla para abrir Eureka Dashboard...
pause >nul
start http://localhost:8761

echo.
echo Para detener todos los servicios, cierra las ventanas de comandos
echo o ejecuta: taskkill /f /im java.exe
echo.
pause 