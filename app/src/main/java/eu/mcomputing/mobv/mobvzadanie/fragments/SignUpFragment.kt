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


class SignUpFragment : Fragment(R.layout.fragment_signup) {
    val TAG = "SignUpFragment"

    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val submitButton: Button = view.findViewById(R.id.submitButton)


        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[AuthViewModel::class.java]


        viewModel.registrationResult.observe(viewLifecycleOwner){
            if (it.second != null){
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
                viewModel.registerUser(
                    view.findViewById<TextInputEditText>(R.id.editUsername).text.toString(),
                    view.findViewById<TextInputEditText>(R.id.editTextEmailReg).text.toString(),
                    view.findViewById<TextInputEditText>(R.id.editPasswordReg).text.toString(),
                    view.findViewById<TextInputEditText>(R.id.editRepeatPassword).text.toString()
                )
            }
        }


        val alreadyLoggedIn: TextView = view.findViewById(R.id.alreadyLoggedInLink)
        alreadyLoggedIn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

}