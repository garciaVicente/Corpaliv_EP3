package com.example.corpaliv.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [SolicitudEntity::class, ActividadEntity::class, ContenidoCacheEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun solicitudDao(): SolicitudDao

    companion object {
        @Volatile private var instancia: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE solicitudes ADD COLUMN aceptaPrivacidad INTEGER NOT NULL DEFAULT 0")
                database.execSQL("UPDATE solicitudes SET aceptaPrivacidad = 1")
                database.execSQL("ALTER TABLE solicitudes ADD COLUMN estadoSincronizacion TEXT NOT NULL DEFAULT 'PENDIENTE'")
                database.execSQL("ALTER TABLE solicitudes ADD COLUMN idRemoto INTEGER")
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS contenidos_cache (
                        tipo TEXT NOT NULL,
                        idRemoto INTEGER NOT NULL,
                        titulo TEXT NOT NULL,
                        resumen TEXT NOT NULL,
                        descripcion TEXT NOT NULL,
                        categoria TEXT NOT NULL,
                        PRIMARY KEY(tipo, idRemoto)
                    )
                """.trimIndent())
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE solicitudes ADD COLUMN estadoRemoto TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE solicitudes ADD COLUMN telefono TEXT NOT NULL DEFAULT ''")
            }
        }

        fun obtener(context: Context): AppDatabase = instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "corpaliv_local.db"
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build().also { instancia = it }
        }
    }
}
