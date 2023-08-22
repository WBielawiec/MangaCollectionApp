package com.example.mangacollectionapp.adapters

import com.example.mangacollectionapp.models.Manga


interface OnUserMangaListItemClickListener {

    fun onItemClick( item : Manga )

    fun onItemLongClick( item : Manga, position : Int )
}