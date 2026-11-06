package com.pmdm.lab.kotlin.modelo

/** Los únicos estados en los que puede estar una descarga. */
sealed interface EstadoDescarga {
    data object Pendiente : EstadoDescarga
    data class Descargando(val progreso: Int) : EstadoDescarga
    data class Completada(val ruta: String) : EstadoDescarga
    data class Fallida(val motivo: String) : EstadoDescarga
}

/** Un fichero de la cola de descargas. */
data class Descarga(
    val id: Int,
    val nombre: String,
    val tamanoMb: Int,
    val estado: EstadoDescarga = EstadoDescarga.Pendiente,
)
