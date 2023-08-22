package com.example.mangacollectionapp.database

sealed interface MangaEvent {
    object SaveManga: MangaEvent

}