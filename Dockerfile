# Etapa 1: Build con GraalVM
FROM ghcr.io/graalvm/native-image-community:25 AS builder

# Instalar herramientas necesarias para mvnw
RUN microdnf install findutils -y

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Descargar dependencias (cacheable)
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente y recursos (incluyendo el nuevo resource-config.json)
COPY src ./src

# Compilar la aplicación en modo nativo
# Limitamos el paralelismo y la memoria para evitar fallos en el builder de Railway
RUN ./mvnw -Pnative native:compile -DskipTests

# Etapa 2: Runtime ligero
# Usamos alpine para que la imagen final sea de apenas ~100MB
FROM alpine:latest
RUN apk add --no-cache libc6-compat
WORKDIR /app

# Copiar el binario desde la etapa de build
# El nombre coincide con el artifactId definido en pom.xml
COPY --from=builder /app/target/paqueteria-iw ./app

# Exponer el puerto de la aplicación
EXPOSE 8080
ENV PORT=8080

# Ejecutar el binario nativo directamente
ENTRYPOINT ["./app"]
