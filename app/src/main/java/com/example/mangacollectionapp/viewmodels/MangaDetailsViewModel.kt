package com.example.mangacollectionapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangacollectionapp.database.MangaDao
import com.example.mangacollectionapp.models.Manga
import com.example.mangacollectionapp.models.MangaVolume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MangaDetailsViewModel(private val dao : MangaDao) : ViewModel() {

    fun insertManga(manga : Manga) = viewModelScope.launch(Dispatchers.IO) {
        dao.insertManga(manga)
    }

    fun deleteManga(manga : Manga) = viewModelScope.launch(Dispatchers.IO) {
        dao.deleteManga(manga)
    }

    fun insertMangaWithVolumes(manga: Manga, volumes: List<MangaVolume>) = viewModelScope.launch(Dispatchers.IO) {
        dao.insertMangaWithVolumes(manga, volumes)
    }


}