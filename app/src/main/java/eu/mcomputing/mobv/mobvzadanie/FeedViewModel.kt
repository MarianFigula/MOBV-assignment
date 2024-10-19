package eu.mcomputing.mobv.mobvzadanie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedViewModel : ViewModel() {
    private val _feedItems = MutableLiveData<List<MyItem>>()
    val feedItems : LiveData<List<MyItem>> get() = _feedItems


    fun updateItems(items: List<MyItem>) {
        _feedItems.value = items
    }

}