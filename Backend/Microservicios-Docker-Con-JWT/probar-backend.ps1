$ErrorActionPreference = "Stop"
$base = "http://localhost:9090"

Write-Host "1. Consultando programas publicados..." -ForegroundColor Cyan
$programas = Invoke-RestMethod -Uri "$base/contenidos/programas" -Method Get
Write-Host "Programas recibidos: $($programas.Count)"

Write-Host "2. Consultando productos y servicios..." -ForegroundColor Cyan
$productos = Invoke-RestMethod -Uri "$base/contenidos/productos" -Method Get
Write-Host "Productos recibidos: $($productos.Count)"

Write-Host "3. Creando solicitud de prueba..." -ForegroundColor Cyan
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

$nueva = Invoke-RestMethod -Uri "$base/vinculacion/solicitudes" -Method Post -ContentType "application/json; charset=utf-8" -Body $solicitud
Write-Host "Solicitud creada. ID: $($nueva.id) | Estado: $($nueva.estado)" -ForegroundColor Green

Write-Host "4. Iniciando sesion de gestor..." -ForegroundColor Cyan
$login = @{
    email = "gestor@albalab.cl"
    password = "GestorDemo2026!"
} | ConvertTo-Json
$auth = Invoke-RestMethod -Uri "$base/auth/login" -Method Post -ContentType "application/json" -Body $login
$headers = @{ Authorization = "Bearer $($auth.token)" }
Write-Host "Token obtenido para el rol: $($auth.rol)" -ForegroundColor Green

Write-Host "5. Listando solicitudes (requiere JWT)..." -ForegroundColor Cyan
$lista = Invoke-RestMethod -Uri "$base/vinculacion/solicitudes" -Headers $headers -Method Get
Write-Host "Solicitudes registradas: $($lista.Count)"

Write-Host "6. Cambiando estado de la nueva solicitud..." -ForegroundColor Cyan
$cambio = @{
    estado = "EN_REVISION"
    observacion = "Revision inicial de prueba"
} | ConvertTo-Json
$actualizada = Invoke-RestMethod -Uri "$base/vinculacion/solicitudes/$($nueva.id)/estado" -Headers $headers -Method Patch -ContentType "application/json" -Body $cambio
Write-Host "Nuevo estado: $($actualizada.estado)" -ForegroundColor Green

Write-Host "7. Consultando estado publico..." -ForegroundColor Cyan
$estado = Invoke-RestMethod -Uri "$base/vinculacion/solicitudes/$($nueva.id)/estado" -Method Get
$estado | Format-List

Write-Host "Prueba completada." -ForegroundColor Green
