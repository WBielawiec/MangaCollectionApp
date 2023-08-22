package com.example.mangacollectionapp.repositories

import com.example.mangacollectionapp.api.ApiService

class MangaAPIRepository(private val apiService: ApiService) {

    suspend fun getMangas(q : String) = apiService.fetchMangas(q)
}