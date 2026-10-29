package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Comment
import com.example.data.model.Post
import com.example.data.model.User
import com.example.data.repository.MAX_SERVER_POST_ID
import com.example.data.repository.NetworkResult
import com.example.data.repository.PostRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState {
    data object Cargando : UiState
    data class Exito(val lista: List<Post>) : UiState
    data object Vacio : UiState
    data class Error(val mensaje: String) : UiState
}

sealed interface DetailUiState {
    data object Cargando : DetailUiState
    data class Exito(val post: Post, val comentarios: List<Comment>) : DetailUiState
    data class Error(val mensaje: String) : DetailUiState
}

sealed interface UsersUiState {
    data object Cargando : UsersUiState
    data class Exito(val usuarios: List<User>) : UsersUiState
    data class Error(val mensaje: String) : UsersUiState
}

class PostViewModel(
    private val repository: PostRepository = PostRepository()
) : ViewModel() {

    private val usuariosPredeterminados = listOf(
        User(1, "Leanne Graham", "Bret", "Sincere@april.biz"),
        User(2, "Ervin Howell", "Antonette", "Shanna@melissa.tv"),
        User(3, "Clementine Bauch", "Samantha", "Nathan@yesenia.net"),
        User(4, "Patricia Lebsack", "Karianne", "Julianne.OConner@kory.org"),
        User(5, "Chelsey Dietrich", "Kamren", "Lucio_Hettinger@annie.ca"),
        User(6, "Mrs. Dennis Schulist", "Leopoldo_Corkery", "Karley_Dach@jasper.info"),
        User(7, "Kurtis Weissnat", "Elwyn.Skiles", "Telly.Hoeger@billy.biz"),
        User(8, "Nicholas Runolfsdottir V", "Maxime_Nienow", "Sherwood@rosamond.me"),
        User(9, "Glenna Reichert", "Delphine", "Chaim_McDermott@dana.io"),
        User(10, "Clementina DuBuque", "Moriah.Stanton", "Rey.Padberg@karina.biz")
    )

    private var usuariosCargadosDeApi = false

    private val _currentUser = MutableStateFlow<User>(usuariosPredeterminados.first())
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _availableUsers = MutableStateFlow<List<User>>(usuariosPredeterminados)
    val availableUsers: StateFlow<List<User>> = _availableUsers.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Cargando)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Cargando)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    private val _usersUiState = MutableStateFlow<UsersUiState>(UsersUiState.Exito(usuariosPredeterminados))
    val usersUiState: StateFlow<UsersUiState> = _usersUiState.asStateFlow()

    private val _selectedUserId = MutableStateFlow<Int?>(null)
    val selectedUserId: StateFlow<Int?> = _selectedUserId.asStateFlow()

    private val _selectedUserName = MutableStateFlow<String?>(null)
    val selectedUserName: StateFlow<String?> = _selectedUserName.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _userMessage = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    private var cargaListaJob: Job? = null
    private var cargaDetalleJob: Job? = null
    private var cargaUsuariosJob: Job? = null

    private val inMemoryPosts = mutableListOf<Post>()
    private var nextGeneratedId = MAX_SERVER_POST_ID + 1

    init {
        cargarUsuarios(forzar = false)
        cargarPublicaciones(userId = null)
    }

    fun cambiarUsuarioActivo(user: User) {
        _currentUser.value = user
        viewModelScope.launch {
            _userMessage.emit("Sesión activa como: ${user.name} (Usuario #${user.id})")
        }
    }

    fun esAutorDePublicacion(post: Post): Boolean {
        return post.userId == _currentUser.value.id
    }

    fun esAutorDePublicacion(postId: Int): Boolean {
        val post = obtenerPublicacionPorId(postId) ?: run {
            val detail = _detailUiState.value
            if (detail is DetailUiState.Exito && detail.post.id == postId) detail.post else null
        }
        return post?.userId == _currentUser.value.id
    }

    private fun formatHttpStatus(statusCode: Int?): String {
        return when (statusCode) {
            200 -> "200 OK"
            201 -> "201 Created"
            204 -> "204 No Content"
            400 -> "400 Bad Request"
            401 -> "401 Unauthorized"
            403 -> "403 Forbidden"
            404 -> "404 Not Found"
            500 -> "500 Internal Server Error"
            null -> "200 OK"
            else -> "$statusCode"
        }
    }

    fun cargarPublicaciones(userId: Int?, userName: String? = null) {
        _selectedUserId.value = userId
        if (userId == null) {
            _selectedUserName.value = null
        } else if (userName != null) {
            _selectedUserName.value = userName
        }

        _uiState.value = UiState.Cargando

        cargaListaJob?.cancel()
        cargaListaJob = viewModelScope.launch {
            when (val result = repository.getPosts(userId)) {
                is NetworkResult.Success -> {
                    val apiPosts = result.data

                    if (userId == null) {
                        val localCreated = inMemoryPosts.filter { it.id > MAX_SERVER_POST_ID }
                        inMemoryPosts.clear()
                        inMemoryPosts.addAll(localCreated)
                        inMemoryPosts.addAll(apiPosts)
                        emitCurrentListState(inMemoryPosts)
                    } else {
                        val localMatching = inMemoryPosts.filter { it.userId == userId && it.id > MAX_SERVER_POST_ID }
                        val combined = (localMatching + apiPosts).distinctBy { it.id }
                        emitCurrentListState(combined)
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.value = UiState.Error(result.message)
                    val statusStr = result.statusCode?.let { " - ${formatHttpStatus(it)}" } ?: ""
                    _userMessage.emit("Error al cargar reseñas (HTTP GET$statusStr): ${result.message}")
                }
            }
        }
    }

    fun reintentarCarga() {
        cargarPublicaciones(_selectedUserId.value, _selectedUserName.value)
    }

    fun cargarUsuarios(forzar: Boolean = false) {
        if (!forzar && usuariosCargadosDeApi) {
            return
        }

        _usersUiState.value = UsersUiState.Cargando
        cargaUsuariosJob?.cancel()
        cargaUsuariosJob = viewModelScope.launch {
            when (val result = repository.getUsers()) {
                is NetworkResult.Success -> {
                    usuariosCargadosDeApi = true
                    val usuarios = result.data
                    _availableUsers.value = usuarios
                    _usersUiState.value = UsersUiState.Exito(usuarios)
                    
                    if (usuarios.none { it.id == _currentUser.value.id }) {
                        usuarios.firstOrNull()?.let { _currentUser.value = it }
                    }
                }
                is NetworkResult.Error -> {
                    _usersUiState.value = UsersUiState.Error(result.message)
                    val statusStr = result.statusCode?.let { " - ${formatHttpStatus(it)}" } ?: ""
                    _userMessage.emit("Error al obtener usuarios (HTTP GET$statusStr): ${result.message}")
                }
            }
        }
    }

    fun cargarDetallePublicacion(postId: Int, forzar: Boolean = false) {
        val currentState = _detailUiState.value
        if (!forzar && currentState is DetailUiState.Exito && currentState.post.id == postId) {
            return
        }

        _detailUiState.value = DetailUiState.Cargando

        cargaDetalleJob?.cancel()
        cargaDetalleJob = viewModelScope.launch {
            val cachedPost = inMemoryPosts.find { it.id == postId }

            val postDeferred = async {
                cachedPost?.let { NetworkResult.Success(it) } ?: repository.getPostById(postId)
            }
            val commentsDeferred = async {
                repository.getPostComments(postId)
            }

            val postResult = postDeferred.await()
            val commentsResult = commentsDeferred.await()

            if (postResult is NetworkResult.Success) {
                val post = postResult.data
                val comments = if (commentsResult is NetworkResult.Success) {
                    commentsResult.data
                } else {
                    emptyList()
                }
                _detailUiState.value = DetailUiState.Exito(post, comments)
            } else if (postResult is NetworkResult.Error) {
                _detailUiState.value = DetailUiState.Error(postResult.message)
                val statusStr = postResult.statusCode?.let { " - ${formatHttpStatus(it)}" } ?: ""
                _userMessage.emit("Error al cargar detalle (HTTP GET$statusStr): ${postResult.message}")
            }
        }
    }

    fun crearPublicacion(post: Post, onSuccess: () -> Unit) {
        if (_isSubmitting.value) return
        _isSubmitting.value = true

        val activeUser = _currentUser.value
        val postConUsuarioActivo = post.copy(userId = activeUser.id)

        viewModelScope.launch {
            when (val result = repository.createPost(postConUsuarioActivo)) {
                is NetworkResult.Success -> {
                    _isSubmitting.value = false

                    val assignedId = nextGeneratedId++
                    val nuevoPost = postConUsuarioActivo.copy(id = assignedId)

                    inMemoryPosts.add(0, nuevoPost)
                    filtrarYEmitirSegunUsuarioActual()

                    val statusStr = formatHttpStatus(result.statusCode)
                    _userMessage.emit("Reseña #${nuevoPost.id} creada con éxito por ${activeUser.name} (HTTP POST $statusStr)")
                    onSuccess()
                }
                is NetworkResult.Error -> {
                    _isSubmitting.value = false
                    val statusStr = result.statusCode?.let { " - ${formatHttpStatus(it)}" } ?: ""
                    _userMessage.emit("Error al crear reseña (HTTP POST$statusStr): ${result.message}")
                }
            }
        }
    }

    fun editarPublicacion(post: Post, onSuccess: () -> Unit) {
        val activeUser = _currentUser.value
        val originalPost = obtenerPublicacionPorId(post.id) ?: run {
            val detail = _detailUiState.value
            if (detail is DetailUiState.Exito && detail.post.id == post.id) detail.post else null
        }

        val targetUserId = originalPost?.userId ?: post.userId

        if (targetUserId != activeUser.id) {
            viewModelScope.launch {
                _userMessage.emit("No tienes permiso para editar esta reseña. Pertenece al Usuario #$targetUserId.")
            }
            return
        }

        if (_isSubmitting.value) return
        _isSubmitting.value = true

        val postParaEnviar = post.copy(userId = targetUserId)

        viewModelScope.launch {
            val result = if (postParaEnviar.id > MAX_SERVER_POST_ID) {
                NetworkResult.Success(postParaEnviar)
            } else {
                repository.updatePost(postParaEnviar.id, postParaEnviar)
            }

            when (result) {
                is NetworkResult.Success -> {
                    _isSubmitting.value = false
                    val postActualizado = result.data.copy(userId = targetUserId)

                    val index = inMemoryPosts.indexOfFirst { it.id == post.id }
                    if (index != -1) {
                        inMemoryPosts[index] = postActualizado
                    }

                    val currentDetail = _detailUiState.value
                    if (currentDetail is DetailUiState.Exito && currentDetail.post.id == post.id) {
                        _detailUiState.value = currentDetail.copy(post = postActualizado)
                    }

                    filtrarYEmitirSegunUsuarioActual()

                    val statusStr = formatHttpStatus(result.statusCode)
                    _userMessage.emit("Reseña #${post.id} actualizada con éxito (HTTP PUT $statusStr)")
                    onSuccess()
                }
                is NetworkResult.Error -> {
                    _isSubmitting.value = false
                    val statusStr = result.statusCode?.let { " - ${formatHttpStatus(it)}" } ?: ""
                    _userMessage.emit("Error al actualizar reseña (HTTP PUT$statusStr): ${result.message}")
                }
            }
        }
    }

    fun eliminarPublicacion(postId: Int, onDeleted: (() -> Unit)? = null) {
        val post = obtenerPublicacionPorId(postId) ?: run {
            val detail = _detailUiState.value
            if (detail is DetailUiState.Exito && detail.post.id == postId) detail.post else null
        }

        val activeUser = _currentUser.value
        if (post != null && post.userId != activeUser.id) {
            viewModelScope.launch {
                _userMessage.emit("No tienes permiso para eliminar esta reseña. Pertenece al Usuario #${post.userId}.")
            }
            return
        }

        viewModelScope.launch {
            val result = if (postId > MAX_SERVER_POST_ID) {
                NetworkResult.Success(Unit)
            } else {
                repository.deletePost(postId)
            }

            when (result) {
                is NetworkResult.Success -> {
                    inMemoryPosts.removeAll { it.id == postId }
                    filtrarYEmitirSegunUsuarioActual()

                    val statusStr = formatHttpStatus(result.statusCode)
                    _userMessage.emit("Reseña #$postId eliminada con éxito (HTTP DELETE $statusStr)")
                    onDeleted?.invoke()
                }
                is NetworkResult.Error -> {
                    val statusStr = result.statusCode?.let { " - ${formatHttpStatus(it)}" } ?: ""
                    _userMessage.emit("Error al eliminar reseña (HTTP DELETE$statusStr): ${result.message}")
                }
            }
        }
    }

    fun obtenerPublicacionPorId(postId: Int): Post? {
        return inMemoryPosts.find { it.id == postId }
    }

    private fun filtrarYEmitirSegunUsuarioActual() {
        val currentUserId = _selectedUserId.value
        val filtered = if (currentUserId != null) {
            inMemoryPosts.filter { it.userId == currentUserId }
        } else {
            inMemoryPosts
        }
        emitCurrentListState(filtered)
    }

    private fun emitCurrentListState(posts: List<Post>) {
        _uiState.value = if (posts.isEmpty()) {
            UiState.Vacio
        } else {
            UiState.Exito(posts)
        }
    }
}
