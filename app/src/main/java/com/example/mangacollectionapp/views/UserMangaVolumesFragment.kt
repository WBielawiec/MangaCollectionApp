package com.example.mangacollectionapp.views

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mangacollectionapp.R
import com.example.mangacollectionapp.adapters.OnMangaVolumeItemClickListener
import com.example.mangacollectionapp.adapters.UserMangaVolumesAdapter
import com.example.mangacollectionapp.database.MangaDatabase
import com.example.mangacollectionapp.databinding.FragmentUserMangaVolumesBinding
import com.example.mangacollectionapp.models.MangaVolume
import com.example.mangacollectionapp.viewmodelfactory.UserMangaVolumesViewModelFactory
import com.example.mangacollectionapp.viewmodels.UserMangaVolumesViewModel
import kotlinx.coroutines.launch

class UserMangaVolumesFragment : Fragment(), OnMangaVolumeItemClickListener {

    private lateinit var binding : FragmentUserMangaVolumesBinding
    private val args : MangaDetailsFragmentArgs by navArgs()
    private lateinit var adapter : UserMangaVolumesAdapter

    private val viewModel : UserMangaVolumesViewModel by lazy {
        ViewModelProvider(
            this,
            UserMangaVolumesViewModelFactory(MangaDatabase.getDatabase(requireContext()).mangaDao, args.mangaObject?.id!!)
        ).get(UserMangaVolumesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserMangaVolumesBinding.inflate(layoutInflater)

        val recyclerView = binding.userMangaVolumesRecyclerView

        viewModel.volumesListLiveData.observe(viewLifecycleOwner) {
            adapter = UserMangaVolumesAdapter(it, this)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter

            adapter.selectedItems.observeForever { selectedItems ->
                if (selectedItems.isNotEmpty()) {
                    binding.userMangaVolumesToolbar.visibility = View.GONE
                    binding.userMangaVolumesChangeToolbar.visibility = View.VISIBLE
                }
                else {
                    binding.userMangaVolumesChangeToolbar.visibility = View.GONE
                    binding.userMangaVolumesToolbar.visibility = View.VISIBLE
                }
            }
        }

        inflateMainToolbarMenu()
        inflateChangeVolumesToolbarMenu()

        binding.userMangaVolumesAdapterLayout.setOnClickListener {
            adapter.clearSelectedItems()
            viewModel.getAllMangaVolumes(args.mangaObject!!.id)
        }

        return binding.root
    }

    private fun inflateMainToolbarMenu() {
        binding.userMangaVolumesToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.add_volume_to_list -> {
                    showInsertToListDialog()
                }
                R.id.remove_volume -> {
                    showRemoveVolumesDialog()
                }
            }
            true
        }
    }

    private fun inflateChangeVolumesToolbarMenu() {
        binding.userMangaVolumesChangeToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.change_volume_status -> {
                    showUpdateVolumesDialog()
                }
            }
            true
        }

        binding.userMangaVolumesChangeSelectionImageView.setOnClickListener {
            adapter.changeAllItemSelection()
            //viewModel.getAllMangaVolumes(args.mangaObject!!.id)
        }
    }

    private fun showRemoveVolumesDialog() {
        val dialog = Dialog(this.requireContext())
        dialog.setContentView(R.layout.remove_volume_dialog)

        var counter = 0

        val confirmationButton = dialog.findViewById<Button>(R.id.remove_volume_dialog_confirmation_button)
        val cancelButton = dialog.findViewById<Button>(R.id.remove_volume_dialog_cancel_button)
        val decrementButton = dialog.findViewById<ImageView>(R.id.remove_volume_dialog_decrement_button)
        val incrementButton = dialog.findViewById<ImageView>(R.id.remove_volume_dialog_increment_button)
        val allButton = dialog.findViewById<ImageView>(R.id.remove_volume_dialog_all_button)
        val numberOfVolumesTextView  = dialog.findViewById<TextView>(R.id.remove_volume_dialog_counter_textview)

        decrementButton.setOnClickListener {
            if(counter > 0 ){
                counter--
                numberOfVolumesTextView.text = counter.toString()
            }
        }


        incrementButton.setOnClickListener {
            if(counter <= adapter.getVolumesList().size){
                counter++
                numberOfVolumesTextView.text = counter.toString()
            }
        }

        allButton.setOnClickListener {
            counter = adapter.getVolumesList().size
            numberOfVolumesTextView.text = counter.toString()
        }

        confirmationButton.setOnClickListener {
            removeMangaVolumes(counter)
            dialog.cancel()
        }

        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()

    }

    private fun removeMangaVolumes(quantity : Int) {

        val volumesOnList = viewModel.volumesListLiveData.value?.size
        val startPosition = volumesOnList?.minus(quantity)

        val listToRemove = viewModel.volumesListLiveData.value!!.subList(startPosition!!, volumesOnList)

        lifecycleScope.launch {
            viewModel.deleteVolumes(listToRemove, args.mangaObject!!.id)
        }

    }

    private fun showUpdateVolumesDialog() {
        val dialog = Dialog(this.requireContext())
        dialog.setContentView(R.layout.change_volume_dialog)

        val confirmationButton = dialog.findViewById<Button>(R.id.change_volume_dialog_confirmation_button)
        val cancelButton = dialog.findViewById<Button>(R.id.change_volume_dialog_cancel_button)
        val statusSpinner = dialog.findViewById<Spinner>(R.id.change_volume_dialog_status_spinner)

        var status = ""

        statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                status = statusSpinner.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        confirmationButton.setOnClickListener {
            updateVolumes(status)
            dialog.cancel()
        }

        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    private fun updateVolumes(status: String) {
        val volumeList = adapter.getVolumesList()

        adapter.selectedItems.value?.forEach { index ->
            volumeList[index].status = status
        }

        lifecycleScope.launch {
            viewModel.updateVolumes(volumeList, args.mangaObject!!.id)
        }

        adapter.clearSelectedItems()
    }

    override fun onItemClick(item: MangaVolume) {

    }

    override fun onItemLongClick(item: MangaVolume, position : Int) {

    }

    private fun showInsertToListDialog(){
        val dialog = Dialog(this.requireContext())
        dialog.setContentView(R.layout.add_volume_dialog)

        var counter = 0
        var status = "unowned"

        val confirmationButton = dialog.findViewById<Button>(R.id.add_volume_dialog_confirmation_button)
        val cancelButton = dialog.findViewById<Button>(R.id.add_volume_dialog_cancel_button)
        val numberOfVolumesTextView  = dialog.findViewById<TextView>(R.id.add_volume_dialog_counter_textview)
        val decrementButton = dialog.findViewById<ImageView>(R.id.add_volume_dialog_decrement_button)
        val incrementButton = dialog.findViewById<ImageView>(R.id.add_volume_dialog_increment_button)
        val statusSpinner = dialog.findViewById<Spinner>(R.id.add_volume_dialog_status_spinner)

        decrementButton.setOnClickListener {
            if(counter > 0 ){
                counter--
                numberOfVolumesTextView.text = counter.toString()
            }
        }

        incrementButton.setOnClickListener {
            counter++
            numberOfVolumesTextView.text = counter.toString()
        }

        statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                status = statusSpinner.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        confirmationButton.setOnClickListener {
            lifecycleScope.launch {
                insertVolumesToList(counter, status)
            }
            viewModel.volumeCount.removeObservers(viewLifecycleOwner)
            dialog.cancel()
        }

        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    private suspend fun insertVolumesToList(numberOfVolumesToAdd : Int, status : String) {

        val mangaVolumes = adapter.getVolumesList().size
        val volumesToAdd = mutableListOf<MangaVolume>()
        val firstTomeToAdd = mangaVolumes + 1

        for(volumeNumber in firstTomeToAdd until numberOfVolumesToAdd + firstTomeToAdd) {
            volumesToAdd.add(MangaVolume(numberOfTome = context?.getString(R.string.volume_title) + " " + volumeNumber.toString(), mangaId = args.mangaObject!!.id, status = status))
        }
        lifecycleScope.launch {
            viewModel.insertVolumesWithDataRefresh(volumesToAdd, args.mangaObject!!.id)
        }


    }
}