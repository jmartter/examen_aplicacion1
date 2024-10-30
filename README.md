# Examen Aplicacion 1

## Enlace al Repositorio
[Repositorio en GitHub](https://github.com/jmartter/Feedback1_Eventos.git)

## Descripción General

Examen Aplicacion 1 es una aplicación de recordatorios construida usando Kotlin y Jetpack Compose. La aplicación permite a los usuarios agregar, ver y gestionar recordatorios. También soporta el cambio de idioma entre inglés y español.

## Características

- **Agregar Recordatorios**: Los usuarios pueden agregar nuevos recordatorios usando un campo de texto.
- **Ver Recordatorios**: Los recordatorios se muestran en una lista y los usuarios pueden filtrarlos por su estado (pendientes o hechos).
- **Gestionar Recordatorios**: Los usuarios pueden marcar los recordatorios como hechos o eliminarlos.
- **Cambio de Idioma**: Los usuarios pueden cambiar el idioma de la aplicación entre inglés y español.

## Componentes

### MainActivity

La clase `MainActivity` inicializa la base de datos de Firebase Firestore y configura el contenido principal de la aplicación. Incluye un botón para cambiar el idioma y la interfaz principal de la aplicación de recordatorios.

### ReminderApp

La función composable `ReminderApp` es el componente principal de la interfaz de usuario de la aplicación. Incluye:

- Un campo de texto para agregar nuevos recordatorios.
- Una lista de recordatorios filtrados por su estado.
- Botones para filtrar los recordatorios por estado (pendientes o hechos).
- Un menú desplegable para gestionar los recordatorios (marcar como hecho o eliminar).

### LanguageToggleButton

La función composable `LanguageToggleButton` permite a los usuarios cambiar el idioma de la aplicación entre inglés y español.

### Reminder

La clase de datos `Reminder` representa un recordatorio con un texto y un estado booleano `isDone` que indica si está hecho.

## Recursos

### Cadenas de Texto

La aplicación soporta múltiples idiomas con recursos de cadenas de texto definidos en `strings.xml` y `strings-es.xml` para inglés y español, respectivamente.

### Temas

La aplicación usa un tema personalizado definido en `themes.xml`.

## Integración con Firebase

La aplicación usa Firebase Firestore para almacenar y gestionar los recordatorios. Incluye funcionalidades para agregar, actualizar y eliminar recordatorios en la base de datos de Firestore.

## Dependencias

- Kotlin
- Jetpack Compose
- Firebase Firestore
- Android Studio
