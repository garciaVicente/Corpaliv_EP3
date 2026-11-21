@echo off
setlocal
cd /d "%~dp0"

if not exist .env (
    copy .env.example .env >nul
    echo Se creo .env con valores de demostracion local.
)

call build-all.bat
if errorlevel 1 (
    echo No se pudo compilar. Revise Java 21 y Maven.
    exit /b 1
)

echo.
echo Iniciando MySQL y microservicios...
docker compose up --build -d
if errorlevel 1 exit /b 1

echo.
echo Backend iniciado. API Gateway: http://localhost:9090
echo Espere a que terminen de iniciar los servicios antes de probar endpoints.
docker compose ps
