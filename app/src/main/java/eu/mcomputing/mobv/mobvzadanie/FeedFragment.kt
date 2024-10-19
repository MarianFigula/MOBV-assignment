package eu.mcomputing.mobv.mobvzadanie

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FeedFragment : Fragment(R.layout.fragment_feed), CustomBottomNavLayout.OnNavIconClickListener {

    private lateinit var viewModel: FeedViewModel


    val TAG = "FeedFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = view.findViewById<CustomBottomNavLayout>(R.id.feedFragment)
        bottomNav.setOnNavIconClickListener(this)  // Set the listener

        viewModel = ViewModelProvider(this)[FeedViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val feedAdapter = FeedAdapter()
        recyclerView.adapter = feedAdapter

        val email = arguments?.getString("email")

        viewModel.feedItems.observe(viewLifecycleOwner) { items ->
            feedAdapter.updateItems(items)
        }

        viewModel.updateItems(
            listOf(
                MyItem(1, R.drawable.ic_person, "Prvy"),
                MyItem(2, R.drawable.ic_person, "Druhy"),
                MyItem(3, R.drawable.ic_person, "Treti"),
                MyItem(4, R.drawable.ic_person, "Stvrty"),
                MyItem(5, R.drawable.ic_person, "Piaty"),
                MyItem(6, R.drawable.ic_person, "Siesty"),
                MyItem(7, R.drawable.ic_person, "Siedmy"),
                MyItem(8, R.drawable.ic_person, "Osmy"),
                MyItem(9, R.drawable.ic_person, "Deviaty"),
                MyItem(10, R.drawable.ic_person, "Desiaty"),
                MyItem(11, R.drawable.ic_person, "Jedenasty")
            )
        )

    }

    override fun onMapClick() {
        findNavController().navigate(R.id.mapFragment)
    }

    override fun onListClick() {
        Log.d(TAG, "onListClick")

    }

    override fun onProfileClick() {
        findNavController().navigate(R.id.profileFragment)
    }
}