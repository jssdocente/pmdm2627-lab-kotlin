package com.pmdm.lab.kotlin.paso4_scope

import com.pmdm.lab.kotlin.modelo.Descarga
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class PeticionesTest {
    @Test
    fun `crearPeticion configura todas las propiedades`() {
        val peticion = crearPeticion("https://x.es/a.zip")
        assertEquals("https://x.es/a.zip", peticion.url)
        assertEquals(3, peticion.reintentos)
        assertEquals("*/*", peticion.cabeceras["Accept"])
    }

    @Test
    fun `nombreDesdeUrl extrae lo que hay tras la ultima barra`() {
        assertEquals("a.zip", nombreDesdeUrl("https://x.es/juegos/a.zip"))
    }

    @Test
    fun `nombreDesdeUrl usa un valor por defecto`() {
        assertEquals("sin-nombre", nombreDesdeUrl(null))
        assertEquals("sin-nombre", nombreDesdeUrl("https://x.es/"))
    }

    @Test
    fun `registrar anota y devuelve la misma descarga`() {
        val log = mutableListOf<String>()
        val descarga = Descarga(1, "a.zip", 10)
        assertSame(descarga, registrar(descarga, log))
        assertEquals(listOf("Añadida: a.zip"), log)
    }

    @Test
    fun `tamanoLegible elige la unidad`() {
        assertEquals("300 MB", tamanoLegible(Descarga(1, "a", 300)))
        assertEquals("1 GB", tamanoLegible(Descarga(1, "a", 1024)))
        assertEquals("2 GB", tamanoLegible(Descarga(1, "a", 2100)))
    }

    @Test
    fun `resumen cuenta y suma`() {
        val lista = listOf(Descarga(1, "a", 300), Descarga(2, "b", 600))
        assertEquals("2 descargas, 900 MB", resumen(lista))
    }
}
