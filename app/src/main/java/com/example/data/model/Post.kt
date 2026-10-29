package com.example.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String
)
