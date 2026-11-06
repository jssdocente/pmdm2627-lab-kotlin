package com.pmdm.lab.kotlin.paso3_colecciones

import com.pmdm.lab.kotlin.modelo.Descarga
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Completada
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Descargando
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Fallida
import com.pmdm.lab.kotlin.modelo.EstadoDescarga.Pendiente
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class EstadisticasTest {
    private val mapa = Descarga(1, "mapa.zip", 300, Completada("/mapa.zip"))
    private val musica = Descarga(2, "musica.zip", 120, Descargando(40))
    private val video = Descarga(3, "video.zip", 900, Completada("/video.zip"))
    private val texturas = Descarga(4, "texturas.zip", 500, Pendiente)
    private val voces = Descarga(5, "voces.zip", 80, Fallida("sin red"))
    private val lista = listOf(mapa, musica, video, texturas, voces)

    @Test
    fun `nombresCompletadas filtra y transforma`() {
        assertEquals(listOf("mapa.zip", "video.zip"), nombresCompletadas(lista))
    }

    @Test
    fun `lasMasGrandes ordena de mayor a menor y limita`() {
        assertEquals(listOf("video.zip", "texturas.zip"), lasMasGrandes(lista, 2))
    }

    @Test
    fun `porId permite buscar por clave`() {
        val mapaId = porId(lista)
        assertEquals(5, mapaId.size)
        assertEquals(video, mapaId[3])
    }

    @Test
    fun `agruparPorEstado reparte las descargas`() {
        val grupos = agruparPorEstado(lista)
        assertEquals(listOf(mapa, video), grupos["Completada"])
        assertEquals(listOf(voces), grupos["Fallida"])
        assertEquals(4, grupos.size)
    }

    @Test
    fun `contarPorEstado cuenta cada grupo`() {
        assertEquals(
            mapOf("Completada" to 2, "Descargando" to 1, "Pendiente" to 1, "Fallida" to 1),
            contarPorEstado(lista),
        )
    }

    @Test
    fun `megasPorEstado suma cada grupo`() {
        val megas = megasPorEstado(lista)
        assertEquals(1200, megas["Completada"])
        assertEquals(120, megas["Descargando"])
    }

    @Test
    fun `cambiarEstado crea un mapa nuevo sin tocar el original`() {
        val original = porId(lista)
        val nuevo = cambiarEstado(original, 4, Descargando(10))
        assertEquals(Descargando(10), nuevo[4]?.estado)
        assertEquals(Pendiente, original[4]?.estado)
    }

    @Test
    fun `cambiarEstado con un id desconocido devuelve el mismo mapa`() {
        val original = porId(lista)
        assertSame(original, cambiarEstado(original, 99, Pendiente))
    }

    @Test
    fun `quitar crea un mapa nuevo sin esa clave`() {
        val original = porId(lista)
        val nuevo = quitar(original, 1)
        assertEquals(4, nuevo.size)
        assertEquals(5, original.size)
    }
}
