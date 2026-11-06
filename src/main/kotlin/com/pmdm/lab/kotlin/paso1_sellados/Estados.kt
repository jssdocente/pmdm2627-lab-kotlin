package com.pmdm.lab.kotlin.paso1_sellados

import com.pmdm.lab.kotlin.modelo.EstadoDescarga

/**
 * Devuelve el texto para el usuario:
 * "En cola", "Descargando 40 %", "Guardada en /ruta" o "Error: motivo".
 */
fun describir(estado: EstadoDescarga): String =
    TODO("Pendiente de implementar")

/** Una descarga ha terminado si se ha completado o ha fallado. */
fun EstadoDescarga.estaTerminada(): Boolean =
    TODO("Pendiente de implementar")

/**
 * Calcula el estado siguiente al recibir [incremento] puntos de progreso:
 * - Pendiente pasa a Descargando(incremento).
 * - Descargando suma el incremento; al llegar a 100 o más pasa a Completada(ruta).
 * - Una descarga terminada no cambia.
 */
fun avanzar(estado: EstadoDescarga, incremento: Int, ruta: String): EstadoDescarga =
    TODO("Pendiente de implementar")
