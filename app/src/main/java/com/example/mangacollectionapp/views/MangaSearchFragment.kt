package com.example.mangacollectionapp.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mangacollectionapp.adapters.MangaSearchAdapter
import com.example.mangacollectionapp.adapters.OnSearchItemClickListener
import com.example.mangacollectionapp.api.MangaAPIObject
import com.example.mangacollectionapp.api.ApiResult
import com.example.mangacollectionapp.databinding.FragmentMangaSearchBinding
import com.example.mangacollectionapp.models.Manga
import com.example.mangacollectionapp.viewmodels.MangaSearchViewModel

class MangaSearchFragment : Fragment(), OnSearchItemClickListener {

    private lateinit var binding: FragmentMangaSearchBinding

    private val viewModel: MangaSearchViewModel by lazy{
        ViewModelProvider(this).get(MangaSearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMangaSearchBinding.inflate(layoutInflater)

        binding.mangaSearchSearchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
            viewModel.changeSearchText(text!!)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        viewModel.mangasLiveData.observe(viewLifecycleOwner) { state ->
            processMangaResponse(state)
        }


        return binding.root
    }

    private fun processMangaResponse(state: ApiResult<List<MangaAPIObject>?>){
        when(state){
            is ApiResult.Loading -> {
                Log.d("Loading data", state.data.toString())
            }
            is ApiResult.Success -> {
                //pb.visibility = View.GONE
                if(state.data != null) {
                    Log.d("Fetched data", state.data.toString())
                    val adapter = MangaSearchAdapter(state.data, this)
                    val recyclerView = binding.mangaSearchRecyclerView
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = adapter

                }
            }
            is ApiResult.Error -> {

            }
        }
    }

    override fun onItemClick(item: MangaAPIObject) {
        var authors =""

        item.authors.forEachIndexed { index, author ->
            authors = "$authors${author.name.replace(",", "")}"
            if(index < item.authors.size - 1){
                authors = "$authors, "
            }
        }
        var genres =""
        item.genres.forEachIndexed { index, genre ->
            if(index < item.genres.size - 1){
                genres = "$genres${genre.name}, "
            } else {
                genres += genre.name
            }
        }
        val mangaObject = Manga(
            item.title,
            item.chapters,
            item.volumes,
            item.status,
            item.published.prop.from.day.toString() + "." + item.published.prop.from.month.toString() + "." + item.published.prop.from.year.toString(),
            item.published.prop.to.day.toString() + "." + item.published.prop.to.month.toString() + "." + item.published.prop.to.year.toString(),
            item.synopsis,
            authors,
            genres,
            item.image.jpg.image_url,
            id = item.mal_id
        )

            val action = MangaSearchFragmentDirections.actionMangaSearchFragmentToMangaDetailsFragment(mangaObject)
            this.findNavController().navigate(action)
    }
}