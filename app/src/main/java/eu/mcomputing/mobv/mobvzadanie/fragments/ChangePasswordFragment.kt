package eu.mcomputing.mobv.mobvzadanie.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import eu.mcomputing.mobv.mobvzadanie.viewmodels.AuthViewModel
import eu.mcomputing.mobv.mobvzadanie.data.DataRepository
import eu.mcomputing.mobv.mobvzadanie.R

class ChangePasswordFragment: Fragment(R.layout.fragment_forgot_password_reset) {

    private lateinit var viewModel: AuthViewModel
    val TAG = "ChangePasswordFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val changePasswordButton: Button = view.findViewById(R.id.changePasswordButton)

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[AuthViewModel::class.java]

        viewModel.changePasswordResult.observe(viewLifecycleOwner){
            if (it.second != null) {
                Snackbar.make(
                    changePasswordButton,
                    it.first,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        changePasswordButton.setOnClickListener {
            val oldPassword = view.findViewById<TextInputEditText>(R.id.changePasswordEditOldPassword).text.toString()
            val newPassword = view.findViewById<TextInputEditText>(R.id.changePasswordEditNewPassword).text.toString()

            // Assuming you store the user's token somewhere after login
            val authToken = "your_auth_token_here"
            viewModel.changePassword(authToken, oldPassword, newPassword)
        }

    }
}