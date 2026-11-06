package com.pmdm.lab.kotlin.paso1_sellados

import com.pmdm.lab.kotlin.modelo.EstadoDescarga

/**
 * Devuelve el texto para el usuario:
 * "En cola", "Descargando 40 %", "Guardada en /ruta" o "Error: motivo".
 */
fun describir(estado: EstadoDescarga): String =
    when (estado) {
        EstadoDescarga.Pendiente -> "En cola"
        is EstadoDescarga.Descargando -> "Descargando ${estado.progreso} %"
        is EstadoDescarga.Completada -> "Guardada en ${estado.ruta}"
        is EstadoDescarga.Fallida -> "Error: ${estado.motivo}"
    }

/** Una descarga ha terminado si se ha completado o ha fallado. */
fun EstadoDescarga.estaTerminada(): Boolean =
    this is EstadoDescarga.Completada || this is EstadoDescarga.Fallida

/**
 * Calcula el estado siguiente al recibir [incremento] puntos de progreso:
 * - Pendiente pasa a Descargando(incremento).
 * - Descargando suma el incremento; al llegar a 100 o más pasa a Completada(ruta).
 * - Una descarga terminada no cambia.
 */
fun avanzar(estado: EstadoDescarga, incremento: Int, ruta: String): EstadoDescarga =
    when (estado) {
        EstadoDescarga.Pendiente -> EstadoDescarga.Descargando(incremento)
        is EstadoDescarga.Descargando -> {
            val nuevo = estado.progreso + incremento
            if (nuevo >= 100) EstadoDescarga.Completada(ruta) else EstadoDescarga.Descargando(nuevo)
        }
        is EstadoDescarga.Completada, is EstadoDescarga.Fallida -> estado
    }
