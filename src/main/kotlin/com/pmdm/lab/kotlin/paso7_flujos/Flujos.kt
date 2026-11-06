package com.pmdm.lab.kotlin.paso7_flujos

import com.pmdm.lab.kotlin.modelo.EstadoDescarga
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Flujo frío del progreso: en cada uno de los [pasos] espera [retardoMs] y
 * emite el porcentaje. Con 4 pasos emite 25, 50, 75 y 100.
 */
fun progreso(pasos: Int, retardoMs: Long): Flow<Int> =
    flow {
        for (paso in 1..pasos) {
            delay(retardoMs)
            emit(paso * 100 / pasos)
        }
    }

/** Convierte cada porcentaje en Descargando(porcentaje), o en Completada(ruta) al llegar a 100. */
fun Flow<Int>.comoEstados(ruta: String): Flow<EstadoDescarga> =
    map { porcentaje ->
        if (porcentaje >= 100) EstadoDescarga.Completada(ruta) else EstadoDescarga.Descargando(porcentaje)
    }
