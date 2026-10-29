package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.Post
import com.example.ui.viewmodel.PostViewModel

/**
 * Pantalla de formulario para crear (POST) o editar (PUT) una reseña de libro.
 * Asigna la autoría al usuario actualmente activo en la app y restringe la edición a los autores.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFormScreen(
    postId: Int,
    viewModel: PostViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEditMode = postId > 0
    val isSubmitting by viewModel.isSubmitting.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val availableUsers by viewModel.availableUsers.collectAsStateWithLifecycle()

    var userId by rememberSaveable { mutableIntStateOf(currentUser.id) }
    var title by rememberSaveable { mutableStateOf("") }
    var body by rememberSaveable { mutableStateOf("") }

    var titleError by rememberSaveable { mutableStateOf<String?>(null) }
    var bodyError by rememberSaveable { mutableStateOf<String?>(null) }

    var precargado by rememberSaveable { mutableStateOf(false) }
    var dropdownExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(postId, currentUser.id) {
        if (isEditMode && !precargado) {
            val existingPost = viewModel.obtenerPublicacionPorId(postId)
            if (existingPost != null) {
                userId = existingPost.userId
                title = existingPost.title
                body = existingPost.body
                precargado = true
            }
        } else if (!isEditMode) {
            userId = currentUser.id
        }
    }

    val isAuthorAllowed = if (isEditMode) (userId == currentUser.id) else true

    val emptyTitleMsg = stringResource(R.string.error_empty_title)
    val emptyBodyMsg = stringResource(R.string.error_empty_body)

    fun validarYEnviar() {
        if (!isAuthorAllowed) return

        var valido = true

        if (title.trim().isBlank()) {
            titleError = emptyTitleMsg
            valido = false
        } else {
            titleError = null
        }

        if (body.trim().isBlank()) {
            bodyError = emptyBodyMsg
            valido = false
        } else {
            bodyError = null
        }

        if (valido) {
            val post = Post(
                userId = userId,
                id = if (isEditMode) postId else 0,
                title = title.trim(),
                body = body.trim()
            )

            if (isEditMode) {
                viewModel.editarPublicacion(post) {
                    onNavigateBack()
                }
            } else {
                viewModel.crearPublicacion(post) {
                    onNavigateBack()
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) {
                            stringResource(R.string.title_edit_post)
                        } else {
                            stringResource(R.string.title_new_post)
                        },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("form_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isAuthorAllowed) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = if (isAuthorAllowed) Icons.Default.AccountCircle else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isAuthorAllowed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isEditMode) "Modo Edición" else "Publicando como Usuario Activo:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isAuthorAllowed) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = if (isAuthorAllowed) {
                                "${currentUser.name} (@${currentUser.username}) - Usuario #${currentUser.id}"
                            } else {
                                "No puedes editar esta reseña porque pertenece al Usuario #$userId. Tu usuario activo es ${currentUser.name} (#${currentUser.id})."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isAuthorAllowed) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            val dropdownEnabled = !isEditMode && !isSubmitting
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded && dropdownEnabled,
                onExpandedChange = { if (dropdownEnabled) dropdownExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                val selectedUserObj = availableUsers.find { it.id == userId } ?: currentUser
                OutlinedTextField(
                    value = "${selectedUserObj.name} (Usuario #${selectedUserObj.id})",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.field_user_id)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable, enabled = dropdownEnabled)
                        .fillMaxWidth()
                        .testTag("user_id_dropdown"),
                    enabled = dropdownEnabled
                )

                ExposedDropdownMenu(
                    expanded = dropdownExpanded && dropdownEnabled,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    availableUsers.forEach { user ->
                        DropdownMenuItem(
                            text = { Text("${user.id}. ${user.name}") },
                            onClick = {
                                userId = user.id
                                viewModel.cambiarUsuarioActivo(user)
                                dropdownExpanded = false
                            },
                            modifier = Modifier.testTag("dropdown_item_user_${user.id}")
                        )
                    }
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (titleError != null && it.isNotBlank()) titleError = null
                },
                label = { Text(stringResource(R.string.field_title)) },
                placeholder = { Text("Ej: Reseña de 'Cien años de soledad'") },
                isError = titleError != null,
                supportingText = {
                    titleError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = false,
                maxLines = 3,
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                enabled = isAuthorAllowed && !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("title_input")
            )

            OutlinedTextField(
                value = body,
                onValueChange = {
                    body = it
                    if (bodyError != null && it.isNotBlank()) bodyError = null
                },
                label = { Text(stringResource(R.string.field_body)) },
                placeholder = { Text("Escribe aquí tu análisis y opinión sobre la obra literaria...") },
                isError = bodyError != null,
                supportingText = {
                    bodyError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                minLines = 5,
                maxLines = 10,
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                enabled = isAuthorAllowed && !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("body_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { validarYEnviar() },
                enabled = isAuthorAllowed && !isSubmitting,
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_button")
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Enviando a JSONPlaceholder…")
                } else {
                    Icon(
                        imageVector = if (isAuthorAllowed) Icons.Default.Check else Icons.Default.Lock,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (!isAuthorAllowed) {
                            "Sin permisos de edición"
                        } else if (isEditMode) {
                            stringResource(R.string.action_save)
                        } else {
                            stringResource(R.string.action_create)
                        },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
