# Gateway Service

## Descripción

API Gateway que actúa como punto de entrada centralizado para todos los microservicios. Enruta las peticiones a los servicios correspondientes.

## Puerto

8080

## Ejecución

```bash
./gradlew bootRun
```

## Verificación

http://localhost:8080/health

## Rutas Configuradas

- `/cuentas/**` → account-transaction-service
- `/clientes/**` → client-person-service

## Configuración

- Puerto: 8080
- Eureka Client: Habilitado
- Load Balancer: Habilitado 