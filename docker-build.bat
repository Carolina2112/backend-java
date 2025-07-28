@echo off
echo ========================================
echo    CONSTRUYENDO IMAGENES DOCKER
echo ========================================

echo.
echo Construyendo Eureka Server...
cd eureka-server
call gradlew.bat build -x test
cd ..

echo.
echo Construyendo Client Person Service...
cd client-person-service
call gradlew.bat build -x test
cd ..

echo.
echo Construyendo Account Transaction Service...
cd account-transaction-service
call gradlew.bat build -x test
cd ..

echo.
echo Construyendo Gateway Service...
cd gateway-service
call gradlew.bat build -x test
cd ..

echo.
echo ========================================
echo    CONSTRUCCION COMPLETADA
echo ========================================
echo.
echo Para ejecutar los servicios:
echo docker-compose up
echo.
pause 