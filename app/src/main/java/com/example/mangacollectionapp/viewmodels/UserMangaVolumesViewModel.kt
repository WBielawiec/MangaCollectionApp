package com.example.mangacollectionapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangacollectionapp.database.MangaDao
import com.example.mangacollectionapp.models.MangaVolume
import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

class UserMangaVolumesViewModel(private val dao : MangaDao, mangaId: Int) : ViewModel() {

    private val _volumesListLiveData : MutableLiveData<List<MangaVolume>> = MutableLiveData()
    val volumesListLiveData : LiveData<List<MangaVolume>>
        get() = _volumesListLiveData

    private val _volumeCount : MutableLiveData<String> = MutableLiveData()
    val volumeCount : LiveData<String>
        get() = _volumeCount

    init {
        getAllMangaVolumes(mangaId)
        countMangaVolumes(mangaId)
    }

    fun getAllMangaVolumes(mangaId : Int) = viewModelScope.launch(Dispatchers.IO) {
        _volumesListLiveData.postValue(dao.getAllMangaVolumes(mangaId))
    }

    private suspend fun insertMangaVolumes(volumes : List<MangaVolume>) {
        withContext(Dispatchers.IO) {
            dao.insertVolumes(volumes)
        }
    }

    private fun countMangaVolumes(mangaId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _volumeCount.postValue(dao.countMangaVolumes(mangaId))
        }
    }

    suspend fun insertVolumesWithDataRefresh(volumes : List<MangaVolume>, mangaId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            insertMangaVolumes(volumes)
            getAllMangaVolumes(mangaId)
            countMangaVolumes(mangaId)
        }
    }

    suspend fun deleteVolumes(volumes : List<MangaVolume>, mangaId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteMangaVolumes(volumes)
            getAllMangaVolumes(mangaId)
            countMangaVolumes(mangaId)
        }
    }

    suspend fun updateVolumes(volumes : List<MangaVolume>, mangaId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateMangaVolumes(volumes)
            getAllMangaVolumes(mangaId)
        }
    }
}