# Spyro The Dragon - App Mejorada

## Introducción
Esta aplicación está inspirada en el universo de **Spyro the Dragon** y se ha actualizado para hacerla más atractiva y fácil de usar. Para ello, se han desarrollado dos elementos clave:

1. **Guía de inicio interactiva**: Presenta las principales funcionalidades de la app con animaciones, sonidos y elementos gráficos para enriquecer la experiencia del usuario.
2. **Easter Egg**: Añade un toque de sorpresa y entretenimiento, creando momentos divertidos y memorables para los usuarios.

## Instalación
Para descargar el código del proyecto, sigue estos pasos:

1. Abre **Android Studio**.
2. En el menú superior, selecciona `File > New > Project From Version Control...`.
3. En la opción de `Version Control`, selecciona `Git`.
4. Introduce el siguiente enlace en el apartado `URL`:  
   **[https://github.com/lbarmar/SpyroTheDragon.git](https://github.com/NURIALO/NURIA-LOPEZ-GAGO-PMDM-04-TAREA/]**
5. En `Directory`, selecciona la carpeta donde se guardará el proyecto.
6. Pulsa `Clone`.
7. Modifica el nombre en `About` para que muestre el tuyo.
8. Ejecuta la app y explora su navegación y estructuración de código.

## Características Principales

### Guía de inicio interactiva
La guía está compuesta por varias pantallas superpuestas a la aplicación que explican cada sección:

- **Pantalla 1**: Bienvenida e introducción con fondo personalizado y botón de "Comenzar".
- **Pantalla 2**: Explicación de la pestaña `Personajes` con un bocadillo informativo animado.
- **Pantalla 3**: Explicación de la pestaña `Mundos` con una animación.
- **Pantalla 4**: Explicación de la pestaña `Coleccionables`.
- **Pantalla 5**: Explicación del icono de información en la `Action Bar`.
- **Pantalla 6**: Resumen final y cierre de la guía.

La guía solo se muestra la **primera vez** que se abre la app, utilizando `SharedPreferences` para registrar si el usuario ya la ha completado.

### Navegación
- Se ha implementado un **botón de avance** en cada pantalla de la guía.
- Se ha incluido un **botón para omitir la guía** en cualquier momento que lleva al juego directamente.
- La interacción con la app está **bloqueada mientras la guía está activa** para mantener el enfoque del usuario.

### Animaciones
- Se han implementado **efectos de aparición, deslizamiento y escalado** en los bocadillos informativos.
- Se han añadido **transiciones visuales** al cambiar entre pantallas de la guía.

### Sonidos
- Se han incluido **efectos de sonido** relacionados con la temática de Spyro en momentos clave, como:
  - Avanzar de pantalla
  - Interactuar con bocadillos
  - Completar la guía

## Easter Eggs
Se han agregado **dos Easter Eggs** en la aplicación:

1. **Easter Egg con vídeo**
   - Ubicado en la pestaña `Coleccionables`.
   - Se activa al pulsar **cuatro veces consecutivas** sobre las gemas.
   - Reproduce un **video temático** en pantalla completa.
   - Al finalizar, redirige automáticamente a la pestaña `Coleccionables`.

2. **Easter Egg con animación**
   - Ubicado en la pestaña `Personajes`.
   - Se activa con una **pulsación prolongada** sobre el personaje Spyro.
   - Muestra una animación creada con `Canvas` donde **Spyro lanza una llama de fuego**.

## Tecnologías utilizadas
- **Android Studio**
- **Java**
- **View Binding**
- **Recyclerview y Cardview**
- **SharedPreferences**
- **Animaciones XML**
- **Canvas**
- **Transiciones**
- **MediaPlayer** (para los sonidos)

## Conclusiones
Este proyecto ha permitido mejorar la experiencia de usuario con una guía visual y Easter Eggs interactivos, haciendo que la app sea más atractiva y dinámica. Se han aplicado conocimientos de diseño UI/UX, animaciones y navegación en Android.

¡Espero que disfrutes la experiencia de Spyro, que la guía te sea últil para conocer más la app y que encuentres la sorpresa!!!! 🐉🔥


---
**Desarrollador:** [Nuria López Gago]  
**Repositorio:** [https://github.com/NURIALO/NURIA-LOPEZ-GAGO-PMDM-04-TAREA)

