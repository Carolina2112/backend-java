

## Requisitos del Sistema

### Versiones Requeridas
- Java 17 (JDK)
- Spring Boot 3.5.0
- Spring Cloud 2024.0.0
- Gradle 8.0+ (incluido en el proyecto)

### Verificación
```bash
java -version
echo %JAVA_HOME%
```

## Estructura del Proyecto

```
backend-java-main/
├── eureka-server/           # Servidor de descubrimiento
├── gateway-service/         # API Gateway
├── client-person-service/   # Microservicio de clientes
├── account-transaction-service/ # Microservicio de cuentas
├── docker-compose.yml       # Orquestación Docker
└── README.md               # Este archivo
```

## Guía de Ejecución

### Orden de Ejecución Importante

Los servicios deben ejecutarse en este orden específico:

1. Eureka Server (Puerto 8761)
2. Client Service (Puerto 8081)
3. Account Service (Puerto 8082)
4. Gateway Service (Puerto 8080)

### Comandos de Ejecución

#### 1. Eureka Server (Puerto 8761)
```bash
cd backend-java-main/eureka-server
./gradlew bootRun
```
Verificar: http://localhost:8761

#### 2. Client Person Service (Puerto 8081)
```bash
cd backend-java-main/client-person-service
./gradlew bootRun
```
Verificar: http://localhost:8081/health

#### 3. Account Transaction Service (Puerto 8082)
```bash
cd backend-java-main/account-transaction-service
./gradlew bootRun
```
Verificar: http://localhost:8082/health

#### 4. Gateway Service (Puerto 8080)
```bash
cd backend-java-main/gateway-service
./gradlew bootRun
```
Verificar: http://localhost:8080/health

### Ejecución con Docker

```bash
# Construir y ejecutar todo el stack
./docker-build.bat
./docker-run.bat

# Detener servicios
./docker-stop.bat
```

## Configuración de Puertos

| Servicio | Puerto | URL de Acceso |
|----------|--------|---------------|
| Eureka Server | 8761 | http://localhost:8761 |
| Gateway Service | 8080 | http://localhost:8080 |
| Client Service | 8081 | http://localhost:8081 |
| Account Service | 8082 | http://localhost:8082 |

### Bases de Datos H2

**Client Service:**
- URL: http://localhost:8081/h2-console
- JDBC URL: jdbc:h2:mem:clientdb
- Username: sa
- Password: password

**Account Service:**
- URL: http://localhost:8082/h2-console
- JDBC URL: jdbc:h2:mem:accountdb
- Username: sa
- Password: password

## Endpoints de la API

### A través del Gateway (Puerto 8080)

#### Clientes
- GET /clientes - Listar todos los clientes
- GET /clientes/{id} - Obtener cliente por ID
- POST /clientes - Crear nuevo cliente
- PUT /clientes/{id} - Actualizar cliente
- DELETE /clientes/{id} - Eliminar cliente (soft delete)

#### Cuentas
- GET /cuentas - Listar todas las cuentas
- GET /cuentas/{accountNumber} - Obtener cuenta por número
- POST /cuentas - Crear nueva cuenta
- PUT /cuentas/{accountNumber} - Actualizar cuenta
- DELETE /cuentas/{accountNumber} - Eliminar cuenta (soft delete)

#### Transacciones
- GET /cuentas/{accountNumber}/movimientos - Obtener transacciones de cuenta
- POST /cuentas/movimientos - Crear nueva transacción
- GET /cuentas/reporte - Generar reporte de cuenta

### Ejemplos de Uso

#### Crear Cliente
```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "identification": "12345678",
    "name": "Juan Pérez",
    "email": "juan@email.com",
    "phone": "3001234567",
    "address": "Calle 123 #45-67"
  }'
```

#### Crear Cuenta
```bash
curl -X POST http://localhost:8080/cuentas \
  -H "Content-Type: application/json" \
  -d '{
    "type": "Saving",
    "initialBalance": 1000.00,
    "clientId": 1
  }'
```

#### Crear Transacción
```bash
curl -X POST http://localhost:8080/cuentas/movimientos \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "ACC123456",
    "type": "Depósito",
    "amount": 500.00,
    "description": "Depósito inicial"
  }'
```

## Testing

### Ejecutar Tests
```bash
# Todos los servicios
./gradlew test

# Servicio específico
cd client-person-service
./gradlew test

cd account-transaction-service
./gradlew test
```

## Solución de Problemas

### Puerto ya en uso
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F



### Error de Java
```bash
java -version
# Configurar JAVA_HOME si es necesario
```

## Scripts de Automatización

- `start-all-services.bat` - Inicia todos los servicios automáticamente
- `stop-all-services.bat` - Detiene todos los servicios
- `docker-build.bat` - Construye imágenes Docker
- `docker-run.bat` - Ejecuta servicios con Docker
- `docker-stop.bat` - Detiene servicios Docker

## Monitoreo

- Eureka Dashboard: http://localhost:8761
- Health Checks: http://localhost:8080/health, http://localhost:8081/health, http://localhost:8082/health
