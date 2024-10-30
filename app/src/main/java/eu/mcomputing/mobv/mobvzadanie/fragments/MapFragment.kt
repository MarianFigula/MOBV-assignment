package eu.mcomputing.mobv.mobvzadanie.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import eu.mcomputing.mobv.mobvzadanie.bottomNavLayout.CustomBottomNavLayout
import eu.mcomputing.mobv.mobvzadanie.R

class MapFragment: Fragment(R.layout.fragment_map),
    CustomBottomNavLayout.OnNavIconClickListener
{
    private var mapView: MapView? = null

    val TAG = "MapFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "tu")

        val bottomNav = view.findViewById<CustomBottomNavLayout>(R.id.mapFragment)
        bottomNav.setOnNavIconClickListener(this)  // Set the listener

        val mapView: MapView? = view.findViewById(R.id.mapView)
        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)
    }

    override fun onMapClick() {

    }

    override fun onListClick() {
        findNavController().navigate(R.id.feedFragment)
    }

    override fun onProfileClick() {
        findNavController().navigate(R.id.profileFragment)
    }
}