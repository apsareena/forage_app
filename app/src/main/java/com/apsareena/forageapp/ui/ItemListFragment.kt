package com.apsareena.forageapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apsareena.forageapp.R
import com.apsareena.forageapp.adapter.ItemListAdapter
import com.apsareena.forageapp.application.ForageApplication
import com.apsareena.forageapp.databinding.FragmentItemListBinding
import com.apsareena.forageapp.model.ForageViewModel
import com.apsareena.forageapp.model.ForageViewModelFactory


class ItemListFragment : Fragment() {

    private val viewModel: ForageViewModel by activityViewModels {
        ForageViewModelFactory(
            (activity?.application as ForageApplication).database.itemDao()
        )
    }

    private var _binding : FragmentItemListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ItemListAdapter{
            val action =
                ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.forageId)
            this.findNavController().navigate(action)
        }
        // attach an observer on the allItems list to update the UI automatically
        // when the data changes
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        binding.floatingActionButton.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
    }


}