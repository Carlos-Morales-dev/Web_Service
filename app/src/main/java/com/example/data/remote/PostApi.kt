package com.example.data.remote

import com.example.data.model.Comment
import com.example.data.model.Post
import com.example.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {

    @GET("posts")
    suspend fun getPosts(
        @Query("userId") userId: Int? = null
    ): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getPost(
        @Path("id") id: Int
    ): Response<Post>

    @GET("posts/{id}/comments")
    suspend fun getComments(
        @Path("id") id: Int
    ): Response<List<Comment>>

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @POST("posts")
    suspend fun createPost(
        @Body post: Post
    ): Response<Post>

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body post: Post
    ): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Int
    ): Response<Unit>
}
