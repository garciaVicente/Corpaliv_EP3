package com.example.corpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpaliv.data.*
import com.example.corpaliv.data.local.AppDatabase
import com.example.corpaliv.data.remote.RetrofitInstance
import com.example.corpaliv.model.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SolicitudViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = SolicitudRepository(
        AppDatabase.obtener(app).solicitudDao(),
        RetrofitInstance.api
    )

    val solicitudes = repo.solicitudes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val actividades = repo.actividades.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val programasCache = repo.programasCache.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val productosCache = repo.productosCache.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val historiasCache = repo.historiasCache.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val indicadoresCache = repo.indicadoresCache.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun actualizarProgramas() {
        viewModelScope.launch { runCatching { repo.actualizarProgramas() } }
    }

    fun actualizarProductos() {
        viewModelScope.launch { runCatching { repo.actualizarProductos() } }
    }

    fun actualizarImpacto() {
        viewModelScope.launch {
            runCatching { repo.actualizarHistorias() }
            runCatching { repo.actualizarIndicadores() }
        }
    }

    fun actualizarContenidos() {
        viewModelScope.launch {
            runCatching { repo.actualizarProgramas() }
            runCatching { repo.actualizarProductos() }
            runCatching { repo.actualizarHistorias() }
            runCatching { repo.actualizarIndicadores() }
            // Si no hay conexion, Room conserva la ultima copia descargada.
        }
    }

    fun guardar(s: Solicitud, terminado: (ResultadoGuardado) -> Unit) {
        viewModelScope.launch {
            val resultado = runCatching { repo.guardarSolicitud(s) }
                .getOrElse { ResultadoGuardado(false, false) }
            terminado(resultado)
        }
    }

    fun sincronizarPendientes(terminado: (ResultadoSincronizacion) -> Unit) {
        viewModelScope.launch {
            val resultado = runCatching { repo.sincronizarPendientes() }
                .getOrElse { ResultadoSincronizacion(0, 0) }
            terminado(resultado)
        }
    }

    fun consultarEstado(idLocal: Int, terminado: (String?) -> Unit) {
        viewModelScope.launch {
            val solicitud = solicitudes.value.firstOrNull { it.id == idLocal }
            val estado = if (solicitud == null) null else runCatching {
                repo.consultarEstadoRemoto(solicitud)
            }.getOrNull()
            terminado(estado)
        }
    }

    fun guardarActividad(a: Actividad, terminado: (Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = runCatching { repo.guardarActividad(a) }.isSuccess
            terminado(ok)
        }
    }

    fun eliminar(id: Int) {
        val solicitud = solicitudes.value.firstOrNull { it.id == id } ?: return
        viewModelScope.launch { repo.eliminarSolicitud(solicitud) }
    }

    fun eliminarActividad(id: Int) {
        val actividad = actividades.value.firstOrNull { it.id == id } ?: return
        viewModelScope.launch { repo.eliminarActividad(actividad) }
    }
}
