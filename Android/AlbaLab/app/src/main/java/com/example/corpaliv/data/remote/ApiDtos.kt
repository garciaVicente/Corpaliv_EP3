package com.example.corpaliv.data.remote

// DTO que coincide con el JSON que espera ms-vinculacion.
data class SolicitudRequestDto(
    val nombreContacto: String,
    val organizacion: String?,
    val email: String?,
    val telefono: String?,
    val tipoColaboracion: String,
    val programaId: Long? = null,
    val productoServicioId: Long? = null,
    val mensaje: String,
    val aceptaPrivacidad: Boolean
)

data class SolicitudResponseDto(
    val id: Long,
    val estado: String,
    val fechaCreacion: String
)

// Campos publicos devueltos por GET /contenidos/programas.
data class ProgramaDto(
    val id: Long,
    val nombre: String,
    val resumen: String?,
    val descripcion: String?,
    val categoria: String?,
    val imagenUrl: String?,
    val publicado: Boolean
)


data class TipoColaboracionDto(
    val id: Long,
    val codigo: String,
    val nombre: String,
    val descripcion: String?,
    val activo: Boolean
)


data class EstadoSolicitudDto(
    val id: Long,
    val estado: String,
    val fechaCreacion: String,
    val fechaActualizacion: String
)


data class ProductoServicioDto(
    val id: Long,
    val categoriaId: Long?,
    val nombre: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val modalidadConsulta: String?,
    val disponible: Boolean,
    val publicado: Boolean
)

data class HistoriaImpactoDto(
    val id: Long,
    val titulo: String,
    val resumen: String?,
    val contenido: String?,
    val imagenUrl: String?,
    val autorizada: Boolean,
    val anonimizada: Boolean,
    val publicada: Boolean,
    val fechaPublicacion: String?
)

data class IndicadorImpactoDto(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val valor: Double?,
    val unidad: String?,
    val periodo: String?,
    val fuente: String?,
    val fechaActualizacion: String?,
    val publicado: Boolean
)
