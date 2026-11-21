package com.example.corpaliv.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.corpaliv.data.local.ContenidoCacheEntity
import com.example.corpaliv.model.Contenido
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ContenidoRemoteScreen(
    titulo: String,
    contenidos: StateFlow<List<ContenidoCacheEntity>>,
    respaldoLocal: List<Contenido>,
    onActualizar: () -> Unit
) {
    val datos by contenidos.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        
        if (datos.isEmpty()) {
            Text("No hay contenido publicado en el servidor. Se muestra la informacion local disponible.",
                style = MaterialTheme.typography.bodySmall)
            respaldoLocal.forEach { item ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(item.descripcion)
                        if (item.detalle.isNotBlank()) Text(item.detalle, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        } else {
            Text("Contenido recibido del backend y guardado en Room.", style = MaterialTheme.typography.bodySmall)
            datos.forEach { item ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.titulo, style = MaterialTheme.typography.titleMedium)
                        if (item.resumen.isNotBlank()) Text(item.resumen)
                        if (item.descripcion.isNotBlank()) Text(item.descripcion, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}


@Composable
fun ImpactoRemoteScreen(
    historias: StateFlow<List<ContenidoCacheEntity>>,
    indicadores: StateFlow<List<ContenidoCacheEntity>>,
    respaldoHistorias: List<Contenido>,
    onActualizar: () -> Unit
) {
    val listaHistorias by historias.collectAsState()
    val listaIndicadores by indicadores.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {


        Text("Historias de impacto", style = MaterialTheme.typography.titleLarge)
        if (listaHistorias.isEmpty()) {
            Text("No hay historias autorizadas publicadas en el servidor. Se muestra informacion general local; no son casos individuales.",
                style = MaterialTheme.typography.bodySmall)
            respaldoHistorias.forEach { item ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(item.descripcion)
                        if (item.detalle.isNotBlank()) Text(item.detalle, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        } else {
            listaHistorias.forEach { item ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(item.resumen)
                        Text(item.descripcion, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Text("Indicadores de impacto", style = MaterialTheme.typography.titleLarge)
        if (listaIndicadores.isEmpty()) {
            Text("No hay indicadores oficiales verificados publicados.",
                style = MaterialTheme.typography.bodySmall)
        } else {
            listaIndicadores.forEach { item ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.titulo, style = MaterialTheme.typography.titleMedium)
                        if (item.resumen.isNotBlank()) Text(item.resumen)
                        if (item.descripcion.isNotBlank()) Text(item.descripcion, style = MaterialTheme.typography.bodySmall)
                        if (item.categoria.isNotBlank()) Text("Fuente: ${item.categoria}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        OutlinedButton(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=inclusion+laboral+accesibilidad"))
            runCatching { context.startActivity(intent) }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Explorar videos de inclusion")
        }
    }
}
