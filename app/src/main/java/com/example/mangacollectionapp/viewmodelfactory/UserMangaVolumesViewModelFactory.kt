package com.example.mangacollectionapp.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mangacollectionapp.database.MangaDao
import com.example.mangacollectionapp.viewmodels.UserMangaListViewModel
import com.example.mangacollectionapp.viewmodels.UserMangaVolumesViewModel

class UserMangaVolumesViewModelFactory(private val dao : MangaDao, private val mangaId: Int) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserMangaVolumesViewModel::class.java)) {
            return UserMangaVolumesViewModel(dao, mangaId) as T
        }

        throw java.lang.IllegalArgumentException()
    }
}