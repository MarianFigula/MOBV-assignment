package eu.mcomputing.mobv.mobvzadanie.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import eu.mcomputing.mobv.mobvzadanie.viewmodels.AuthViewModel
import eu.mcomputing.mobv.mobvzadanie.DataRepository
import eu.mcomputing.mobv.mobvzadanie.R


class LoginFragment : Fragment(R.layout.fragment_login) {
    val TAG = "LoginFragment"
    private lateinit var viewModel: AuthViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val submitButton : Button = view.findViewById(R.id.submitButton)
        val forgotPassword: TextView = view.findViewById(R.id.forgotPasswordLink)
        val notYetAUser: TextView  = view.findViewById(R.id.notYetAUserLink)

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[AuthViewModel::class.java]

        viewModel.loginResult.observe(viewLifecycleOwner){
            val user = it.second

            if (user != null && user.id.toInt() >= 0){
                requireView().findNavController().navigate(R.id.feedFragment)
            }else{
                Snackbar.make(
                    submitButton,
                    it.first,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        submitButton.apply {
            setOnClickListener {
                viewModel.loginUser(
                    view.findViewById<TextInputEditText>(R.id.editTextEmail).text.toString(),
                    view.findViewById<TextInputEditText>(R.id.editTextPassword).text.toString(),
                )
            }
        }

        forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }

        notYetAUser.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }
    }
}