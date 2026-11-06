# Codelab de Kotlin: gestor de descargas (PMDM 26/27)

Siete pasos cortos para practicar Kotlin construyendo un gestor de descargas:
tipos sellados, genéricos, colecciones y mapas, *scope functions*, corrutinas y flujos.

El enunciado de cada paso está en los apuntes: <https://jssdocente.github.io/pmdm2627d>,
en *El lenguaje Kotlin › Codelab de Kotlin*.

## Cómo se trabaja

1. Clona el repositorio o descarga el `.zip`, y abre la carpeta en IntelliJ IDEA.

2. Abre el paquete del paso en `src/main/kotlin` y rellena los `TODO`.

3. Haz clic derecho sobre el paquete del paso en `src/test/kotlin` y elige **Run Tests**.

También puedes lanzarlos desde la terminal:

```bash
./gradlew test --tests "com.pmdm.lab.kotlin.paso1_sellados.*"
```

Los pasos que aún no has hecho fallan con `TODO`: es lo normal. Cada paso solo necesita sus propios tests en verde.

Al acabar el paso 7, `./gradlew run` muestra el gestor funcionando en la consola.

## Soluciones

Están en la rama `soluciones`, con un tag por paso (`c0.01-fin` … `c0.07-fin`). `main` conserva siempre los `TODO`.
