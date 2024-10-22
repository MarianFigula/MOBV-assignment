package eu.mcomputing.mobv.mobvzadanie

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

class ProfileFragment: Fragment(R.layout.fragment_profile),
    CustomBottomNavLayout.OnNavIconClickListener
{
    val TAG = "Profile"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = view.findViewById<CustomBottomNavLayout>(R.id.profileFragment)
        bottomNav.setOnNavIconClickListener(this)  // Set the listener

        Log.d(TAG, "som profile")

    }

    override fun onMapClick() {
        findNavController().navigate(R.id.mapFragment)
    }

    override fun onListClick() {
        findNavController().navigate(R.id.feedFragment)
    }

    override fun onProfileClick() {

    }
}