package com.pmdm.lab.kotlin.paso5_suspend

import com.pmdm.lab.kotlin.modelo.Servidor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * La tarea de descargarConCallbacks, en línea recta: pide permiso con el
 * [nombre], obtiene el enlace con el token, descarga con el enlace y devuelve la ruta.
 */
suspend fun descargarUna(servidor: Servidor, nombre: String): String {
    TODO("Pendiente de implementar")
}

/**
 * Descarga todos los [nombres] a la vez y devuelve las rutas en el mismo orden.
 * Usa coroutineScope, async y awaitAll.
 */
suspend fun descargarVarias(servidor: Servidor, nombres: List<String>): List<String> =
    TODO("Pendiente de implementar")

/**
 * Lanza la descarga con launch, sin esperarla, y llama a [alTerminar] con la ruta.
 * Devuelve el Job de la corrutina.
 */
fun CoroutineScope.descargarEnSegundoPlano(
    servidor: Servidor,
    nombre: String,
    alTerminar: (String) -> Unit,
): Job =
    TODO("Pendiente de implementar")
