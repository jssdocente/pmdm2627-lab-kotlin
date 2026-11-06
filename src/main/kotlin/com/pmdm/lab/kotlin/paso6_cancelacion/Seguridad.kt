package com.pmdm.lab.kotlin.paso6_cancelacion

import com.pmdm.lab.kotlin.modelo.EstadoDescarga
import com.pmdm.lab.kotlin.modelo.Servidor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Llama a servidor.descargar(nombre) y devuelve Completada(ruta). Si falla,
 * devuelve Fallida(mensaje de la excepción). La cancelación no es un fallo:
 * una CancellationException debe volver a lanzarse.
 */
suspend fun descargaSegura(servidor: Servidor, nombre: String): EstadoDescarga =
    try {
        EstadoDescarga.Completada(servidor.descargar(nombre))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        EstadoDescarga.Fallida(e.message ?: "Error desconocido")
    }

/**
 * Igual que una descarga normal, pero si tarda más de [limiteMs]
 * devuelve Fallida("Tiempo agotado"). Usa withTimeoutOrNull.
 */
suspend fun descargaConLimite(servidor: Servidor, nombre: String, limiteMs: Long): EstadoDescarga =
    withTimeoutOrNull(limiteMs) {
        EstadoDescarga.Completada(servidor.descargar(nombre))
    } ?: EstadoDescarga.Fallida("Tiempo agotado")

/**
 * Ejecuta [bloque] hasta [veces] veces y devuelve su primer resultado correcto.
 * Si falla la última vez, deja salir la excepción. La cancelación no se reintenta.
 */
suspend fun <T> conReintentos(veces: Int, bloque: suspend () -> T): T {
    repeat(veces - 1) {
        try {
            return bloque()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Se reintenta
        }
    }
    return bloque()
}

/**
 * Lanza la descarga con launch y devuelve su Job. Avisa con [alCambiar]:
 * primero Descargando(0) y al acabar Completada(ruta). Si la cancelan, avisa con
 * Fallida("Cancelada") y vuelve a lanzar la CancellationException.
 */
fun CoroutineScope.iniciarCancelable(
    servidor: Servidor,
    nombre: String,
    alCambiar: (EstadoDescarga) -> Unit,
): Job =
    launch {
        alCambiar(EstadoDescarga.Descargando(0))
        try {
            alCambiar(EstadoDescarga.Completada(servidor.descargar(nombre)))
        } catch (e: CancellationException) {
            alCambiar(EstadoDescarga.Fallida("Cancelada"))
            throw e
        }
    }
