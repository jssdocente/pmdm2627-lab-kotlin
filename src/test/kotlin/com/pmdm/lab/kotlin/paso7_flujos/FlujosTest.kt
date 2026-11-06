package com.pmdm.lab.kotlin.paso7_flujos

import com.pmdm.lab.kotlin.modelo.Descarga
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Completada
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Descargando
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Pendiente
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FlujosTest {
    @Test
    fun `progreso emite los porcentajes`() = runTest {
        assertEquals(listOf(25, 50, 75, 100), progreso(4, 100).toList())
    }

    @Test
    fun `progreso espera entre valores`() = runTest {
        progreso(4, 100).toList()
        assertEquals(400, currentTime)
    }

    @Test
    fun `progreso es frio y no hace nada hasta que lo recogen`() = runTest {
        progreso(4, 100)
        assertEquals(0, currentTime)
    }

    @Test
    fun `comoEstados convierte los porcentajes`() = runTest {
        val estados = flowOf(50, 100).comoEstados("/a").toList()
        assertEquals(listOf(Descargando(50), Completada("/a")), estados)
    }

    @Test
    fun `agregar guarda la descarga por su id`() = runTest {
        val gestor = GestorDescargas(this)
        gestor.agregar(Descarga(7, "a.zip", 10))
        assertEquals(Pendiente, gestor.descargas.value[7]?.estado)
    }

    @Test
    fun `cambiarEstado actualiza una descarga`() = runTest {
        val gestor = GestorDescargas(this)
        gestor.agregar(Descarga(7, "a.zip", 10))
        gestor.cambiarEstado(7, Descargando(20))
        assertEquals(Descargando(20), gestor.descargas.value[7]?.estado)
    }

    @Test
    fun `cambiarEstado ignora un id desconocido`() = runTest {
        val gestor = GestorDescargas(this)
        gestor.cambiarEstado(99, Descargando(20))
        assertTrue(gestor.descargas.value.isEmpty())
    }

    @Test
    fun `iniciar lleva la descarga hasta el final`() = runTest {
        val gestor = GestorDescargas(this, pasos = 2, retardoMs = 100)
        gestor.agregar(Descarga(1, "a.zip", 10))
        gestor.iniciar(1)
        advanceTimeBy(100)
        runCurrent()
        assertEquals(Descargando(50), gestor.descargas.value[1]?.estado)
        advanceUntilIdle()
        assertEquals(Completada("/descargas/a.zip"), gestor.descargas.value[1]?.estado)
    }

    @Test
    fun `iniciar se puede cancelar`() = runTest {
        val gestor = GestorDescargas(this, pasos = 2, retardoMs = 100)
        gestor.agregar(Descarga(1, "a.zip", 10))
        val job = gestor.iniciar(1)
        advanceTimeBy(100)
        runCurrent()
        job.cancel()
        advanceUntilIdle()
        assertEquals(Descargando(50), gestor.descargas.value[1]?.estado)
    }

    @Test
    fun `iniciar con un id desconocido no hace nada`() = runTest {
        val gestor = GestorDescargas(this)
        assertFalse(gestor.iniciar(99).isActive)
    }

    @Test
    fun `activas cuenta las que se estan descargando`() = runTest {
        val gestor = GestorDescargas(this)
        gestor.agregar(Descarga(1, "a", 10))
        gestor.agregar(Descarga(2, "b", 10))
        gestor.cambiarEstado(1, Descargando(10))
        assertEquals(1, gestor.activas().first())
    }

    @Test
    fun `descargas es un estado que se puede observar`() = runTest {
        val gestor = GestorDescargas(this)
        val vistos = mutableListOf<Int>()
        val observador = launch { gestor.descargas.collect { vistos.add(it.size) } }
        runCurrent()
        gestor.agregar(Descarga(1, "a", 10))
        runCurrent()
        observador.cancel()
        assertEquals(listOf(0, 1), vistos)
    }
}
