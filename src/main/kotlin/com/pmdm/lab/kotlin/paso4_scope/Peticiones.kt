package com.pmdm.lab.kotlin.paso4_scope

import com.pmdm.lab.kotlin.modelo.Descarga

/** Configuración de una petición: se rellena propiedad a propiedad. */
class Peticion {
    var url: String = ""
    var reintentos: Int = 0
    val cabeceras: MutableMap<String, String> = mutableMapOf()
}

/**
 * Usa apply. Crea una Peticion con esa [url], 3 reintentos y la cabecera
 * "Accept" con el valor que acepta cualquier tipo: asterisco, barra, asterisco.
 */
fun crearPeticion(url: String): Peticion =
    Peticion().apply {
        this.url = url
        reintentos = 3
        cabeceras["Accept"] = "*/*"
    }

/**
 * Usa let con ?. y el operador ?: . Devuelve lo que hay tras la última barra
 * de la [url], o "sin-nombre" si la url es null o termina en barra.
 */
fun nombreDesdeUrl(url: String?): String =
    url?.let { it.substringAfterLast('/') }?.takeIf { it.isNotBlank() } ?: "sin-nombre"

/** Usa also. Añade "Añadida: nombre" a [log] y devuelve la misma descarga. */
fun registrar(descarga: Descarga, log: MutableList<String>): Descarga =
    descarga.also { log.add("Añadida: ${it.nombre}") }

/** Usa run. Devuelve "2 GB" a partir de 1024 MB (división entera) o "300 MB". */
fun tamanoLegible(descarga: Descarga): String =
    descarga.run { if (tamanoMb >= 1024) "${tamanoMb / 1024} GB" else "$tamanoMb MB" }

/** Usa with. Devuelve "3 descargas, 900 MB" para la lista recibida. */
fun resumen(descargas: List<Descarga>): String =
    with(descargas) { "$size descargas, ${sumOf { it.tamanoMb }} MB" }
