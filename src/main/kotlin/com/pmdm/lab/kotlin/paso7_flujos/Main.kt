package com.pmdm.lab.kotlin.paso7_flujos

import com.pmdm.lab.kotlin.modelo.Descarga
import com.pmdm.lab.kotlin.modelo.EstadoDescarga
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/** La recompensa del paso 7: el gestor funcionando en la consola. */
fun main() = runBlocking {
    val gestor = GestorDescargas(this)
    gestor.agregar(Descarga(1, "mapa.zip", 300))
    gestor.agregar(Descarga(2, "musica.zip", 120))

    val pantalla = launch {
        gestor.descargas.collect { mapa ->
            println(mapa.values.joinToString(" | ") { "${it.nombre}: ${texto(it.estado)}" })
        }
    }

    listOf(gestor.iniciar(1), gestor.iniciar(2)).joinAll()
    pantalla.cancel()
}

private fun texto(estado: EstadoDescarga): String = when (estado) {
    EstadoDescarga.Pendiente -> "en cola"
    is EstadoDescarga.Descargando -> "${estado.progreso} %"
    is EstadoDescarga.Completada -> "lista"
    is EstadoDescarga.Fallida -> "error"
}
