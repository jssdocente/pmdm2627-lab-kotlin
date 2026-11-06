package com.pmdm.lab.kotlin.paso5_suspend

/*
 * Solo para leer y comparar: la misma tarea escrita con callbacks.
 * No hay que tocar este archivo.
 */
interface ServidorConCallbacks {
    fun pedirPermiso(nombre: String, alTerminar: (String) -> Unit, alFallar: (Exception) -> Unit)
    fun obtenerEnlace(token: String, alTerminar: (String) -> Unit, alFallar: (Exception) -> Unit)
    fun descargar(enlace: String, alTerminar: (String) -> Unit, alFallar: (Exception) -> Unit)
}

fun descargarConCallbacks(
    servidor: ServidorConCallbacks,
    nombre: String,
    alTerminar: (String) -> Unit,
    alFallar: (Exception) -> Unit,
) {
    servidor.pedirPermiso(nombre, { token ->
        servidor.obtenerEnlace(token, { enlace ->
            servidor.descargar(enlace, { ruta ->
                alTerminar(ruta)
            }, { e -> alFallar(e) })
        }, { e -> alFallar(e) })
    }, { e -> alFallar(e) })
}
