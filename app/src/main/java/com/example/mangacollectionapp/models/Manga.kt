package com.example.mangacollectionapp.models


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Manga")
@Parcelize
data class Manga (
    val title: String?,
    val chapters: String?,
    val volumes: Int?,
    val status: String?,
    val publish_from: String?,
    val publish_to: String?,
    val synopsis : String?,
    val authors : String?,
    val genres : String?,
    val image : String?,
    var userStatus : String? = "Wanted",
    @PrimaryKey(autoGenerate = false)
    val id: Int
) : Parcelable