package com.example.mangacollectionapp.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mangacollectionapp.database.MangaDao
import com.example.mangacollectionapp.viewmodels.UserMangaListViewModel

class UserMangaListViewModelFactory(private val dao : MangaDao) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserMangaListViewModel::class.java)) {
            return UserMangaListViewModel(dao) as T
        }

        throw java.lang.IllegalArgumentException()
    }
}