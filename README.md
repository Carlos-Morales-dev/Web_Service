# Guía de Exposición y Arquitectura: Consumo de Web Service REST en Android

Este proyecto es una aplicación nativa en Android desarrollada en **Kotlin** y **Jetpack Compose** que consume el servicio web REST público **JSONPlaceholder**. Implementa las operaciones CRUD completas (**GET, POST, PUT, DELETE**) siguiendo la arquitectura recomendada por Google (**MVVM + Repository Pattern**).

---

## 📐 Arquitectura General del Proyecto

```
com.example
├── MainActivity.kt                      # Punto de entrada y host de Snackbars
├── data
│   ├── local
│   │   └── ContenidoEs.kt              # Diccionario de textos contextualizados
│   ├── mapper
│   │   └── ContenidoMapper.kt          # Mapeo de respuestas a español
│   ├── model
│   │   ├── Post.kt                      # Modelo de Publicación
│   │   ├── User.kt                      # Modelo de Usuario
│   │   └── Comment.kt                   # Modelo de Comentario
│   ├── remote
│   │   ├── PostApi.kt                   # Interfaz Retrofit (Endpoints HTTP)
│   │   └── RetrofitClient.kt            # Singleton de Retrofit y OkHttp
│   └── repository
│       └── PostRepository.kt            # Repositorio (Manejo de red y excepciones)
├── ui
│   ├── navigation
│   │   └── AppNavigation.kt             # Grafo de navegación (Navigation Compose)
│   ├── screens
│   │   ├── PostListScreen.kt            # Pantalla principal (Listado y filtro)
│   │   ├── PostDetailScreen.kt          # Pantalla de detalle y comentarios
│   │   ├── PostFormScreen.kt            # Pantalla de creación/edición
│   │   └── UserListScreen.kt            # Pantalla de selección de usuario
│   └── viewmodel
│       └── PostViewModel.kt             # Lógica de negocio y estados de UI
```

---

## 🎤 Estructura y Guion para la Exposición en Clase (4 Integrantes)

---

### 👤 Integrante 1: Fundamentos de Web Services REST, Modelos de Datos y Cliente HTTP
* **Duración estimada:** 3 - 4 minutos
* **Nivel de dificultad:** Medio
* **Objetivo:** Explicar qué es un servicio web REST, cómo se estructuran los datos JSON y cómo se configura el cliente de red en Android.

#### 📄 Archivos a mostrar:
1. `data/model/Post.kt`, `data/model/User.kt`, `data/model/Comment.kt`
2. `data/remote/RetrofitClient.kt`

#### 🗣️ Puntos clave para la exposición:
* **¿Qué es un Web Service REST?**: Explicar que un servicio web RESTful permite transferir información entre cliente (Android) y servidor usando el protocolo HTTP y formato JSON.
* **Modelos de Datos (`Post`, `User`, `Comment`)**: 
  * Mostrar cómo las clases `data class` convierten campos JSON a propiedades de Kotlin.
  * Mención especial de `@SerializedName`: permite mapear el nombre exacto de la clave del JSON (ej: `userId`) aunque en Kotlin usemos otra convención.
* **Cliente Retrofit (`RetrofitClient`)**:
  * Explicar el patrón **Singleton** (`object RetrofitClient`).
  * **Base URL**: `https://jsonplaceholder.typicode.com/`.
  * **OkHttpClient**: Interceptor de logs (`HttpLoggingInterceptor`) para depurar las peticiones en tiempo de desarrollo.
  * **GsonConverterFactory**: Convierte automáticamente el texto JSON a objetos Kotlin.

---

### 👤 Integrante 2: Definición de Endpoints (PostApi) y Capa de Repositorio (PostRepository)
* **Duración estimada:** 4 - 5 minutos
* **Nivel de dificultad:** Medio - Alto
* **Objetivo:** Explicar las peticiones HTTP (GET, POST, PUT, DELETE), el uso de Corrutinas para asincronía y la gestión de errores de red.

#### 📄 Archivos a mostrar:
1. `data/remote/PostApi.kt`
2. `data/repository/PostRepository.kt`

#### 🗣️ Puntos clave para la exposición:
* **Endpoints HTTP (`PostApi`)**:
  * `@GET("posts")`: Consulta publicaciones. Uso de `@Query("userId")` para filtrado por autor.
  * `@GET("posts/{id}")`: Consulta una publicación específica usando `@Path("id")`.
  * `@POST("posts")`: Envía datos para crear una nueva publicación con `@Body`. Retorna `201 Created`.
  * `@PUT("posts/{id}")`: Actualiza una publicación existente. Retorna `200 OK`.
  * `@DELETE("posts/{id}")`: Elimina un recurso por su ID.
  * **Corrutinas (`suspend`)**: Explicar que las funciones son `suspend` para ejecutarse en segundo plano sin congelar la pantalla.
* **Capa de Repositorio (`PostRepository`)**:
  * **Aislamiento de fuente de datos**: El repositorio es el único encargado de hablar con la API REST.
  * **`Dispatchers.IO`**: Garantiza que las llamadas de red se hagan en hilos dedicados a entrada/salida.
  * **`NetworkResult<T>`**: Clase sellada (`sealed class`) que encapsula el resultado en dos estados: `Success` (con datos y código HTTP) o `Error` (con mensaje detallado).
  * **Manejo de errores**: Traducción de excepciones (`UnknownHostException`, `TimeoutException`) y códigos HTTP (`404 Not Found`, `500 Server Error`).

---

### 👤 Integrante 3: Patrón MVVM, Estado Reactivo (PostViewModel) y Navegación
* **Duración estimada:** 4 - 5 minutos
* **Nivel de dificultad:** Medio - Alto
* **Objetivo:** Explicar cómo el ViewModel gestiona la lógica de negocio, mantiene el estado de la UI y coordina las llamadas del repositorio.

#### 📄 Archivos a mostrar:
1. `ui/viewmodel/PostViewModel.kt`
2. `ui/navigation/AppNavigation.kt`

#### 🗣️ Puntos clave para la exposición:
* **Patrón MVVM (Model-View-ViewModel)**: Explicar que el ViewModel sobrevive a cambios de configuración (como girar la pantalla) y separa la lógica de negocio de la interfaz.
* **Flujos Reactivos (`StateFlow` y `SharedFlow`)**:
  * `uiState`, `detailUiState`, `usersUiState`: Representan el estado de la UI (Cargando, Éxito, Error).
  * `_userMessage` (`SharedFlow`): Emite eventos únicos para mostrar notificaciones en Snackbar sobre la respuesta de la API (ej: `HTTP POST 201 Created`).
* **Lógica CRUD en el ViewModel**:
  * `crearPublicacion()`: Invoca la API POST, actualiza la lista local en memoria y emite el mensaje de confirmación con su código de estado HTTP.
  * `editarPublicacion()`: Valida permisos de autor antes de invocar la API PUT.
  * `eliminarPublicacion()`: Elimina el recurso vía API DELETE y actualiza el estado.
* **Navegación (`AppNavigation`)**:
  * Uso de `NavHost` y `NavController` para pasar de la lista al detalle, al formulario o al listado de usuarios.

---

### 👤 Integrante 4 (Parte Fácil y Práctica): Interfaz de Usuario en Jetpack Compose y Demostración en Vivo
* **Duración estimada:** 3 - 4 minutos
* **Nivel de dificultad:** Fácil (Visual e Intuitivo)
* **Posición:** Último en exponer
* **Objetivo:** Mostrar cómo la interfaz de usuario reacciona al estado y realizar la prueba en vivo de la aplicación en el emulador.

#### 📄 Archivos a mostrar:
1. `ui/screens/PostListScreen.kt` o `PostFormScreen.kt`
2. `MainActivity.kt`

#### 🗣️ Puntos clave para la exposición:
* **Interfaz Declarativa con Jetpack Compose**:
  * Explicar brevemente que la UI se construye mediante funciones `@Composable` que se redefinen automáticamente cuando cambia el estado (`collectAsStateWithLifecycle`).
  * Mostrar cómo `MainActivity.kt` escucha los eventos del ViewModel para mostrar avisos con `SnackbarHost`.
* **Demostración Práctica en Vivo (En el emulador / dispositivo)**:
  1. **GET (Consultar)**: Mostrar la lista inicial de reseñas de libros obtenida de la API REST. Probar el botón de refrescar y el filtro por usuario.
  2. **POST (Crear)**: Abrir el formulario con el botón `+`, escribir una nueva reseña y presionar "Publicar". Señalar la notificación Snackbar que confirma: `HTTP POST 201 Created`.
  3. **PUT (Editar)**: Seleccionar una reseña del usuario activo, hacer clic en "Editar", modificar el contenido y guardar. Señalar el mensaje: `HTTP PUT 200 OK`.
  4. **DELETE (Eliminar)**: Eliminar una reseña y mostrar la confirmación en pantalla: `HTTP DELETE 200 OK`.

---

## 🛠️ Requisitos e Instalación

1. Tener instalado **Android Studio** (versión Iguana, Jellyfish, Ladybug o superior).
2. Tener configurado el **JDK 17** o superior.
3. Clonar o abrir este proyecto en Android Studio.
4. Conectar un emulador o dispositivo físico con acceso a internet.
5. Ejecutar la tarea de Gradle `:app:assembleDebug` o presionar **Run 'app'**.
