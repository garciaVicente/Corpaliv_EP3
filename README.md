# CORPALIV / AlbaLab — Proyecto integrado

Esta entrega reúne la aplicación Android existente y el backend necesario para conectarla. La estructura original de AlbaLab se conserva; los cambios se concentran en la integración Retrofit/Room, el acceso a contenidos remotos y el envío de solicitudes.

## Contenido

- `Android/AlbaLab`: proyecto Android Kotlin + Jetpack Compose + MVVM + Room + Retrofit. Ábrelo directamente en Android Studio.
- `Backend/Microservicios-Docker-Con-JWT`: cuatro proyectos Maven independientes siguiendo el estilo del material docente: API Gateway, autenticación, contenidos y vinculación. Incluye MySQL, Liquibase, JWT, Dockerfiles y Docker Compose.

## Orden para ejecutar

1. En Windows, instala/inicia Docker Desktop y asegúrate de tener JDK 21 disponible (`java -version`).
2. Abre PowerShell en `Backend\Microservicios-Docker-Con-JWT`. Ejecuta `Copy-Item .env.example .env` (solo la primera vez).
3. Ejecuta `start-backend.bat`. El script compila cada microservicio con Maven Wrapper y después inicia MySQL y los servicios con Docker Compose. La primera ejecución requiere Internet para descargar Maven y dependencias.
4. Espera a que los contenedores estén activos con `docker compose ps`. Prueba `http://localhost:9090/contenidos/programas`.
5. Abre `Android/AlbaLab` en Android Studio, sincroniza Gradle y ejecuta la aplicación en un emulador. Para el emulador, la URL ya está configurada en `http://10.0.2.2:9090/`. Para un teléfono físico, configura la IPv4 local del PC en `RetrofitInstance.kt`.


## Backend basado en los ejemplos del profesor

Se conserva el enfoque por capas `Controller → Service → Repository → Entity/Model`, Spring Data JPA, Liquibase, Gateway reactivo y JWT. Se usa un proyecto Maven independiente por microservicio, Dockerfile por módulo y Compose para orquestar los servicios. Se alinearon las versiones Spring Boot/Cloud para evitar la mezcla de versiones observada entre los ZIP docentes. No se agregaron los módulos de compra/pago del ejemplo, porque no corresponden al alcance actual de CORPALIV.

Consulta los README de Android y Backend para los puertos, endpoints, credenciales locales de demostración, pruebas y limitaciones. Los datos iniciales de contenido son sintéticos y no representan cifras ni historias institucionales verificadas.


