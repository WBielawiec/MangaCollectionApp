package com.example.mangacollectionapp.viewmodels

import androidx.lifecycle.*
import com.example.mangacollectionapp.database.MangaDao
import com.example.mangacollectionapp.models.Manga
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserMangaListViewModel(private val dao : MangaDao) : ViewModel() {

    val mangaList : MutableLiveData<List<Manga>> = MutableLiveData()

//    private val _mangaListLiveData = MutableLiveData<List<Manga>>()
//    val mangaList : LiveData<List<Manga>> get() = _mangaListLiveData

    init {

    }

    fun getAllMangas() = viewModelScope.launch(Dispatchers.IO) {
        mangaList.postValue(dao.getAllMangas())
    }

    fun getAllCompletedMangas() = viewModelScope.launch(Dispatchers.IO) {
        mangaList.postValue(dao.getAllCompletedMangas())
    }

    fun getAllInProgressMangas() = viewModelScope.launch(Dispatchers.IO) {
        mangaList.postValue(dao.getAllInProgressMangas())
    }

    fun getAllWantedMangas() = viewModelScope.launch(Dispatchers.IO) {
        mangaList.postValue(dao.getAllWantedMangas())
    }

    fun deleteManga(manga : Manga)  {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteManga(manga)
            getAllMangas()
        }
    }

    fun updateManga(manga: Manga) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateManga(manga)
            getAllMangas()
        }
    }

    fun getSingleMangaFromList(position : Int) : Manga {
        return mangaList.value!![position]
    }

}

