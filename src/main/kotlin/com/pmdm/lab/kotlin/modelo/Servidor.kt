package com.pmdm.lab.kotlin.modelo

import kotlinx.coroutines.delay
import java.io.IOException

/** Un servidor de descargas: cada llamada espera a la red sin bloquear el hilo. */
interface Servidor {
    suspend fun pedirPermiso(nombre: String): String
    suspend fun obtenerEnlace(token: String): String
    suspend fun descargar(enlace: String): String
}

/**
 * Servidor simulado: cada llamada tarda [retardoMs] y descargar un fichero
 * incluido en [fallan] lanza una IOException.
 */
class ServidorFalso(
    private val retardoMs: Long = 1000,
    private val fallan: Set<String> = emptySet(),
) : Servidor {

    override suspend fun pedirPermiso(nombre: String): String {
        delay(retardoMs)
        return "token-$nombre"
    }

    override suspend fun obtenerEnlace(token: String): String {
        delay(retardoMs)
        return "enlace-" + token.removePrefix("token-")
    }

    override suspend fun descargar(enlace: String): String {
        delay(retardoMs)
        val nombre = enlace.removePrefix("enlace-")
        if (nombre in fallan) throw IOException("No se pudo descargar $nombre")
        return "/descargas/$nombre"
    }
}
