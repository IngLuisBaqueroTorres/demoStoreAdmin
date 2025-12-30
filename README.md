# demostore-admin (Backend)

Breve guía para ejecutar el backend **con y sin Docker**.

---

## 🧰 Requisitos
- JDK 21 (recomendado) o superior
- Maven 3.x
- MySQL (opcional para producción) — por defecto la app usa: `jdbc:mysql://localhost:3306/store_db`

> En desarrollo también puedes usar H2 (ya incluido) si no quieres configurar MySQL.

---

## 🚀 Ejecutar local (sin Docker)
1. Configura la BD (opcional): crea la base `store_db` en MySQL o usa H2.
2. Ajusta `src/main/resources/application.yml` o exporta variables de entorno si necesitas cambiar credenciales:
   - `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
   - `GOOGLE_CLIENT_ID` (si usas Google auth)
3. Ejecuta:

```bash
# compilar
mvn clean package

# ejecutar el JAR
java -jar target/demostore-0.0.1-SNAPSHOT.jar

# o en desarrollo
./mvnw spring-boot:run
```

Por defecto la aplicación escucha en el puerto `8080`.

---

## 🐳 Ejecutar con Docker
### Construir la imagen
```bash
docker build -t demostore-admin .
```

### Ejecutar (ejemplo simple)
```bash
docker run -d --name demostore-admin -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/store_db?useUnicode=true&characterEncoding=utf8" \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=1A2b3c4d! \
  -e GOOGLE_CLIENT_ID=YOUR_GOOGLE_CLIENT_ID.apps.googleusercontent.com \
  demostore-admin
```
> Nota: en Windows, para conectar desde el contenedor a una BD que corre en el host local, `host.docker.internal` suele funcionar.

### Ejemplo con MySQL (docker-compose)
```yaml
version: '3.8'
services:
  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: example
      MYSQL_DATABASE: store_db
    ports:
      - '3306:3306'
  admin:
    build: .
    ports:
      - '8080:8080'
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/store_db?useUnicode=true&characterEncoding=utf8
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: example
```

---

## ⚠️ Notas importantes
- CORS: `application.yml` permite por defecto `http://localhost:3000` y `http://localhost:5173`. Asegúrate de que el origen del frontend esté incluido.
- Cambiar puerto: exporta `SERVER_PORT` o `SPRING_SERVER_PORT` si quieres usar otro puerto.
- Logs: los logs aparecen en consola; usa `docker logs -f demostore-admin` para verlos cuando corres con Docker.


