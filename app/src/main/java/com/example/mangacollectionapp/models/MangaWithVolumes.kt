package com.example.mangacollectionapp.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

//@Entity(tableName = "MangaWithVolumes")
data class MangaWithVolumes (
    @Embedded
    val manga : Manga,
    @Relation(
        parentColumn = "id",
        entityColumn = "volumeId"
    )
    val volumeList: List<MangaVolume>

)