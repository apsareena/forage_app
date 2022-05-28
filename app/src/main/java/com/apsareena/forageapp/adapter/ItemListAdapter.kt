package com.apsareena.forageapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apsareena.forageapp.data.Item
import com.apsareena.forageapp.databinding.ItemListItemBinding


class ItemListAdapter(private val onItemClicked: (Item)->Unit) :
    ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback){

        class ItemViewHolder(private var binding: ItemListItemBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(item: Item) {
                    binding.forageName.text = item.itemName
                    binding.forageLocation.text = item.itemLocation
                }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemListItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            )
        ))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.forageId == newItem.forageId
            }
        }
    }
}