# Docker - Microservicios Java Spring Boot

## Descripción

Este proyecto incluye configuración completa de Docker para containerizar todos los microservicios y ejecutarlos de manera orquestada.

## Requisitos Previos

- Docker Desktop instalado y ejecutándose
- Java 17 (solo para compilación local)
- Gradle (solo para compilación local)

## Comandos Rápidos

### Opción 1: Scripts Automatizados (Recomendado)

```bash
# 1. Construir todas las imágenes
docker-build.bat

# 2. Ejecutar todos los servicios
docker-run.bat

# 3. Detener todos los servicios
docker-stop.bat
```

### Opción 2: Comandos Manuales

```bash
# 1. Construir y ejecutar todo
docker-compose up --build

# 2. Ejecutar en segundo plano
docker-compose up -d

# 3. Ver logs
docker-compose logs -f

# 4. Detener servicios
docker-compose down
```

## Estructura de Docker

```
backend-java-main/
├── docker-compose.yml          # Orquestación de servicios
├── .dockerignore              # Archivos ignorados en build
├── docker-build.bat           # Script de construcción
├── docker-run.bat             # Script de ejecución
├── docker-stop.bat            # Script de detención
├── eureka-server/
│   └── Dockerfile             # Imagen del servidor Eureka
├── gateway-service/
│   └── Dockerfile             # Imagen del API Gateway
├── account-transaction-service/
│   └── Dockerfile             # Imagen del servicio de cuentas
└── client-person-service/
    └── Dockerfile             # Imagen del servicio de clientes
```

## URLs de Acceso

Una vez ejecutados los servicios:

| Servicio | URL | Descripción |
|----------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | Servidor de descubrimiento |
| API Gateway | http://localhost:8080 | Punto de entrada principal |
| Account Service | http://localhost:8082 | Servicio de cuentas y transacciones |
| Client Service | http://localhost:8081 | Servicio de clientes |
| H2 Console Account | http://localhost:8082/h2-console | Base de datos de cuentas |
| H2 Console Client | http://localhost:8081/h2-console | Base de datos de clientes |

## Configuración de Servicios

### Eureka Server (Puerto 8761)
- Servidor de descubrimiento de servicios
- Registra automáticamente todos los microservicios
- Dashboard web para monitoreo

### API Gateway (Puerto 8080)
- Punto de entrada centralizado
- Enruta peticiones a los microservicios correspondientes
- Rutas configuradas:
  - `/cuentas/**` → account-transaction-service
  - `/clientes/**` → client-person-service

### Account Transaction Service (Puerto 8082)
- Gestión de cuentas bancarias
- Gestión de transacciones
- Validación de saldos
- Generación de reportes

### Client Person Service (Puerto 8081)
- Gestión de clientes
- Gestión de personas
- Validaciones de identificación

## Monitoreo y Logs

### Ver logs de todos los servicios:
```bash
docker-compose logs -f
```

### Ver logs de un servicio específico:
```bash
docker-compose logs -f eureka-server
docker-compose logs -f gateway-service
docker-compose logs -f account-transaction-service
docker-compose logs -f client-person-service
```

### Ver estado de los contenedores:
```bash
docker-compose ps
```

## Testing con Docker

### Ejecutar tests en contenedores:
```bash
# Tests de account-transaction-service
docker-compose run account-transaction-service ./gradlew test

# Tests de client-person-service
docker-compose run client-person-service ./gradlew test
```

## Troubleshooting

### Problema: Servicios no se comunican
```bash
# Verificar red de Docker
docker network ls
docker network inspect backend-java-main_microservices-network
```

### Problema: Puertos ocupados
```bash
# Verificar puertos en uso
netstat -an | findstr ":8080\|:8081\|:8082\|:8761"

# Detener servicios que usen esos puertos
docker-compose down
```

### Problema: Imágenes no se construyen
```bash
# Limpiar cache de Docker
docker system prune -a

# Reconstruir sin cache
docker-compose build --no-cache
```

### Health Checks
Todos los servicios incluyen health checks que verifican:
- Disponibilidad del servicio
- Respuesta HTTP correcta
- Tiempo de respuesta adecuado

## Seguridad

### Variables de Entorno
- Configuraciones sensibles en archivos .properties
- No se incluyen credenciales en las imágenes
- Uso de perfiles Spring para diferentes entornos

### Redes
- Red aislada microservices-network
- Comunicación interna entre contenedores
- Puertos expuestos solo para acceso externo

## Notas Importantes

1. Primera ejecución: Puede tardar varios minutos en descargar las imágenes base
2. Base de datos: H2 en memoria, se reinicia al reiniciar contenedores
3. Persistencia: Para producción, considerar bases de datos persistentes
4. Escalabilidad: Los servicios están preparados para escalar horizontalmente

