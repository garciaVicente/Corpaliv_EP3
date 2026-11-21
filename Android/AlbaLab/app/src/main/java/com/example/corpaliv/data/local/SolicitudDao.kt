package com.example.corpaliv.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {
    @Query("SELECT * FROM solicitudes ORDER BY fechaRegistro DESC")
    fun observarSolicitudes(): Flow<List<SolicitudEntity>>

    @Insert
    suspend fun insertarSolicitud(s: SolicitudEntity): Long

    @Delete
    suspend fun eliminarSolicitud(s: SolicitudEntity)

    @Query("SELECT * FROM solicitudes WHERE estadoSincronizacion = 'PENDIENTE' ORDER BY fechaRegistro ASC")
    suspend fun obtenerPendientes(): List<SolicitudEntity>

    @Query("UPDATE solicitudes SET estadoSincronizacion = :estado, idRemoto = :idRemoto WHERE id = :idLocal")
    suspend fun actualizarSincronizacion(idLocal: Int, estado: String, idRemoto: Long?)

    @Query("UPDATE solicitudes SET estadoRemoto = :estado WHERE id = :idLocal")
    suspend fun actualizarEstadoRemoto(idLocal: Int, estado: String)

    @Query("SELECT * FROM actividades ORDER BY fecha DESC")
    fun observarActividades(): Flow<List<ActividadEntity>>

    @Insert
    suspend fun insertarActividad(a: ActividadEntity)

    @Delete
    suspend fun eliminarActividad(a: ActividadEntity)

    @Query("SELECT * FROM contenidos_cache WHERE tipo = :tipo ORDER BY titulo")
    fun observarCache(tipo: String): Flow<List<ContenidoCacheEntity>>

    @Query("DELETE FROM contenidos_cache WHERE tipo = :tipo")
    suspend fun borrarCache(tipo: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCache(contenidos: List<ContenidoCacheEntity>)
}
