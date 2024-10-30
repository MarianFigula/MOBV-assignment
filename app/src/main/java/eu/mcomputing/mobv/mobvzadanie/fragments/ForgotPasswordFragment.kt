package eu.mcomputing.mobv.mobvzadanie.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import eu.mcomputing.mobv.mobvzadanie.viewmodels.AuthViewModel
import eu.mcomputing.mobv.mobvzadanie.data.DataRepository
import eu.mcomputing.mobv.mobvzadanie.R

class ForgotPasswordFragment() : Fragment(R.layout.fragment_forgot_password) {
    val TAG = "ForgotPasswordFragment"
    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sendEmailCodeButton: Button = view.findViewById(R.id.sendEmailCodeButton)

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[AuthViewModel::class.java]

        viewModel.resetPasswordResult.observe(viewLifecycleOwner){
            if (it.first.contains("success", ignoreCase = true)){
                requireView().findNavController().navigate(R.id.loginFragment)
            }else{
                Snackbar.make(
                    sendEmailCodeButton,
                    it.first,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        sendEmailCodeButton.apply {
            setOnClickListener {
                viewModel.resetPassword(
                    view.findViewById<TextInputEditText>(R.id.forgotPasswordEditEmail).text.toString()
                )
            }
        }
        Log.d(TAG, "ok")
    }
}
