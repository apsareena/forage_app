package com.apsareena.forageapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apsareena.forageapp.R
import com.apsareena.forageapp.application.ForageApplication
import com.apsareena.forageapp.data.Item
import com.apsareena.forageapp.databinding.FragmentItemDetailBinding
import com.apsareena.forageapp.model.ForageViewModel
import com.apsareena.forageapp.model.ForageViewModelFactory


class ItemDetailFragment : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()
    private lateinit var item: Item

    private val viewModel: ForageViewModel by activityViewModels {
        ForageViewModelFactory(
            (activity?.application as ForageApplication).database.itemDao()
        )
    }

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    // Binds views with the passed data in item data.
    private fun bind(item: Item){
        binding.apply {
            forageName.text = item.itemName
            forageLocation.text = item.itemLocation
            forageNotes.text = item.itemNotes
            forageSeason.text = if (item.itemSeason){
                "Currently in season"
            } else{
                "Currently not in season"
            }
            editActionButton.setOnClickListener {
                editItem()
            }
        }

    }

    // navigate to the edit item screen

    private fun editItem(){
        val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddItemFragment(
            getString(R.string.edit_fragment_title),
            item.forageId
        )
        this.findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        // retrieve the item details using the item id
        // attach an observer on the data (instead of poling
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            item = selectedItem
            bind(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}