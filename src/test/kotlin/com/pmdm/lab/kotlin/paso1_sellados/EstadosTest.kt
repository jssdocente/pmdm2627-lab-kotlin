package com.pmdm.lab.kotlin.paso1_sellados

import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Completada
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Descargando
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Fallida
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Pendiente
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EstadosTest {
    @Test
    fun `describir devuelve el texto de cada estado`() {
        assertEquals("En cola", describir(Pendiente))
        assertEquals("Descargando 40 %", describir(Descargando(40)))
        assertEquals("Guardada en /a.zip", describir(Completada("/a.zip")))
        assertEquals("Error: sin red", describir(Fallida("sin red")))
    }

    @Test
    fun `estaTerminada solo es cierto en completada y fallida`() {
        assertFalse(Pendiente.estaTerminada())
        assertFalse(Descargando(99).estaTerminada())
        assertTrue(Completada("/a").estaTerminada())
        assertTrue(Fallida("x").estaTerminada())
    }

    @Test
    fun `avanzar desde pendiente empieza a descargar`() {
        assertEquals(Descargando(10), avanzar(Pendiente, 10, "/a"))
    }

    @Test
    fun `avanzar suma el progreso`() {
        assertEquals(Descargando(50), avanzar(Descargando(30), 20, "/a"))
    }

    @Test
    fun `avanzar hasta 100 o mas completa la descarga`() {
        assertEquals(Completada("/a"), avanzar(Descargando(90), 10, "/a"))
        assertEquals(Completada("/a"), avanzar(Descargando(90), 50, "/a"))
    }

    @Test
    fun `una descarga terminada no cambia`() {
        assertEquals(Completada("/b"), avanzar(Completada("/b"), 10, "/a"))
        assertEquals(Fallida("x"), avanzar(Fallida("x"), 10, "/a"))
    }
}
