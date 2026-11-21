@echo off
cd /d "%~dp0"
echo ATENCION: esto elimina el volumen local de MySQL y todos los registros de prueba.
set /p CONFIRMAR=Escriba SI para continuar: 
if /I not "%CONFIRMAR%"=="SI" exit /b 0
docker compose down -v
echo Base de datos de prueba eliminada. Inicie nuevamente con start-backend.bat.
