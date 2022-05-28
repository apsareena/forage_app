package com.apsareena.forageapp.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apsareena.forageapp.R
import com.apsareena.forageapp.application.ForageApplication
import com.apsareena.forageapp.data.Item
import com.apsareena.forageapp.databinding.FragmentAddItemBinding
import com.apsareena.forageapp.model.ForageViewModel
import com.apsareena.forageapp.model.ForageViewModelFactory


class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForageViewModel by activityViewModels {
        ForageViewModelFactory(
            (activity?.application as ForageApplication).database
                .itemDao()
        )
    }

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    private lateinit var item: Item

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item)
            }
        } else {
            binding.saveButton.setOnClickListener {
                addNewItem()
                this.findNavController().navigate(R.id.action_addItemFragment_to_itemListFragment)
            }
        }

    }

    private fun addNewItem() {
        if (isEntryValid()){
            viewModel.addNewItem(
                binding.forageNameEditText.text.toString(),
                binding.forageLocationEditText.text.toString(),
                binding.seasonCheckBox.isChecked,
                binding.notesInputEditText.text.toString()
            )
        }
    }

    private fun updateItem() {
        if (isEntryValid()){
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.forageNameEditText.text.toString(),
                this.binding.forageLocationEditText.text.toString(),
                this.binding.seasonCheckBox.isChecked,
                this.binding.notesInputEditText.text.toString()
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.forageNameEditText.text.toString(),
            binding.forageLocationEditText.text.toString(),
            binding.notesInputEditText.text.toString()
        )
    }

    private fun bind(item: Item){
        binding.apply {
            forageNameEditText.setText(item.itemName, TextView.BufferType.SPANNABLE)
            forageLocationEditText.setText(item.itemLocation, TextView.BufferType.SPANNABLE)
            seasonCheckBox.isChecked = item.itemSeason
            notesInputEditText.setText(item.itemNotes, TextView.BufferType.SPANNABLE)
            saveButton.setOnClickListener {
                updateItem()
                this@AddItemFragment.findNavController().navigate(R.id.action_addItemFragment_to_itemListFragment)
            }
            deleteButton.setOnClickListener {
                deleteItem()
            }
        }
    }

    private fun deleteItem() {
        viewModel.deleteItem(item)
        this.findNavController().navigate(R.id.action_addItemFragment_to_itemListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // hide keyboard
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }


}