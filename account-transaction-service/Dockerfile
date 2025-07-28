# Dockerfile para Account Transaction Service
# Usar imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim

# Información del contenedor
LABEL maintainer="JavaReto Team"
LABEL description="Account and Transaction Management Service"

# Crear directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR del proyecto
COPY build/libs/*.jar app.jar

# Exponer puerto
EXPOSE 8082

# Configurar variables de entorno
ENV JAVA_OPTS="-Xmx1024m -Xms512m"

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 