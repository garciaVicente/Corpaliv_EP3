package com.example.corpaliv.data

import com.example.corpaliv.data.local.*
import com.example.corpaliv.data.remote.*
import com.example.corpaliv.model.*

class SolicitudRepository(
    private val dao: SolicitudDao,
    private val api: ApiService
) {
    val solicitudes = dao.observarSolicitudes()
    val actividades = dao.observarActividades()
    val programasCache = dao.observarCache("programa")
    val productosCache = dao.observarCache("producto")
    val historiasCache = dao.observarCache("historia")
    val indicadoresCache = dao.observarCache("indicador")

    suspend fun guardarSolicitud(s: Solicitud): ResultadoGuardado {
        val local = SolicitudEntity(
            nombre = s.nombre.trim(),
            correo = s.correo.trim(),
            telefono = s.telefono.trim(),
            organizacion = s.organizacion.trim(),
            tipo = s.tipo,
            mensaje = s.mensaje.trim(),
            archivoUri = s.archivoUri,
            archivoNombre = s.archivoNombre,
            aceptaPrivacidad = s.consentimiento
        )
        val idLocal = dao.insertarSolicitud(local).toInt()

        return try {
            val respuesta = api.crearSolicitud(s.toRequestDto())
            dao.actualizarSincronizacion(idLocal, "SINCRONIZADA", respuesta.id)
            dao.actualizarEstadoRemoto(idLocal, respuesta.estado)
            ResultadoGuardado(guardadaLocalmente = true, sincronizada = true)
        } catch (_: Exception) {
            // La solicitud no se pierde: queda en Room para un reintento posterior.
            ResultadoGuardado(guardadaLocalmente = true, sincronizada = false)
        }
    }

    suspend fun sincronizarPendientes(): ResultadoSincronizacion {
        val pendientes = dao.obtenerPendientes()
        var enviadas = 0
        for (solicitud in pendientes) {
            try {
                val respuesta = api.crearSolicitud(solicitud.toRequestDto())
                dao.actualizarSincronizacion(solicitud.id, "SINCRONIZADA", respuesta.id)
                dao.actualizarEstadoRemoto(solicitud.id, respuesta.estado)
                enviadas++
            } catch (_: Exception) {
                // Se mantiene PENDIENTE para el siguiente intento.
            }
        }
        return ResultadoSincronizacion(enviadas, pendientes.size - enviadas)
    }

    suspend fun actualizarProgramas() {
        val cache = api.obtenerProgramas().map { p ->
            ContenidoCacheEntity("programa", p.id, p.nombre, p.resumen.orEmpty(),
                p.descripcion.orEmpty(), p.categoria.orEmpty())
        }
        reemplazarCache("programa", cache)
    }

    suspend fun actualizarProductos() {
        val cache = api.obtenerProductos().filter { it.disponible && it.publicado }.map { p ->
            ContenidoCacheEntity("producto", p.id, p.nombre, p.modalidadConsulta.orEmpty(),
                p.descripcion.orEmpty(), p.categoriaId?.toString().orEmpty())
        }
        reemplazarCache("producto", cache)
    }

    suspend fun actualizarHistorias() {
        val cache = api.obtenerHistorias()
            .filter { it.autorizada && it.anonimizada && it.publicada }
            .map { h ->
                ContenidoCacheEntity("historia", h.id, h.titulo, h.resumen.orEmpty(),
                    h.contenido.orEmpty(), h.fechaPublicacion.orEmpty())
            }
        reemplazarCache("historia", cache)
    }

    suspend fun actualizarIndicadores() {
        val cache = api.obtenerIndicadores().filter { it.publicado }.map { i ->
            val valor = i.valor?.toString().orEmpty()
            val unidad = i.unidad.orEmpty()
            val resumen = listOf(valor, unidad, i.periodo.orEmpty())
                .filter { it.isNotBlank() }.joinToString(" · ")
            ContenidoCacheEntity("indicador", i.id, i.nombre, resumen,
                i.descripcion.orEmpty(), i.fuente.orEmpty())
        }
        reemplazarCache("indicador", cache)
    }

    private suspend fun reemplazarCache(tipo: String, cache: List<ContenidoCacheEntity>) {
        dao.borrarCache(tipo)
        if (cache.isNotEmpty()) dao.insertarCache(cache)
    }

    suspend fun consultarEstadoRemoto(solicitud: SolicitudEntity): String {
        val id = solicitud.idRemoto ?: throw IllegalStateException("La solicitud aun no se ha sincronizado")
        val respuesta = api.consultarEstadoSolicitud(id)
        dao.actualizarEstadoRemoto(solicitud.id, respuesta.estado)
        return respuesta.estado
    }

    suspend fun guardarActividad(a: Actividad) = dao.insertarActividad(
        ActividadEntity(
            titulo = a.titulo.trim(), fecha = a.fecha,
            participantes = a.participantes.toInt(), descripcion = a.descripcion.trim(),
            realizada = a.realizada, recordatorio = a.recordatorio
        )
    )

    suspend fun eliminarSolicitud(s: SolicitudEntity) = dao.eliminarSolicitud(s)
    suspend fun eliminarActividad(a: ActividadEntity) = dao.eliminarActividad(a)
}

private fun Solicitud.toRequestDto() = SolicitudRequestDto(
    nombreContacto = nombre.trim(),
    organizacion = organizacion.trim().ifBlank { null },
    email = correo.trim().ifBlank { null },
    telefono = telefono.trim().ifBlank { null },
    tipoColaboracion = tipo,
    mensaje = mensaje.trim(),
    aceptaPrivacidad = consentimiento
)

private fun SolicitudEntity.toRequestDto() = SolicitudRequestDto(
    nombreContacto = nombre,
    organizacion = organizacion.ifBlank { null },
    email = correo.ifBlank { null },
    telefono = telefono.ifBlank { null },
    tipoColaboracion = tipo,
    mensaje = mensaje,
    aceptaPrivacidad = aceptaPrivacidad
)

data class ResultadoGuardado(
    val guardadaLocalmente: Boolean,
    val sincronizada: Boolean
)

data class ResultadoSincronizacion(
    val enviadas: Int,
    val pendientes: Int
)
