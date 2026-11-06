package com.pmdm.lab.kotlin.paso6_cancelacion

import com.pmdm.lab.kotlin.modelo.EstadoDescarga
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Completada
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Descargando
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Fallida
import com.pmdm.lab.kotlin.modelo.ServidorFalso
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SeguridadTest {
    private val servidor = ServidorFalso(retardoMs = 1000, fallan = setOf("roto.zip"))

    @Test
    fun `descargaSegura devuelve completada si todo va bien`() = runTest {
        assertEquals(Completada("/descargas/a.zip"), descargaSegura(servidor, "a.zip"))
    }

    @Test
    fun `descargaSegura convierte el fallo en un estado`() = runTest {
        assertEquals(Fallida("No se pudo descargar roto.zip"), descargaSegura(servidor, "roto.zip"))
    }

    @Test
    fun `descargaSegura no se traga la cancelacion`() = runTest {
        var resultado: EstadoDescarga? = null
        val job = launch { resultado = descargaSegura(servidor, "a.zip") }
        advanceTimeBy(500)
        job.cancel()
        advanceUntilIdle()
        assertNull(resultado)
        assertTrue(job.isCancelled)
    }

    @Test
    fun `descargaConLimite termina si le da tiempo`() = runTest {
        assertEquals(Completada("/descargas/a"), descargaConLimite(servidor, "a", 2000))
    }

    @Test
    fun `descargaConLimite falla si tarda demasiado`() = runTest {
        assertEquals(Fallida("Tiempo agotado"), descargaConLimite(servidor, "a", 500))
    }

    @Test
    fun `conReintentos se detiene en el primer acierto`() = runTest {
        var intentos = 0
        val resultado = conReintentos(3) {
            intentos++
            if (intentos < 2) throw IOException("falla")
            "ok"
        }
        assertEquals("ok", resultado)
        assertEquals(2, intentos)
    }

    @Test
    fun `conReintentos deja salir el ultimo fallo`() = runTest {
        var intentos = 0
        assertFailsWith<IOException> {
            conReintentos(3) {
                intentos++
                throw IOException("falla")
            }
        }
        assertEquals(3, intentos)
    }

    @Test
    fun `conReintentos no reintenta la cancelacion`() = runTest {
        var intentos = 0
        assertFailsWith<CancellationException> {
            conReintentos<String>(3) {
                intentos++
                throw CancellationException("cancelada")
            }
        }
        assertEquals(1, intentos)
    }

    @Test
    fun `iniciarCancelable avisa de cada estado`() = runTest {
        val estados = mutableListOf<EstadoDescarga>()
        iniciarCancelable(servidor, "a") { estados.add(it) }
        advanceUntilIdle()
        assertEquals(listOf(Descargando(0), Completada("/descargas/a")), estados)
    }

    @Test
    fun `iniciarCancelable avisa si la cancelan`() = runTest {
        val estados = mutableListOf<EstadoDescarga>()
        val job = iniciarCancelable(servidor, "a") { estados.add(it) }
        advanceTimeBy(500)
        runCurrent()
        job.cancel()
        advanceUntilIdle()
        assertEquals(listOf(Descargando(0), Fallida("Cancelada")), estados)
        assertTrue(job.isCancelled)
    }
}
