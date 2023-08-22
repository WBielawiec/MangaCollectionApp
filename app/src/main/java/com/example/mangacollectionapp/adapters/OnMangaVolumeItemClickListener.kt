package com.example.mangacollectionapp.adapters

import android.view.View
import com.example.mangacollectionapp.models.MangaVolume

interface OnMangaVolumeItemClickListener {

    fun onItemClick( item : MangaVolume)

    fun onItemLongClick( item : MangaVolume, position : Int)
}