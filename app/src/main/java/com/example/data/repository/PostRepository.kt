package com.example.data.repository

import android.util.Log
import com.example.data.mapper.enEspanol
import com.example.data.model.Comment
import com.example.data.model.Post
import com.example.data.model.User
import com.example.data.remote.PostApi
import com.example.data.remote.RetrofitClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

const val MAX_SERVER_POST_ID = 100
const val POSTS_POR_USUARIO = 2

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T, val statusCode: Int = 200) : NetworkResult<T>()
    data class Error(
        val message: String,
        val statusCode: Int? = null,
        val cause: Throwable? = null
    ) : NetworkResult<Nothing>()
}

class PostRepository(
    private val api: PostApi = RetrofitClient.api,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    internal fun limitarPorUsuario(lista: List<Post>): List<Post> {
        val contadorPorUsuario = mutableMapOf<Int, Int>()
        val resultado = mutableListOf<Post>()
        for (post in lista) {
            val contadorActual = contadorPorUsuario.getOrDefault(post.userId, 0)
            if (contadorActual < POSTS_POR_USUARIO) {
                contadorPorUsuario[post.userId] = contadorActual + 1
                resultado.add(post)
            }
        }
        return resultado
    }

    suspend fun getPosts(userId: Int? = null): NetworkResult<List<Post>> = withContext(ioDispatcher) {
        try {
            val response = api.getPosts(userId)
            val statusCode = response.code()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val publicacionesLimitadas = limitarPorUsuario(body)
                    val publicacionesEnEspanol = publicacionesLimitadas.map { it.enEspanol() }
                    NetworkResult.Success(publicacionesEnEspanol, statusCode)
                } else {
                    NetworkResult.Error("El servidor respondió con éxito pero el contenido está vacío.", statusCode)
                }
            } else {
                mapHttpError(statusCode, response.message())
            }
        } catch (e: IOException) {
            mapIoException(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            NetworkResult.Error(
                message = "Ocurrió un error al procesar las publicaciones: ${e.localizedMessage ?: "Error desconocido"}",
                cause = e
            )
        }
    }

    suspend fun getPostById(id: Int): NetworkResult<Post> = withContext(ioDispatcher) {
        try {
            val response = api.getPost(id)
            val statusCode = response.code()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body.enEspanol(), statusCode)
                } else {
                    NetworkResult.Error("El servidor devolvió un registro vacío.", statusCode)
                }
            } else {
                mapHttpError(statusCode, response.message())
            }
        } catch (e: IOException) {
            mapIoException(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            NetworkResult.Error(
                message = "Error inesperado al consultar la publicación: ${e.localizedMessage ?: "Error desconocido"}",
                cause = e
            )
        }
    }

    suspend fun getPostComments(postId: Int): NetworkResult<List<Comment>> = withContext(ioDispatcher) {
        try {
            val response = api.getComments(postId)
            val statusCode = response.code()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val comentariosEnEspanol = body.map { it.enEspanol() }
                    NetworkResult.Success(comentariosEnEspanol, statusCode)
                } else {
                    NetworkResult.Success(emptyList(), statusCode)
                }
            } else {
                mapHttpError(statusCode, response.message())
            }
        } catch (e: IOException) {
            mapIoException(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            NetworkResult.Error(
                message = "Error al consultar los comentarios: ${e.localizedMessage ?: "Error desconocido"}",
                cause = e
            )
        }
    }

    suspend fun getUsers(): NetworkResult<List<User>> = withContext(ioDispatcher) {
        safeApiCall { api.getUsers() }
    }

    suspend fun createPost(post: Post): NetworkResult<Post> = withContext(ioDispatcher) {
        try {
            val response = api.createPost(post)
            val statusCode = response.code()
            if (response.isSuccessful && statusCode == 201) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body, statusCode)
                } else {
                    NetworkResult.Error("La publicación se creó (201), pero el cuerpo de respuesta vino vacío.", statusCode)
                }
            } else if (response.isSuccessful) {
                val body = response.body() ?: post
                NetworkResult.Success(body, statusCode)
            } else {
                mapHttpError(statusCode, response.message())
            }
        } catch (e: IOException) {
            mapIoException(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            NetworkResult.Error(
                message = "Error inesperado al crear la publicación: ${e.localizedMessage ?: "Fallo desconocido"}",
                cause = e
            )
        }
    }

    suspend fun updatePost(id: Int, post: Post): NetworkResult<Post> = withContext(ioDispatcher) {
        safeApiCall { api.updatePost(id, post) }
    }

    suspend fun deletePost(id: Int): NetworkResult<Unit> = withContext(ioDispatcher) {
        try {
            val response = api.deletePost(id)
            val statusCode = response.code()
            if (response.isSuccessful) {
                NetworkResult.Success(Unit, statusCode)
            } else {
                mapHttpError(statusCode, response.message())
            }
        } catch (e: IOException) {
            mapIoException(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            NetworkResult.Error(
                message = "Error inesperado al eliminar la publicación: ${e.localizedMessage ?: "Fallo desconocido"}",
                cause = e
            )
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): NetworkResult<T> {
        return try {
            val response = apiCall()
            val statusCode = response.code()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body, statusCode)
                } else {
                    NetworkResult.Error("El servidor respondió con éxito pero el contenido está vacío.", statusCode)
                }
            } else {
                mapHttpError(statusCode, response.message())
            }
        } catch (e: IOException) {
            mapIoException(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            NetworkResult.Error(
                message = "Ocurrió un error al procesar la respuesta: ${e.localizedMessage ?: "Error desconocido"}",
                cause = e
            )
        }
    }

    private fun mapIoException(e: IOException): NetworkResult.Error {
        try {
            Log.e("PostRepository", "Fallo de conexión en llamada de red: ${e.message}", e)
        } catch (_: Throwable) {
        }
        val mensaje = when (e) {
            is UnknownHostException -> "No se pudo resolver el servidor. Revisa que el dispositivo tenga internet real."
            is SocketTimeoutException -> "El servidor tardó demasiado en responder."
            is SSLException -> "Error de conexión segura. Revisa la fecha y hora del dispositivo."
            else -> "Sin conexión a Internet. Revisa tu red e inténtalo de nuevo."
        }
        return NetworkResult.Error(message = mensaje, cause = e)
    }

    private fun mapHttpError(statusCode: Int, rawMessage: String?): NetworkResult.Error {
        val readableMessage = when (statusCode) {
            404 -> "Recurso no encontrado (Código 404). Es posible que la publicación no exista o haya sido eliminada."
            in 400..499 -> "Error del cliente en la solicitud (Código $statusCode). Verifica la información enviada."
            in 500..599 -> "Error interno del servidor (Código $statusCode). El servicio de JSONPlaceholder tiene problemas momentáneos."
            else -> "Respuesta inesperada del servidor (Código $statusCode: ${rawMessage ?: "Sin detalle"})."
        }
        return NetworkResult.Error(message = readableMessage, statusCode = statusCode)
    }
}
