package eu.mcomputing.mobv.mobvzadanie.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.mcomputing.mobv.mobvzadanie.DataRepository
import eu.mcomputing.mobv.mobvzadanie.bottomNavLayout.CustomBottomNavLayout
import eu.mcomputing.mobv.mobvzadanie.viewmodels.FeedViewModel
import eu.mcomputing.mobv.mobvzadanie.R
import eu.mcomputing.mobv.mobvzadanie.adapters.FeedAdapter
import eu.mcomputing.mobv.mobvzadanie.adapters.MyItem
import eu.mcomputing.mobv.mobvzadanie.databinding.FragmentFeedBinding

class FeedFragment : Fragment(R.layout.fragment_feed),
    CustomBottomNavLayout.OnNavIconClickListener {

    private var binding: FragmentFeedBinding? = null
    private lateinit var viewModel: FeedViewModel

    val TAG = "FeedFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FeedViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[FeedViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFeedBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bnd->
//            bnd.btnGenerate.setOnClickListener {
//                viewModel.updateItems()
//            }
            val bottomNavLayout = bnd.root.findViewById<CustomBottomNavLayout>(R.id.customBottomNavLayout)
            bottomNavLayout.setOnNavIconClickListener(this)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
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