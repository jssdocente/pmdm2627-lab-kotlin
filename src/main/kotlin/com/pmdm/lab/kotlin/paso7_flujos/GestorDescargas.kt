package com.pmdm.lab.kotlin.paso7_flujos

import com.pmdm.lab.kotlin.modelo.Descarga
import com.pmdm.lab.kotlin.modelo.EstadoDescarga
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Guarda el estado de la cola en un StateFlow, como hará un ViewModel. */
class GestorDescargas(
    private val scope: CoroutineScope,
    private val pasos: Int = 5,
    private val retardoMs: Long = 400,
) {
    private val _descargas = MutableStateFlow<Map<Int, Descarga>>(emptyMap())
    val descargas: StateFlow<Map<Int, Descarga>> = _descargas.asStateFlow()

    /** Añade [descarga] al mapa usando su id como clave. Usa update. */
    fun agregar(descarga: Descarga) {
        _descargas.update { it + (descarga.id to descarga) }
    }

    /** Cambia el estado de la descarga [id]. Si el id no existe, no hace nada. Usa update. */
    fun cambiarEstado(id: Int, nuevo: EstadoDescarga) {
        _descargas.update { mapa ->
            val descarga = mapa[id] ?: return@update mapa
            mapa + (id to descarga.copy(estado = nuevo))
        }
    }

    /**
     * Lanza en [scope] una corrutina que recoge progreso(pasos, retardoMs)
     * convertido con comoEstados("/descargas/nombre") y cambia el estado con cada
     * valor. Si el id no existe, devuelve un Job sin hacer nada: Job().apply { complete() }.
     */
    fun iniciar(id: Int): Job {
        val descarga = _descargas.value[id] ?: return Job().apply { complete() }
        return scope.launch {
            progreso(pasos, retardoMs)
                .comoEstados("/descargas/${descarga.nombre}")
                .collect { estado -> cambiarEstado(id, estado) }
        }
    }

    /** Flujo con el número de descargas en estado Descargando. Deriva de [descargas] con map. */
    fun activas(): Flow<Int> =
        descargas.map { mapa -> mapa.values.count { it.estado is EstadoDescarga.Descargando } }
}
