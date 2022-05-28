package com.apsareena.forageapp.model

import androidx.lifecycle.*
import com.apsareena.forageapp.data.Item
import com.apsareena.forageapp.data.ItemDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ForageViewModel(private val itemDao: ItemDao) : ViewModel() {

    // cache all the items from the database using LiveData
    val allItems: LiveData<List<Item>> = itemDao.getAllItems().asLiveData()

    fun retrieveItem(forageId: Int): LiveData<Item>{
        return itemDao.getItem(forageId).asLiveData()
    }

    fun addNewItem(itemName: String, itemLocation: String, itemSeason: Boolean, itemNotes: String){
        val newItem = getNewItemEntry(itemName, itemLocation, itemSeason, itemNotes)
        insertItem(newItem)
    }

    private fun insertItem(newItem: Item) {
        viewModelScope.launch {
            itemDao.insert(newItem)
        }
    }

    private fun getNewItemEntry(itemName: String, itemLocation: String, itemSeason: Boolean, itemNotes: String): Item {
        return Item(
            itemName = itemName,
            itemLocation = itemLocation,
            itemSeason = itemSeason,
            itemNotes = itemNotes
        )
    }

    fun deleteItem(item: Item) {
         viewModelScope.launch {
             itemDao.delete(item)
         }
    }

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemLocation: String,
        itemSeason: Boolean,
        itemNotes: String
    ){
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemLocation, itemSeason, itemNotes)
        updateItem(updatedItem)
    }

    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemLocation: String,
        itemSeason: Boolean,
        itemNotes: String)
    : Item {
        return Item(
            forageId = itemId,
            itemName = itemName,
            itemLocation = itemLocation,
            itemSeason = itemSeason,
            itemNotes = itemNotes
        )
    }

    private fun updateItem(item: Item){
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    fun isEntryValid(itemName: String, itemLocation: String, itemNotes: String):
            Boolean {
        if (itemName.isBlank() || itemLocation.isBlank() || itemNotes.isBlank()){
            return false
        }
        return true
    }
}

class ForageViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForageViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}