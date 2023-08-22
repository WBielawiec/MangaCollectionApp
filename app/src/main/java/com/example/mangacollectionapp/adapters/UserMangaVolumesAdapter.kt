package com.example.mangacollectionapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.mangacollectionapp.R
import com.example.mangacollectionapp.models.MangaVolume

class UserMangaVolumesAdapter(private val volumesList: List<MangaVolume>, private val onItemClickListener : OnMangaVolumeItemClickListener) : RecyclerView.Adapter<UserMangaVolumesAdapter.UserMangaVolumesViewHolder>() {

    inner class UserMangaVolumesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(volume : MangaVolume) {
            val volumeTitle = itemView.findViewById<TextView>(R.id.tome_number_manga_volume_item_text_view)
            val status = itemView.findViewById<TextView>(R.id.tome_status_manga_volume_item_text_view)
            val background = itemView.findViewById<ConstraintLayout>(R.id.manga_volume_item_card)

            volumeTitle.text = volume.numberOfTome
            status.text = volume.status

            when (volume.status) {
                "Owned" -> {
                    background.setBackgroundResource(R.color.owned_color)
                }
                "Unowned" -> {
                    background.setBackgroundResource(R.color.unowned_color)
                }
                "Preorder" -> {
                    background.setBackgroundResource(R.color.preorder_color)
                }
                else -> {
                    background.setBackgroundResource(android.R.color.transparent)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMangaVolumesViewHolder {
        return UserMangaVolumesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.manga_volume_item, parent, false))
    }

    override fun getItemCount(): Int {
        return volumesList.size
    }

    private val _selectedItems : MutableList<Int> = mutableListOf()
    val selectedItems : MutableLiveData<MutableList<Int>> = MutableLiveData()
    var isAnyItemSelected = false;

    override fun onBindViewHolder(holder: UserMangaVolumesViewHolder, position: Int) {
        holder.bindData(volumesList[position])

        isAnyItemSelected = _selectedItems.isNotEmpty()

        val isCurrentItemSelected = _selectedItems.contains(position)

        if (isCurrentItemSelected) {
            holder.itemView.setBackgroundColor(Color.GRAY)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemView.setOnClickListener {
            if(isAnyItemSelected) {
                if (isCurrentItemSelected) {
                    _selectedItems.remove(position)
                } else {
                    _selectedItems.add(position)
                }
                selectedItems.postValue(_selectedItems)

                notifyItemChanged(position)
            }
        }

        holder.itemView.setOnLongClickListener {

            if (isCurrentItemSelected) {
                _selectedItems.remove(position)
            } else {
                _selectedItems.add(position)
            }
            selectedItems.postValue(_selectedItems)

            notifyItemChanged(position)

            true
        }
    }

    fun getVolumesList() : List<MangaVolume> {
        return volumesList
    }

    fun clearSelectedItems() {
        _selectedItems.clear()
        notifyItemRangeChanged(0, volumesList.size - 1)
        selectedItems.postValue(_selectedItems)
    }

    private fun selectAllItems() {
        _selectedItems.clear()
        volumesList.forEach {
            _selectedItems.add(volumesList.indexOf(it))
            notifyItemChanged(volumesList.indexOf(it))
        }
        selectedItems.postValue(_selectedItems)
    }

    fun changeAllItemSelection() {
        if(_selectedItems.size != volumesList.size) {
            selectAllItems()
        } else {
            clearSelectedItems()
        }
    }

}