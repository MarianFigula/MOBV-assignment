package eu.mcomputing.mobv.mobvzadanie.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import eu.mcomputing.mobv.mobvzadanie.viewmodels.AuthViewModel
import eu.mcomputing.mobv.mobvzadanie.data.DataRepository
import eu.mcomputing.mobv.mobvzadanie.R
import eu.mcomputing.mobv.mobvzadanie.databinding.FragmentPasswordChangeBinding

class ChangePasswordFragment: Fragment(R.layout.fragment_password_change) {

    private lateinit var viewModel: AuthViewModel
    private var binding: FragmentPasswordChangeBinding? = null

    val TAG = "ChangePasswordFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPasswordChangeBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bnd ->
            viewModel.changePasswordResult.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    Snackbar.make(
                        bnd.changePasswordButton,
                        it,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}