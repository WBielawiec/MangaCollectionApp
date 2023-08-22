package com.example.mangacollectionapp.adapters

import com.example.mangacollectionapp.api.MangaAPIObject

interface OnSearchItemClickListener {
    fun onItemClick( item : MangaAPIObject)
}