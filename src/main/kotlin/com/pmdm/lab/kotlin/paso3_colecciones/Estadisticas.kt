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
    descargas.filter { it.estado is EstadoDescarga.Completada }.map { it.nombre }

/** Los nombres de las [n] descargas que más ocupan, de mayor a menor. */
fun lasMasGrandes(descargas: List<Descarga>, n: Int): List<String> =
    descargas.sortedByDescending { it.tamanoMb }.take(n).map { it.nombre }

/** Un mapa para encontrar cada descarga por su id. */
fun porId(descargas: List<Descarga>): Map<Int, Descarga> =
    descargas.associateBy { it.id }

/** Las descargas agrupadas por el nombre de su estado. */
fun agruparPorEstado(descargas: List<Descarga>): Map<String, List<Descarga>> =
    descargas.groupBy { nombreEstado(it.estado) }

/** Cuántas descargas hay en cada estado. */
fun contarPorEstado(descargas: List<Descarga>): Map<String, Int> =
    descargas.groupingBy { nombreEstado(it.estado) }.eachCount()

/** Cuántos MB suman las descargas de cada estado. */
fun megasPorEstado(descargas: List<Descarga>): Map<String, Int> =
    agruparPorEstado(descargas).mapValues { (_, lista) -> lista.sumOf { it.tamanoMb } }

/**
 * Devuelve un mapa nuevo con el estado de la descarga [id] cambiado.
 * Si el id no existe, devuelve el mismo mapa. El original no se modifica.
 */
fun cambiarEstado(mapa: Map<Int, Descarga>, id: Int, nuevo: EstadoDescarga): Map<Int, Descarga> {
    val descarga = mapa[id] ?: return mapa
    return mapa + (id to descarga.copy(estado = nuevo))
}

/** Devuelve un mapa nuevo sin la descarga [id]. El original no se modifica. */
fun quitar(mapa: Map<Int, Descarga>, id: Int): Map<Int, Descarga> =
    mapa - id
