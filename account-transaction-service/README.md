# Account Transaction Service

## Descripción

Microservicio para la gestión de cuentas bancarias y transacciones. Maneja operaciones CRUD para cuentas y transacciones.

## Puerto

8082

## Ejecución

```bash
./gradlew bootRun
```

## Verificación

http://localhost:8082/health

## Endpoints

### Cuentas
- GET /cuentas - Listar cuentas
- GET /cuentas/{accountNumber} - Obtener cuenta
- POST /cuentas - Crear cuenta
- PUT /cuentas/{accountNumber} - Actualizar cuenta
- DELETE /cuentas/{accountNumber} - Eliminar cuenta

### Transacciones
- GET /cuentas/{accountNumber}/movimientos - Obtener transacciones
- POST /cuentas/movimientos - Crear transacción
- GET /cuentas/reporte - Generar reporte

## Base de Datos

- H2 en memoria
- Console: http://localhost:8082/h2-console
- JDBC URL: jdbc:h2:mem:accountdb
- Username: sa
- Password: password

## Configuración

- Puerto: 8082
- Eureka Client: Habilitado
- JPA: Habilitado
- Validación: Habilitada
- Inter-service communication: Habilitada 