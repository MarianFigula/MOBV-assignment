package eu.mcomputing.mobv.mobvzadanie.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotation
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import eu.mcomputing.mobv.mobvzadanie.data.DataRepository
import eu.mcomputing.mobv.mobvzadanie.data.PreferenceData
import eu.mcomputing.mobv.mobvzadanie.bottomNavLayout.CustomBottomNavLayout
import eu.mcomputing.mobv.mobvzadanie.R
import eu.mcomputing.mobv.mobvzadanie.databinding.FragmentMapBinding
import eu.mcomputing.mobv.mobvzadanie.databinding.FragmentProfileBinding
import eu.mcomputing.mobv.mobvzadanie.viewmodels.ProfileViewModel

class ProfileFragment: Fragment(R.layout.fragment_profile),
    CustomBottomNavLayout.OnNavIconClickListener
{
    private lateinit var viewModel: ProfileViewModel
    private var binding: FragmentProfileBinding? = null

    private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                viewModel.sharingLocation.postValue(false)
            }
        }

    fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[ProfileViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bnd ->
            val bottomNavLayout = bnd.profileFragment
            bottomNavLayout.setOnNavIconClickListener(this)

            bnd.loadProfileBtn.setOnClickListener {
                val user = PreferenceData.getInstance().getUser(requireContext())
                user?.let {
                    viewModel.loadUser(it.id)
                }
            }

            bnd.signOutButton.setOnClickListener {
                PreferenceData.getInstance().clearData(requireContext())
                it.findNavController().navigate(R.id.action_profileFragment_to_introductionFragment)
            }

            viewModel.profileResult.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    Snackbar.make(
                        bnd.loadProfileBtn,
                        it,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            viewModel.sharingLocation.postValue(
                PreferenceData.getInstance().getSharing(requireContext())
            )

            viewModel.sharingLocation.observe(viewLifecycleOwner) {
                it?.let {
                    if (it) {
                        if (!hasPermissions(requireContext())) {
                            viewModel.sharingLocation.postValue(false)
                            requestPermissionLauncher.launch(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        } else {
                            PreferenceData.getInstance().putSharing(requireContext(), true)
                        }
                    } else {
                        PreferenceData.getInstance().putSharing(requireContext(), false)
                    }
                }
            }


        }
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