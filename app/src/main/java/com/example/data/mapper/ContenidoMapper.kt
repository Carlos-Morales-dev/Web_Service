package com.example.data.mapper

import com.example.data.local.ContenidoEs
import com.example.data.model.Comment
import com.example.data.model.Post

/**
 * Funciones de extensión para transformar los textos de prueba en pseudo-latín ("lorem ipsum")
 * devueltos por JSONPlaceholder a textos contextualizados en español.
 */

/**
 * Adapta el título y cuerpo de una [Post] al español si existe una traducción para su [Post.id].
 * Preserva inalterados el [Post.id] y el [Post.userId].
 */
internal fun Post.enEspanol(): Post {
    val traduccion = ContenidoEs.publicaciones[id] ?: return this
    return copy(
        title = traduccion.first,
        body = traduccion.second
    )
}

/**
 * Adapta el asunto ([Comment.name]) y cuerpo ([Comment.body]) de un [Comment] al español
 * si existe una traducción para su [Comment.id].
 * Preserva inalterados el [Comment.postId], [Comment.id] y [Comment.email].
 */
internal fun Comment.enEspanol(): Comment {
    val traduccion = ContenidoEs.comentarios[id] ?: return this
    return copy(
        name = traduccion.first,
        body = traduccion.second
    )
}
