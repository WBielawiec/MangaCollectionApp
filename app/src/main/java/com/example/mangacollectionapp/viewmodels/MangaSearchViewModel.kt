package com.example.mangacollectionapp.viewmodels

import androidx.lifecycle.*
import com.example.mangacollectionapp.api.ApiClient
import com.example.mangacollectionapp.api.MangaAPIObject
import com.example.mangacollectionapp.api.ApiResult
import com.example.mangacollectionapp.repositories.MangaAPIRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MangaSearchViewModel(private val repository: MangaAPIRepository = MangaAPIRepository(ApiClient.apiService)) : ViewModel() {

    private val _mangasLiveData = MutableLiveData<ApiResult<List<MangaAPIObject>?>>()
    val mangasLiveData: LiveData<ApiResult<List<MangaAPIObject>?>>
        get() = _mangasLiveData

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String>
        get() = _searchText

    fun changeSearchText(value: String) {
        _searchText.value = value
    }

    init {
        searchText.observeForever {
            fetchMangas()
        }
    }

    fun fetchMangas() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = repository.getMangas(searchText.value!!)
                _mangasLiveData.postValue(ApiResult.Success(client.data))
            } catch (e:java.lang.Exception) {
                _mangasLiveData.postValue(ApiResult.Error(e.message.toString(), null))
            }
        }
    }
}