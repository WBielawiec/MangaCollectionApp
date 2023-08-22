package com.example.mangacollectionapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mangacollectionapp.api.MangaAPIObject

import com.example.mangacollectionapp.R

class MangaSearchAdapter(private val mangaList : List<MangaAPIObject>, private val onItemClickListener : OnSearchItemClickListener) : RecyclerView.Adapter<MangaSearchAdapter.MangaSearchViewHolder>() {

    inner class MangaSearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindData(manga: MangaAPIObject) {
            val title = itemView.findViewById<TextView>(R.id.manga_search_item_title_text_view)
            val author = itemView.findViewById<TextView>(R.id.manga_search_item_author_text_view)
            val status = itemView.findViewById<TextView>(R.id.manga_search_item_status_text_view)
            val image = itemView.findViewById<ImageView>(R.id.manga_search_item_image_view)

            title.text = manga.title
            var authors = ""
            manga.authors.forEachIndexed { index, author ->
                authors = "$authors${author.name.replace(",", "")}"

                if(index < manga.authors.size - 1){
                    authors = "$authors, "
                }
            }

            author.text = authors
            status.text = manga.status
            image.load(manga.image.jpg.image_url)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaSearchViewHolder {
        return MangaSearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_manga_item, parent,  false))
    }

    override fun onBindViewHolder(holder: MangaSearchViewHolder, position: Int) {
        holder.bindData(mangaList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(mangaList[position])
        }
    }

    override fun getItemCount(): Int {
        return mangaList.size
    }

}