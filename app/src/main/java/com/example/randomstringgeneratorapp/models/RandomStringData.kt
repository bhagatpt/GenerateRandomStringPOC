package com.example.randomstringgeneratorapp.models


data class RandomStringData(
    val value: String,
    val length: Int,
    val created: String,
    val isFavoriteString: Boolean = false
)