package com.pmdm.lab.kotlin.paso5_suspend

import com.pmdm.lab.kotlin.modelo.ServidorFalso
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class DescargasTest {
    private val servidor = ServidorFalso(retardoMs = 1000)

    @Test
    fun `descargarUna encadena los tres pasos`() = runTest {
        assertEquals("/descargas/a.zip", descargarUna(servidor, "a.zip"))
    }

    @Test
    fun `descargarUna tarda lo que suman sus tres pasos`() = runTest {
        descargarUna(servidor, "a.zip")
        assertEquals(3000, currentTime)
    }

    @Test
    fun `descargarVarias mantiene el orden`() = runTest {
        val rutas = descargarVarias(servidor, listOf("a", "b", "c"))
        assertEquals(listOf("/descargas/a", "/descargas/b", "/descargas/c"), rutas)
    }

    @Test
    fun `descargarVarias trabaja en paralelo y tarda 3 s, no 9 s`() = runTest {
        descargarVarias(servidor, listOf("a", "b", "c"))
        assertEquals(3000, currentTime)
    }

    @Test
    fun `descargarEnSegundoPlano no espera a la descarga`() = runTest {
        var ruta: String? = null
        val job = descargarEnSegundoPlano(servidor, "a") { ruta = it }
        assertNull(ruta)
        advanceUntilIdle()
        assertEquals("/descargas/a", ruta)
        assertFalse(job.isActive)
    }
}
