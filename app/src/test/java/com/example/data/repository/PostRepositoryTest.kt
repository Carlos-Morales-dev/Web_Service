package com.example.data.repository

import com.example.data.mapper.enEspanol
import com.example.data.model.Comment
import com.example.data.model.Post
import com.example.data.remote.PostApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * Pruebas unitarias para [PostRepository] y utilidades de dominio utilizando [MockWebServer].
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PostRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: PostApi
    private lateinit var repository: PostRepository

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApi::class.java)

        repository = PostRepository(
            api = api,
            ioDispatcher = Dispatchers.Unconfined
        )
    }

    @After
    fun tearDown() {
        try {
            mockWebServer.shutdown()
        } catch (_: Exception) {
        }
    }

    @Test
    fun getPosts_con_respuesta_200_devuelve_lista_exitosa() = runTest {
        val json = """
            [
              {"userId": 1, "id": 1, "title": "sunt aut facere", "body": "quia et suscipit"},
              {"userId": 1, "id": 2, "title": "qui est esse", "body": "est rerum tempore"}
            ]
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(json))

        val result = repository.getPosts()

        assertTrue(result is NetworkResult.Success)
        val data = (result as NetworkResult.Success).data
        assertEquals(2, data.size)
        assertEquals(1, data[0].id)
        // Debe haberse aplicado la traducción al español para el id 1 (Reseñas de libros)
        assertEquals("Reseña: 'Cien años de soledad' de Gabriel García Márquez", data[0].title)
    }

    @Test
    fun createPost_con_respuesta_201_devuelve_success_con_codigo_201() = runTest {
        val json = """
            {"userId": 1, "id": 101, "title": "Nuevo post", "body": "Cuerpo del post"}
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setResponseCode(201).setBody(json))

        val post = Post(userId = 1, title = "Nuevo post", body = "Cuerpo del post")
        val result = repository.createPost(post)

        assertTrue(result is NetworkResult.Success)
        val success = result as NetworkResult.Success
        assertEquals(201, success.statusCode)
        assertEquals("Nuevo post", success.data.title)
    }

    @Test
    fun getPostById_con_respuesta_404_devuelve_error_con_statusCode_404() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody("{}"))

        val result = repository.getPostById(999)

        assertTrue(result is NetworkResult.Error)
        val error = result as NetworkResult.Error
        assertEquals(404, error.statusCode)
        assertTrue(error.message.contains("404"))
    }

    @Test
    fun getPosts_con_respuesta_500_devuelve_error_con_statusCode_500() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(500).setBody("Error interno"))

        val result = repository.getPosts()

        assertTrue(result is NetworkResult.Error)
        val error = result as NetworkResult.Error
        assertEquals(500, error.statusCode)
        assertTrue(error.message.contains("500"))
    }

    @Test
    fun servidor_apagado_devuelve_error_por_IOException() = runTest {
        mockWebServer.shutdown()

        val result = repository.getPosts()

        assertTrue(result is NetworkResult.Error)
        val error = result as NetworkResult.Error
        assertNotNull(error.cause)
        assertTrue(error.cause is IOException)
    }

    @Test
    fun limitarPorUsuario_conserva_maximo_dos_publicaciones_por_usuario() {
        val lista = listOf(
            Post(userId = 1, id = 1, title = "P1", body = "B1"),
            Post(userId = 1, id = 2, title = "P2", body = "B2"),
            Post(userId = 1, id = 3, title = "P3", body = "B3"),
            Post(userId = 2, id = 11, title = "P11", body = "B11"),
            Post(userId = 2, id = 12, title = "P12", body = "B12"),
            Post(userId = 2, id = 13, title = "P13", body = "B13"),
            Post(userId = 3, id = 21, title = "P21", body = "B21")
        )

        val resultado = repository.limitarPorUsuario(lista)

        assertEquals(5, resultado.size)
        // Usuario 1 debe tener sólo 2
        assertEquals(2, resultado.count { it.userId == 1 })
        assertEquals(listOf(1, 2), resultado.filter { it.userId == 1 }.map { it.id })
        // Usuario 2 debe tener sólo 2
        assertEquals(2, resultado.count { it.userId == 2 })
        assertEquals(listOf(11, 12), resultado.filter { it.userId == 2 }.map { it.id })
        // Usuario 3 debe tener 1
        assertEquals(1, resultado.count { it.userId == 3 })
    }

    @Test
    fun enEspanol_sustituye_texto_para_ids_conocidos_y_conserva_original_en_desconocidos() {
        // Post conocido (id = 1)
        val postConocido = Post(userId = 1, id = 1, title = "Original", body = "Original")
        val postTraducido = postConocido.enEspanol()
        assertEquals("Reseña: 'Cien años de soledad' de Gabriel García Márquez", postTraducido.title)

        // Post desconocido (id = 999)
        val postDesconocido = Post(userId = 1, id = 999, title = "Mi título", body = "Mi cuerpo")
        val postSinCambio = postDesconocido.enEspanol()
        assertEquals("Mi título", postSinCambio.title)
        assertEquals("Mi cuerpo", postSinCambio.body)

        // Comentario conocido (id = 1)
        val comentarioConocido = Comment(postId = 1, id = 1, name = "Orig", email = "test@mail.com", body = "Orig")
        val comentarioTraducido = comentarioConocido.enEspanol()
        assertEquals("Excelente análisis literario", comentarioTraducido.name)
        assertEquals("test@mail.com", comentarioTraducido.email) // Email intacto

        // Comentario desconocido (id = 8888)
        val comentarioDesconocido = Comment(postId = 1, id = 8888, name = "Custom", email = "a@b.com", body = "Custom Body")
        val comentarioSinCambio = comentarioDesconocido.enEspanol()
        assertEquals("Custom", comentarioSinCambio.name)
        assertEquals("Custom Body", comentarioSinCambio.body)
    }
}
