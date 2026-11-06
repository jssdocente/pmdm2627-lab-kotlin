package com.pmdm.lab.kotlin.paso2_genericos

/** Cualquier cosa de la que se pueda preguntar cuánto ocupa. */
interface ConTamano {
    val tamanoMb: Int
}

/** Devuelve el elemento que más ocupa, o null si la lista está vacía. */
fun <T : ConTamano> mayor(elementos: List<T>): T? =
    TODO("Pendiente de implementar")

/** Suma lo que ocupan todos los elementos. */
fun <T : ConTamano> totalMb(elementos: List<T>): Int =
    TODO("Pendiente de implementar")
