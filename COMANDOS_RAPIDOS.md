# Comandos Rápidos - Sistema de Microservicios

## Orden de Ejecución OBLIGATORIO
1. Eureka Server (8761)
2. Client Service (8081)
3. Account Service (8082)
4. Gateway Service (8080)

---

## 1. Eureka Server (Puerto 8761)

```bash
cd backend-java-main/eureka-server
./gradlew bootRun
```

Verificar: http://localhost:8761

---

## 2. Client Service (Puerto 8081)

```bash
cd backend-java-main/client-person-service
./gradlew bootRun
```

Verificar: http://localhost:8081/health

---

## 3. Account Service (Puerto 8082)

```bash
cd backend-java-main/account-transaction-service
./gradlew bootRun
```

Verificar: http://localhost:8082/health

---

## 4. Gateway Service (Puerto 8080)

```bash
cd backend-java-main/gateway-service
./gradlew bootRun
```

Verificar: http://localhost:8080/health

---

## Docker (Alternativa)

```bash
# Construir todo
./docker-build.bat

# Ejecutar todo
./docker-run.bat

# Detener todo
./docker-stop.bat
```

---

## Tests

```bash
# Todos los tests
./gradlew test

# Tests específicos
cd client-person-service && ./gradlew test
cd account-transaction-service && ./gradlew test
```

---

## Verificaciones Rápidas

```bash
# Health checks
curl http://localhost:8080/health
curl http://localhost:8081/health
curl http://localhost:8082/health

# Eureka dashboard
curl http://localhost:8761
```

---

## Solución de Problemas

```bash
# Verificar puertos en uso (Windows)
netstat -ano | findstr :8080
netstat -ano | findstr :8081
netstat -ano | findstr :8082
netstat -ano | findstr :8761

# Matar proceso (Windows)
taskkill /PID <PID> /F

# Verificar Java
java -version
```

---

## URLs Importantes

| Servicio | URL | Descripción |
|----------|-----|-------------|
| Eureka | http://localhost:8761 | Dashboard de servicios |
| Gateway | http://localhost:8080 | API principal |
| Client DB | http://localhost:8081/h2-console | Base de datos clientes |
| Account DB | http://localhost:8082/h2-console | Base de datos cuentas |

Configuración H2:
- JDBC URL: jdbc:h2:mem:clientdb (client) / jdbc:h2:mem:accountdb (account)
- Username: sa
- Password: password

---

## Notas Importantes

- Siempre ejecutar en el orden especificado
- Verificar que cada servicio esté funcionando antes de continuar
- Si un servicio falla, reiniciar desde el principio
- Los puertos deben estar libres antes de ejecutar 