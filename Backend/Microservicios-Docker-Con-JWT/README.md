# Backend CORPALIV / AlbaLab

Este backend complementa la aplicación Android AlbaLab. La organización de los microservicios sigue el patrón de los ejemplos del profesor `Microservicios-Docker-Con-JWT`: `Controller`, `Service`, `Repository`, `Model/Entity`, DTO, Spring Data JPA, JWT en el API Gateway, Dockerfile por servicio y Docker Compose.

## Arquitectura

- `api-gateway` — entrada HTTP y validación JWT. Puerto público 9090.
- `auth-service` — autenticación de gestores y emisión de JWT. Puerto interno 9094.
- `contenidos-service` — programas, productos/servicios, historias e indicadores. Puerto interno 9091.
- `vinculacion-service` — solicitudes de colaboración, estados e historial. Puerto interno 9092.
- `mysql` — una instancia MySQL con tres bases de datos separadas: `bd_corpaliv_auth`, `bd_corpaliv_contenidos` y `bd_corpaliv_vinculacion`.

No se incorporaron los microservicios de compras y pagos del ejemplo del profesor porque no pertenecen al alcance del MVP CORPALIV definido para esta aplicación.

## Versiones

Se unificaron los módulos en Java 21, Spring Boot 3.3.5 y Spring Cloud 2023.0.3 para evitar la mezcla de versiones que aparece en los POM del material docente (el Gateway usa Boot 3.3.5 / Cloud 2023.0.3, mientras otros módulos del ZIP Mirror Maven usan Boot 4.0.5 / Cloud 2025.1.1). Se mantiene el enfoque de capas del profesor. El API Gateway es reactivo como en el ejemplo docente.

## Requisitos en Windows

- Docker Desktop iniciado y con Docker Compose.
- JDK 21 disponible en `PATH` (`java -version`).
- Conexión a internet la primera vez para descargar Maven y las dependencias.

## Iniciar por primera vez

Abre PowerShell en esta carpeta (`Backend\Microservicios-Docker-Con-JWT`) y ejecuta:

```powershell
Copy-Item .env.example .env
.\start-backend.bat
```

`start-backend.bat` compila los cuatro microservicios mediante sus Maven Wrappers y el `settings.xml` del Mirror Maven del profesor. Después inicia MySQL y los servicios con Docker Compose.

Para revisar los contenedores:

```powershell
docker compose ps
```

Ver los registros de inicio:

```powershell
docker compose logs -f
```

Detener sin borrar los datos de MySQL:

```powershell
.\stop-backend.bat
```

Para borrar el volumen y todos los datos de prueba, ejecutar `reset-database.bat` y confirmar con `SI`. No usarlo si se necesitan conservar los registros.

## Puertos

| Servicio | Puerto interno | Puerto del computador |
|---|---:|---:|
| API Gateway | 9090 | 9090 |
| Auth | 9094 | Solo red Docker |
| Contenidos | 9091 | Solo red Docker |
| Vinculación | 9092 | Solo red Docker |
| MySQL | 3306 | 3308 |

MySQL se publica en 3308 para evitar el conflicto que hubo con el puerto 3307 y con instalaciones locales. Dentro de Docker, los microservicios se conectan a `mysql:3306`.

## Comprobar endpoints públicos

Después de que los servicios terminen de iniciar:

```powershell
Invoke-RestMethod -Uri "http://localhost:9090/contenidos/programas" -Method Get
Invoke-RestMethod -Uri "http://localhost:9090/contenidos/productos" -Method Get
```

Crear una solicitud de prueba (correo obligatorio, teléfono opcional):

```powershell
$solicitud = @{
    nombreContacto = "Persona de Prueba"
    organizacion = "Empresa Ejemplo"
    email = "prueba@example.com"
    telefono = $null
    tipoColaboracion = "VOLUNTARIADO"
    programaId = $null
    productoServicioId = $null
    mensaje = "Me interesa recibir informacion para colaborar con CORPALIV."
    aceptaPrivacidad = $true
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:9090/vinculacion/solicitudes" `
    -Method Post -ContentType "application/json" -Body $solicitud
```

El `POST /vinculacion/solicitudes` es público para que pueda enviarlo la aplicación móvil. El servidor valida nombre, correo, formato de correo, tipo de colaboración, mensaje y aceptación de privacidad. El teléfono es opcional.

## Autenticación de gestor

El gestor de demostración se crea automáticamente si no existe:

- Correo: `gestor@albalab.cl`
- Clave: `GestorDemo2026!`

Estas credenciales son solo para desarrollo académico. Se pueden cambiar en `.env` antes de levantar el backend.

Iniciar sesión:

```powershell
$login = @{ email = "gestor@albalab.cl"; password = "GestorDemo2026!" } | ConvertTo-Json
$auth = Invoke-RestMethod -Uri "http://localhost:9090/auth/login" -Method Post -ContentType "application/json" -Body $login
$headers = @{ Authorization = "Bearer $($auth.token)" }
```

Operaciones de gestor protegidas con JWT:

- `GET /vinculacion/solicitudes`
- `GET /vinculacion/solicitudes/{id}`
- `GET /vinculacion/solicitudes/{id}/historial`
- `PATCH /vinculacion/solicitudes/{id}/estado`

Estados permitidos: `RECIBIDA`, `EN_REVISION`, `CONTACTADO`, `CONCRETADO`, `DESCARTADO`. Cada cambio crea un registro en `historial_solicitud`.

El endpoint `GET /vinculacion/solicitudes/{id}/estado` es público y devuelve solo identificador, estado y fechas, no datos personales.

## Prueba integral

Con el backend activo, ejecuta:

```powershell
.\probar-backend.ps1
```

El script consulta contenidos, crea una solicitud, inicia sesión como gestor, lista solicitudes, cambia el estado y consulta el estado público.

## Conexión desde Android

En `Android/AlbaLab`, `RetrofitInstance.kt` usa la dirección:

```kotlin
private const val BASE_URL = "http://10.0.2.2:9090/"
```

`10.0.2.2` permite que el emulador Android acceda al computador. En un teléfono físico, reemplaza la dirección por la IPv4 local del computador y conecta ambos dispositivos a la misma red Wi-Fi.

La aplicación móvil sigue este flujo:

1. Guarda una copia de la solicitud en Room con estado `PENDIENTE`.
2. Envía el `POST` al API Gateway mediante Retrofit.
3. Si el servidor responde, guarda el ID y estado remoto y marca el registro `SINCRONIZADA`.
4. Si la conexión falla, conserva el registro local para reintentar desde `Registros`.
5. La confirmación animada central de Android aparece solo cuando el backend confirma el envío.

Los archivos seleccionados en el formulario se conservan como URI local; este MVP no los sube al servidor.

## Datos de ejemplo

El changelog incluye datos sintéticos para comprobar las rutas. Las historias e indicadores de prueba están marcados como no publicados y no se presentan como resultados reales. Antes de una demostración institucional, reemplazar estos datos por contenidos aprobados por CORPALIV.

## Alcance

- La aplicación Android cubre el flujo del visitante/colaborador.
- La autenticación JWT y las operaciones del gestor se pueden probar con PowerShell o Postman.
- No hay panel visual de gestor dentro de Android en este MVP.
- Se registra una modalidad de colaboración por solicitud, siguiendo la relación singular `tipo_colaboracion_id` del modelo de datos utilizado.
- No se implementan pagos en línea.
