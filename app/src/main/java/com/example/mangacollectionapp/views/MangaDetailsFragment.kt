package com.example.mangacollectionapp.views

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.mangacollectionapp.databinding.FragmentMangaDetailsBinding
import com.example.mangacollectionapp.utils.setResizableText
import coil.load
import coil.size.Scale
import com.example.mangacollectionapp.R
import com.example.mangacollectionapp.database.MangaDatabase
import com.example.mangacollectionapp.viewmodelfactory.MangaDetailsViewModelFactory
import com.example.mangacollectionapp.models.MangaVolume
import com.example.mangacollectionapp.viewmodels.MangaDetailsViewModel


class MangaDetailsFragment : Fragment() {

    private val viewModel : MangaDetailsViewModel by lazy {
        ViewModelProvider(
            this,
            MangaDetailsViewModelFactory(MangaDatabase.getDatabase(requireContext()).mangaDao)
        ).get(MangaDetailsViewModel::class.java)
    }

    private lateinit var binding: FragmentMangaDetailsBinding
    private val args : MangaDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMangaDetailsBinding.inflate(layoutInflater)

        binding.mangaDetailsTitleTextView.text = args.mangaObject?.title
        binding.mangaDetailsAuthorTextView.text = args.mangaObject?.authors
        if(!args.mangaObject?.synopsis.isNullOrEmpty()){
            binding.mangaDetailsSynopsisTextView.setResizableText(args.mangaObject?.synopsis!!, 6, true)
        }
        binding.mangaDetailsCoverImageView.load(args.mangaObject?.image){
            scale(Scale.FIT)
        }

        binding.mangaDetailsToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.add_manga_to_list -> {
                    insertMangaToList()
                }
            }
            true
        }

        return binding.root
    }

    private fun insertMangaToList() {
        val dialog = Dialog(this.requireContext())
        dialog.setContentView(R.layout.add_manga_dialog)

        var status = "Wanted"

        val confirmationButton = dialog.findViewById<Button>(R.id.add_manga_dialog_confirmation_button)
        val cancelButton = dialog.findViewById<Button>(R.id.add_manga_dialog_cancel_button)
        val statusSpinner = dialog.findViewById<Spinner>(R.id.add_manga_dialog_status_spinner)

        statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                status = statusSpinner.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        confirmationButton.setOnClickListener {
            val volumes = mutableListOf<MangaVolume>()

            val manga = args.mangaObject!!
            manga.userStatus = status

            if(manga.volumes != null){
                if(status == "Completed"){
                    for ( iterator in 1..manga.volumes) {
                        volumes.add(MangaVolume(numberOfTome = context?.getString(R.string.volume_title) + " " + iterator.toString(), mangaId = args.mangaObject!!.id, status = context?.getString(R.string.owned_status)!!))
                    }
                }
                else {
                    for ( iterator in 1..manga.volumes) {
                        volumes.add(MangaVolume(numberOfTome = context?.getString(R.string.volume_title) + " " + iterator.toString(), mangaId = args.mangaObject!!.id, status = context?.getString(R.string.unowned_status)!!))
                    }
                }

            }

            viewModel.insertMangaWithVolumes(manga, volumes)

            dialog.cancel()
        }

        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }
}