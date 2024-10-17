# Usa una imagen Alpine ligera con Java 17
FROM openjdk:17-jdk-alpine

# Define la carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR de la aplicación a la carpeta de trabajo del contenedor
COPY target/products-1.0.0.jar /app/products-1.0.0.jar

# Expone el puerto 8080 en el contenedor
EXPOSE 8889

# Comando CMD para ejecutar la aplicación
CMD ["java", "-jar", "products-1.0.0.jar"]


