@echo off
cd /d "%~dp0"
docker compose down
echo Servicios detenidos. Los datos de MySQL se conservaron.
