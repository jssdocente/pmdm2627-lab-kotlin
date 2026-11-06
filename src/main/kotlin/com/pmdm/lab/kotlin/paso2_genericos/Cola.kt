package com.pmdm.lab.kotlin.paso2_genericos

/** Una cola FIFO de cualquier tipo: sale primero el que entró primero. */
class Cola<T> {
    private val elementos = ArrayDeque<T>()

    val tamano: Int get() = elementos.size

    /** Añade [elemento] al final de la cola. */
    fun encolar(elemento: T) {
        elementos.addLast(elemento)
    }

    /** Saca y devuelve el primero, o null si la cola está vacía. */
    fun desencolar(): T? =
        elementos.removeFirstOrNull()

    /** Devuelve el primero sin sacarlo, o null si la cola está vacía. */
    fun siguiente(): T? =
        elementos.firstOrNull()
}
