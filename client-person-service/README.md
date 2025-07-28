# Client Person Service

## Descripción

Microservicio para la gestión de clientes y personas. Maneja operaciones CRUD para clientes bancarios.

## Puerto

8081

## Ejecución

```bash
./gradlew bootRun
```

## Verificación

http://localhost:8081/health

## Endpoints

- GET /clientes - Listar clientes
- GET /clientes/{id} - Obtener cliente
- POST /clientes - Crear cliente
- PUT /clientes/{id} - Actualizar cliente
- DELETE /clientes/{id} - Eliminar cliente

## Base de Datos

- H2 en memoria
- Console: http://localhost:8081/h2-console
- JDBC URL: jdbc:h2:mem:clientdb
- Username: sa
- Password: password

## Configuración

- Puerto: 8081
- Eureka Client: Habilitado
- JPA: Habilitado
- Validación: Habilitada 