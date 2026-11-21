package com.example.corpaliv.data.remote

import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("contenidos/programas")
    suspend fun obtenerProgramas(): List<ProgramaDto>

    @GET("contenidos/productos")
    suspend fun obtenerProductos(): List<ProductoServicioDto>

    @GET("contenidos/historias")
    suspend fun obtenerHistorias(): List<HistoriaImpactoDto>

    @GET("contenidos/indicadores")
    suspend fun obtenerIndicadores(): List<IndicadorImpactoDto>

    @GET("vinculacion/solicitudes/tipos-colaboracion")
    suspend fun obtenerTiposColaboracion(): List<TipoColaboracionDto>

    @GET("vinculacion/solicitudes/{id}/estado")
    suspend fun consultarEstadoSolicitud(@Path("id") id: Long): EstadoSolicitudDto

    @POST("vinculacion/solicitudes")
    suspend fun crearSolicitud(@Body solicitud: SolicitudRequestDto): SolicitudResponseDto
}
