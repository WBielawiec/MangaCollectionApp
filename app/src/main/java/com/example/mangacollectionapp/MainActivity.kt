package com.example.mangacollectionapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.room.Room
import com.example.mangacollectionapp.database.MangaDatabase
import com.example.mangacollectionapp.databinding.ActivityMainBinding
import com.example.mangacollectionapp.views.MangaSearchFragment
import com.example.mangacollectionapp.views.UserMangaListFragment
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //this.deleteDatabase("manga-database")

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.collection_view -> {
                    binding.navigationHostFragmentContainer.findNavController().navigate(R.id.userMangaList)
                }
                R.id.search_view -> {
                    binding.navigationHostFragmentContainer.findNavController().navigate(R.id.mangaSearchFragment)
                }
            }
            true
        }

        setContentView(binding.root)

    }
}