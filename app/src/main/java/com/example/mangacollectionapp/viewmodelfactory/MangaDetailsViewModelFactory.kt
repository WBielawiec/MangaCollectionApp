package com.example.mangacollectionapp.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mangacollectionapp.database.MangaDao
import com.example.mangacollectionapp.viewmodels.MangaDetailsViewModel

class MangaDetailsViewModelFactory(private val dao : MangaDao) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MangaDetailsViewModel::class.java)) {
            return MangaDetailsViewModel(dao) as T
        }

        throw java.lang.IllegalArgumentException()
    }
}