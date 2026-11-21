package com.example.corpaliv.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

import com.example.corpaliv.data.ResultadoGuardado
import com.example.corpaliv.model.*
import com.example.corpaliv.viewmodel.SolicitudViewModel

@Composable
fun FormularioScreen(
    tipoInicial: String,
    vm: SolicitudViewModel,
    onVolver: () -> Unit
) {
    var solicitud by remember(tipoInicial) {
        mutableStateOf(Solicitud(tipo = tipoInicial))
    }
    var errores by remember { mutableStateOf(ErroresSolicitud()) }
    var guardando by remember { mutableStateOf(false) }
    var resultado by remember { mutableStateOf("") }
    var menu by remember { mutableStateOf(false) }
    var mostrarAnimacionConfirmacion by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Selector de archivos: el adjunto permanece en el dispositivo en este MVP.
    val archivo = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                var nombre = "Archivo adjunto"
                context.contentResolver.query(
                    uri,
                    arrayOf(OpenableColumns.DISPLAY_NAME),
                    null,
                    null,
                    null
                )?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        nombre = cursor.getString(0) ?: nombre
                    }
                }

                solicitud = solicitud.copy(
                    archivoUri = uri.toString(),
                    archivoNombre = nombre
                )
            }.onFailure {
                resultado = "No fue posible seleccionar el archivo"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Solicitud de colaboración",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = solicitud.nombre,
            onValueChange = { solicitud = solicitud.copy(nombre = it) },
            label = { Text("Nombre *") },
            isError = errores.nombre != null,
            trailingIcon = { if (errores.nombre != null) Text("⚠") },
            supportingText = { errores.nombre?.let { Text(it) } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Se mantiene el correo como obligatorio, según la regla definida para AlbaLab.
        OutlinedTextField(
            value = solicitud.correo,
            onValueChange = { solicitud = solicitud.copy(correo = it) },
            label = { Text("Correo *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = errores.correo != null,
            trailingIcon = { if (errores.correo != null) Text("⚠") },
            supportingText = { errores.correo?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = solicitud.telefono,
            onValueChange = { solicitud = solicitud.copy(telefono = it) },
            label = { Text("Teléfono (opcional)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = errores.telefono != null,
            trailingIcon = { if (errores.telefono != null) Text("⚠") },
            supportingText = { errores.telefono?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = solicitud.organizacion,
            onValueChange = { solicitud = solicitud.copy(organizacion = it) },
            label = { Text("Organización (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Box {
            OutlinedButton(
                onClick = { menu = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tipo: ${solicitud.tipo}")
            }

            DropdownMenu(
                expanded = menu,
                onDismissRequest = { menu = false }
            ) {
                listOf(
                    "Donaciones",
                    "Alianzas con empresas",
                    "Voluntariado",
                    "Productos y servicios",
                    "Capacitaciones"
                ).forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text(tipo) },
                        onClick = {
                            solicitud = solicitud.copy(tipo = tipo)
                            menu = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = solicitud.mensaje,
            onValueChange = { solicitud = solicitud.copy(mensaje = it) },
            label = { Text("Mensaje *") },
            minLines = 3,
            isError = errores.mensaje != null,
            trailingIcon = { if (errores.mensaje != null) Text("⚠") },
            supportingText = { errores.mensaje?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = { archivo.launch(arrayOf("image/*", "application/pdf")) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar imagen o PDF")
        }

        if (solicitud.archivoNombre.isNotEmpty()) {
            Text("Seleccionado: ${solicitud.archivoNombre}")
            TextButton(
                onClick = {
                    solicitud = solicitud.copy(archivoUri = "", archivoNombre = "")
                }
            ) {
                Text("Eliminar archivo adjunto")
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = solicitud.consentimiento,
                onCheckedChange = {
                    solicitud = solicitud.copy(consentimiento = it)
                }
            )
            Text(
                "Acepto que mis datos se utilicen para gestionar esta solicitud.",
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        errores.consentimiento?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        AnimatedVisibility(visible = resultado.isNotBlank()) {
            Text(
                text = resultado,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            enabled = !guardando,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                errores = validarSolicitud(solicitud)
                resultado = ""
                mostrarAnimacionConfirmacion = false

                if (errores.valido()) {
                    guardando = true
                    vm.guardar(solicitud) { respuesta: ResultadoGuardado ->
                        guardando = false

                        resultado = when {
                            !respuesta.guardadaLocalmente ->
                                "No fue posible guardar la solicitud"
                            respuesta.sincronizada ->
                                "Solicitud guardada y enviada al servidor"
                            else ->
                                "Solicitud guardada en el celular; queda pendiente de sincronización"
                        }

                        if (respuesta.guardadaLocalmente) {
                            solicitud = Solicitud(tipo = tipoInicial)
                        }

                        // La animación central confirma una respuesta positiva del backend.
                        mostrarAnimacionConfirmacion = respuesta.sincronizada
                    }
                }
            }
        ) {
            Text(if (guardando) "Guardando..." else "Guardar solicitud")
        }

        TextButton(onClick = onVolver) {
            Text("Volver")
        }
    }

    // Dialog centra la confirmación en la pantalla y bloquea la interacción del formulario.
    if (mostrarAnimacionConfirmacion) {
        Dialog(
            onDismissRequest = {
                mostrarAnimacionConfirmacion = false
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimacionConfirmacion()

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "¡Solicitud registrada!",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tu solicitud fue enviada correctamente a CORPALIV.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            mostrarAnimacionConfirmacion = false
                            resultado = ""
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}

@Composable
fun AnimacionConfirmacion() {
    val progreso = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progreso.snapTo(0f)
        progreso.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            )
        )
    }

    Canvas(modifier = Modifier.size(120.dp)) {
        val grosor = size.minDimension * 0.075f
        val margen = grosor / 2f

        // Etapa 1: el círculo se dibuja desde la parte superior.
        val progresoCirculo = (progreso.value / 0.62f).coerceIn(0f, 1f)

        drawArc(
            color = Color(0xFF2E7D32),
            startAngle = -90f,
            sweepAngle = 360f * progresoCirculo,
            useCenter = false,
            topLeft = Offset(margen, margen),
            size = Size(size.width - grosor, size.height - grosor),
            style = Stroke(width = grosor, cap = StrokeCap.Round)
        )

        // Etapa 2: el check se traza después de completar el círculo.
        val progresoCheck = ((progreso.value - 0.62f) / 0.38f).coerceIn(0f, 1f)

        val inicio = Offset(size.width * 0.28f, size.height * 0.52f)
        val medio = Offset(size.width * 0.44f, size.height * 0.66f)
        val final = Offset(size.width * 0.74f, size.height * 0.36f)

        val primerTrazo = (progresoCheck * 2f).coerceIn(0f, 1f)
        if (primerTrazo > 0f) {
            drawLine(
                color = Color(0xFF2E7D32),
                start = inicio,
                end = Offset(
                    inicio.x + (medio.x - inicio.x) * primerTrazo,
                    inicio.y + (medio.y - inicio.y) * primerTrazo
                ),
                strokeWidth = grosor,
                cap = StrokeCap.Round
            )
        }

        val segundoTrazo = ((progresoCheck - 0.5f) * 2f).coerceIn(0f, 1f)
        if (segundoTrazo > 0f) {
            drawLine(
                color = Color(0xFF2E7D32),
                start = medio,
                end = Offset(
                    medio.x + (final.x - medio.x) * segundoTrazo,
                    medio.y + (final.y - medio.y) * segundoTrazo
                ),
                strokeWidth = grosor,
                cap = StrokeCap.Round
            )
        }
    }
}
