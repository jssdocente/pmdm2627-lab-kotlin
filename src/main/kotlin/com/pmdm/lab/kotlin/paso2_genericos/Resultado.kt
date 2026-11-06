package com.pmdm.lab.kotlin.paso2_genericos

/** El resultado de una operación que puede fallar, sea cual sea el tipo del valor. */
sealed interface Resultado<out T> {
    data class Exito<out T>(val valor: T) : Resultado<T>
    data class Error(val mensaje: String) : Resultado<Nothing>
}

/** Si es Exito, transforma su valor con [transformar]; si es Error, lo devuelve igual. */
fun <T, R> Resultado<T>.mapear(transformar: (T) -> R): Resultado<R> =
    when (this) {
        is Resultado.Exito -> Resultado.Exito(transformar(valor))
        is Resultado.Error -> this
    }

/** Devuelve el valor si es Exito o [porDefecto] si es Error. */
fun <T> Resultado<T>.valorO(porDefecto: T): T =
    when (this) {
        is Resultado.Exito -> valor
        is Resultado.Error -> porDefecto
    }
