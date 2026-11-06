package com.pmdm.lab.kotlin.paso3_colecciones

import com.pmdm.lab.kotlin.modelo.Descarga
import com.pmdm.lab.kotlin.modelo.EstadoDescarga

/** Nombre corto del estado, para usarlo como clave de los mapas. Ya está hecho. */
fun nombreEstado(estado: EstadoDescarga): String = when (estado) {
    EstadoDescarga.Pendiente -> "Pendiente"
    is EstadoDescarga.Descargando -> "Descargando"
    is EstadoDescarga.Completada -> "Completada"
    is EstadoDescarga.Fallida -> "Fallida"
}

/** Nombres de las descargas completadas, en el mismo orden. */
fun nombresCompletadas(descargas: List<Descarga>): List<String> =
    TODO("Pendiente de implementar")

/** Los nombres de las [n] descargas que más ocupan, de mayor a menor. */
fun lasMasGrandes(descargas: List<Descarga>, n: Int): List<String> =
    TODO("Pendiente de implementar")

/** Un mapa para encontrar cada descarga por su id. */
fun porId(descargas: List<Descarga>): Map<Int, Descarga> =
    TODO("Pendiente de implementar")

/** Las descargas agrupadas por el nombre de su estado. */
fun agruparPorEstado(descargas: List<Descarga>): Map<String, List<Descarga>> =
    TODO("Pendiente de implementar")

/** Cuántas descargas hay en cada estado. */
fun contarPorEstado(descargas: List<Descarga>): Map<String, Int> =
    TODO("Pendiente de implementar")

/** Cuántos MB suman las descargas de cada estado. */
fun megasPorEstado(descargas: List<Descarga>): Map<String, Int> =
    TODO("Pendiente de implementar")

/**
 * Devuelve un mapa nuevo con el estado de la descarga [id] cambiado.
 * Si el id no existe, devuelve el mismo mapa. El original no se modifica.
 */
fun cambiarEstado(mapa: Map<Int, Descarga>, id: Int, nuevo: EstadoDescarga): Map<Int, Descarga> {
    TODO("Pendiente de implementar")
}

/** Devuelve un mapa nuevo sin la descarga [id]. El original no se modifica. */
fun quitar(mapa: Map<Int, Descarga>, id: Int): Map<Int, Descarga> =
    TODO("Pendiente de implementar")
