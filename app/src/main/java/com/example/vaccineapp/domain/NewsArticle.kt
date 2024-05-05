package com.example.vaccineapp.domain


import kotlinx.serialization.Serializable

@Serializable
data class NewsArticle(
    val source: Source?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)
@Serializable
data class Source(
    val id: String?,
    val name: String
)

@Serializable
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>
)