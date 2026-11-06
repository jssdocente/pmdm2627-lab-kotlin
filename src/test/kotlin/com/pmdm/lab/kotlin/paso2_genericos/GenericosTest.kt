package com.pmdm.lab.kotlin.paso2_genericos

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GenericosTest {
    private data class Parche(val nombre: String, override val tamanoMb: Int) : ConTamano

    @Test
    fun `mapear transforma el valor y puede cambiar su tipo`() {
        val resultado: Resultado<Int> = Resultado.Exito(5)
        assertEquals(Resultado.Exito("5 MB"), resultado.mapear { "$it MB" })
    }

    @Test
    fun `mapear deja el error igual y no llama a la funcion`() {
        val error: Resultado<Int> = Resultado.Error("sin red")
        var llamada = false
        val mapeado = error.mapear { llamada = true; it + 1 }
        assertEquals(Resultado.Error("sin red"), mapeado)
        assertEquals(false, llamada)
    }

    @Test
    fun `valorO devuelve el valor o el valor por defecto`() {
        assertEquals("a", Resultado.Exito("a").valorO("b"))
        assertEquals("b", (Resultado.Error("x") as Resultado<String>).valorO("b"))
    }

    @Test
    fun `la cola saca los elementos en orden de llegada`() {
        val cola = Cola<String>()
        cola.encolar("a")
        cola.encolar("b")
        assertEquals(2, cola.tamano)
        assertEquals("a", cola.desencolar())
        assertEquals("b", cola.desencolar())
        assertEquals(0, cola.tamano)
    }

    @Test
    fun `siguiente consulta sin sacar`() {
        val cola = Cola<Int>()
        cola.encolar(7)
        assertEquals(7, cola.siguiente())
        assertEquals(1, cola.tamano)
    }

    @Test
    fun `una cola vacia devuelve null`() {
        val cola = Cola<Int>()
        assertNull(cola.desencolar())
        assertNull(cola.siguiente())
    }

    @Test
    fun `mayor devuelve el elemento con su tipo real`() {
        val parches = listOf(Parche("a", 10), Parche("b", 30), Parche("c", 20))
        val grande: Parche? = mayor(parches)
        assertEquals("b", grande?.nombre)
        assertNull(mayor(emptyList<Parche>()))
    }

    @Test
    fun `totalMb suma lo que ocupan`() {
        assertEquals(60, totalMb(listOf(Parche("a", 10), Parche("b", 50))))
        assertEquals(0, totalMb(emptyList<Parche>()))
    }
}
