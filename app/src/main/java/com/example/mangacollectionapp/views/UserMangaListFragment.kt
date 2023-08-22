package com.example.mangacollectionapp.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.opengl.Visibility
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mangacollectionapp.R
import com.example.mangacollectionapp.adapters.OnUserMangaListItemClickListener
import com.example.mangacollectionapp.adapters.UserMangaListAdapter
import com.example.mangacollectionapp.database.MangaDatabase
import com.example.mangacollectionapp.databinding.FragmentUserMangaListBinding
import com.example.mangacollectionapp.viewmodelfactory.UserMangaListViewModelFactory
import com.example.mangacollectionapp.models.Manga
import com.example.mangacollectionapp.viewmodels.UserMangaListViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.io.File

class UserMangaListFragment : Fragment(), OnUserMangaListItemClickListener {

    private lateinit var binding : FragmentUserMangaListBinding
    private lateinit var adapter : UserMangaListAdapter

    private val viewModel : UserMangaListViewModel by lazy {
        ViewModelProvider(
            this,
            UserMangaListViewModelFactory(MangaDatabase.getDatabase(requireContext()).mangaDao)
        ).get(UserMangaListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserMangaListBinding.inflate(layoutInflater)

        viewModel.mangaList.observe(viewLifecycleOwner) {
            adapter = UserMangaListAdapter(it, this)
            val recyclerView = binding.userMangaListRecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter

            adapter.selectedItem.observeForever {selectedItem ->
                if (selectedItem != null) {
                    binding.userMangaListToolbar.visibility = View.GONE
                    binding.userMangaListChangeToolbar.visibility = View.VISIBLE

                } else {
                    binding.userMangaListToolbar.visibility = View.VISIBLE
                    binding.userMangaListChangeToolbar.visibility = View.GONE
                }
            }

/*            binding.userMangaListToolbar.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.import_database -> {
                        importDatabaseFile()
                    }
                    R.id.export_database -> {
                        val file = Uri.fromFile(activity?.getDatabasePath("manga-database.db"))
                        val ref = FirebaseStorage.getInstance().getReference("database.db")
                        ref.putFile(file).addOnSuccessListener {
                            Toast.makeText(context, context?.getString(R.string.export_database_success_communicat), Toast.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, context?.getString(R.string.export_database_failure_communicat), Toast.LENGTH_LONG).show()
                        }
                    }
                }
                true
            }*/

            binding.userMangaListChangeToolbar.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.change_manga_status -> {
                        changeMangaStatus()
                    }
                    R.id.delete_manga -> {
                        deleteManga()
                    }
                }
                true
            }
        }

        binding.userMangaListGroupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2) {
                    0 -> {
                        viewModel.getAllMangas()
                    }
                    1 -> {
                        viewModel.getAllCompletedMangas()
                    }
                    2 -> {
                        viewModel.getAllInProgressMangas()
                    }
                    3 -> {
                        viewModel.getAllWantedMangas()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.userMangaListAdapterLayout.setOnClickListener {
            adapter.clearSelectedItems()
        }

        return binding.root
    }

/*    private fun importDatabaseFile() {
        val alertDialog : AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.import_database,
                    DialogInterface.OnClickListener { dialog, id ->

                        val destination = File(context.applicationContext.filesDir, "databases")
                        val ref = FirebaseStorage.getInstance().getReference("database.db")
                        ref.getFile(destination).addOnSuccessListener {
                            Toast.makeText(context, context?.getString(R.string.import_database_success_communicat), Toast.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, context?.getString(R.string.import_database_failure_communicat), Toast.LENGTH_LONG).show()
                        }

                        dialog.cancel()
                    })
                setNegativeButton(R.string.add_volume_dialog_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

            }
            builder.setTitle(R.string.import_database_dialog_title)
            builder.setMessage(R.string.import_database_dialog_body)
            builder.create()

        }
        alertDialog?.show()
    }*/

    private fun changeMangaStatus() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.add_manga_dialog)

        var status = ""

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
            val manga = viewModel.getSingleMangaFromList(adapter.selectedItem.value!!)
            manga.userStatus = status


            lifecycleScope.launch {
                viewModel.updateManga(manga)
            }

            dialog.cancel()
        }

        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }


    private fun deleteManga() {
        val alertDialog : AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.delete_manga,
                    DialogInterface.OnClickListener { dialog, id ->
                        val manga = viewModel.getSingleMangaFromList(adapter.selectedItem.value!!)

                        lifecycleScope.launch {
                            viewModel.deleteManga(manga)
                        }

                        dialog.cancel()
                    })
                setNegativeButton(R.string.add_volume_dialog_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

            }
            builder.setTitle(R.string.delete_manga_confirm_dialog_title)
            builder.setMessage(R.string.delete_manga_confirm_dialog_information)
            builder.create()

        }
        alertDialog?.show()
    }

    override fun onItemClick(item: Manga) {
        val action = UserMangaListFragmentDirections.actionUserMangaListToUserMangaVolumesFragment(item)
        this.findNavController().navigate(action)
    }

    override fun onItemLongClick(item: Manga, position: Int) {

    }
}