package com.example.mangacollectionapp.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Volume",
    foreignKeys = [ForeignKey(
        entity = Manga::class,
        parentColumns = ["id"],
        childColumns = ["mangaId"],
        onDelete = CASCADE)])
data class MangaVolume (
    @PrimaryKey(autoGenerate = true)
    val volumeId: Int = 0,
    val numberOfTome : String,
    var status : String = "Unowned",
    val mangaId : Int = 0
)