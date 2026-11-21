@echo off
setlocal
cd /d "%~dp0"

echo ==============================================
echo Compilando microservicios CORPALIV (Java 21)
echo ==============================================

for %%M in (api-gateway auth-service contenidos-service vinculacion-service) do (
    echo.
    echo Compilando %%M...
    pushd %%M
    call mvnw.cmd -B -s ..\configuracion-maven\settings.xml clean package -DskipTests
    if errorlevel 1 (
        popd
        echo ERROR al compilar %%M.
        exit /b 1
    )
    popd
)

echo.
echo Compilacion finalizada correctamente.
exit /b 0
