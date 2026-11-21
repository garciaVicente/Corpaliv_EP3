package com.example.corpaliv.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes")
data class SolicitudEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    @ColumnInfo(defaultValue = "''") val telefono: String = "",
    val organizacion: String,
    val tipo: String,
    val mensaje: String,
    val archivoUri: String,
    val archivoNombre: String,
    @ColumnInfo(defaultValue = "0") val aceptaPrivacidad: Boolean = false,
    @ColumnInfo(defaultValue = "'PENDIENTE'") val estadoSincronizacion: String = "PENDIENTE",
    val idRemoto: Long? = null,
    @ColumnInfo(defaultValue = "''") val estadoRemoto: String = "",
    val fechaRegistro: Long = System.currentTimeMillis()
)

@Entity(tableName = "actividades")
data class ActividadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val fecha: String,
    val participantes: Int,
    val descripcion: String,
    val realizada: Boolean,
    val recordatorio: Boolean
)

@Entity(tableName = "contenidos_cache", primaryKeys = ["tipo", "idRemoto"])
data class ContenidoCacheEntity(
    val tipo: String,
    val idRemoto: Long,
    val titulo: String,
    val resumen: String,
    val descripcion: String,
    val categoria: String
)
