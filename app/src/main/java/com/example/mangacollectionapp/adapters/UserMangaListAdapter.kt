package com.example.mangacollectionapp.adapters

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mangacollectionapp.R
import com.example.mangacollectionapp.models.Manga

class UserMangaListAdapter(private val mangaList : List<Manga>, private val onItemClickListener : OnUserMangaListItemClickListener) : RecyclerView.Adapter<UserMangaListAdapter.UserMangaUserListViewHolder>() {

    inner class UserMangaUserListViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        fun bindData(manga: Manga) {
            val title = itemView.findViewById<TextView>(R.id.manga_search_item_title_text_view)
            val author = itemView.findViewById<TextView>(R.id.manga_search_item_author_text_view)
            val status = itemView.findViewById<TextView>(R.id.manga_search_item_status_text_view)
            val image = itemView.findViewById<ImageView>(R.id.manga_search_item_image_view)


            title.text = manga.title
            author.text = manga.authors
            status.text = manga.userStatus

            //TODO internet connection check
            image.load(manga.image)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMangaListAdapter.UserMangaUserListViewHolder {
        return UserMangaUserListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_manga_item, parent,  false))
    }

    override fun getItemCount(): Int {
        return mangaList.size
    }

    private var _selectedItem : Int? = null
    val selectedItem : MutableLiveData<Int?> = MutableLiveData()
    var isSelected = false;

    override fun onBindViewHolder(holder: UserMangaListAdapter.UserMangaUserListViewHolder, position: Int) {
        holder.bindData(mangaList[position])
        holder.itemView.setOnClickListener {
            if (!isSelected){
                onItemClickListener.onItemClick(mangaList[position])
            }
            else {
                isSelected = false
                _selectedItem = null
                selectedItem.value = null
            }
        }

        selectedItem.observeForever {
            if(_selectedItem != null && _selectedItem == position) {
                holder.itemView.setBackgroundColor(Color.GRAY)
            }
            else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        }

        holder.itemView.setOnLongClickListener {
            isSelected = true

            _selectedItem = if (_selectedItem == position) {
                null
            } else {
                position
            }

            selectedItem.postValue(_selectedItem)

            notifyItemChanged(position)

            true
        }

    }

    fun clearSelectedItems() {
        _selectedItem = null
        selectedItem.value = null
    }

}