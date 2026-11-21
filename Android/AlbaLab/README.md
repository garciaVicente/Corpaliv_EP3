# AlbaLab / CORPALIV — Android

Aplicación Android desarrollada con Kotlin, Jetpack Compose, MVVM y Room. Esta versión conserva la navegación y las pantallas de AlbaLab e integra Retrofit con el backend CORPALIV.

## Abrir el proyecto

En Android Studio, abre la carpeta `Android/AlbaLab` (esta carpeta contiene `settings.gradle.kts` y `gradlew`). No abras la carpeta contenedora `CORPALIV_Proyecto_Integrado`.

Requisitos: Android Studio compatible con AGP 8.12.3, JDK 21 y Android SDK 36.

## Funcionalidades conservadas

- Inicio, programas, productos, impacto, colaboración, actividad y registros.
- Navegación con menú lateral, navegación inferior y transición Crossfade.
- Formularios y validaciones.
- Selector del sistema para PDF e imágenes.
- Invitación de contactos mediante las opciones nativas de Android.
- Registro local de actividades y solicitudes con Room.
- Filtro dinámico de solicitudes por nombre.

## Integración con el backend

Se utiliza el patrón del ejemplo Android del profesor `AppEjemplo-API-Mirror`: `ApiService` + `RetrofitInstance` + `Repository` + `ViewModel` + Compose.

- Programas, productos, historias e indicadores se consultan desde el backend y se mantienen en caché Room.
- Las solicitudes se guardan primero en Room y luego se envían a `POST /vinculacion/solicitudes`.
- El correo es obligatorio y el teléfono es opcional, tanto en el formulario como en el servidor.
- Si el servidor no está disponible, la solicitud local permanece pendiente y se puede reintentar desde Registros.
- La animación de confirmación (círculo y check dibujados progresivamente) aparece centrada cuando el backend confirma la recepción.
- El archivo adjunto seleccionado permanece local; no se transmite al backend.

## Dirección del backend

En `app/src/main/java/com/example/corpaliv/data/remote/RetrofitInstance.kt`:

- Emulador: `http://10.0.2.2:9090/`.
- Teléfono físico: `http://IP_LOCAL_DEL_PC:9090/` (misma red Wi-Fi).

El permiso `INTERNET` y `usesCleartextTraffic="true"` están configurados para la prueba académica local por HTTP. No usar tráfico sin cifrar en producción.

## Room

La base Room pasa de versión 3 a 4 para incorporar el teléfono. Se incluye `MIGRATION_3_4`, conservando solicitudes y actividades existentes. No desinstales la aplicación ni borres los datos antes de probar la migración si necesitas mantener registros locales.

## Ejecutar

1. Inicia el backend siguiendo `Backend/Microservicios-Docker-Con-JWT/README.md`.
2. Abre Android Studio con esta carpeta `AlbaLab`.
3. Sincroniza Gradle.
4. Ejecuta la app en un emulador o dispositivo físico configurando la URL según corresponda.
