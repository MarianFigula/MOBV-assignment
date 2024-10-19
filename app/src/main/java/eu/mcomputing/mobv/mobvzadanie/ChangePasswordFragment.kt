package eu.mcomputing.mobv.mobvzadanie

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ChangePasswordFragment: Fragment(R.layout.fragment_forgot_password_reset) {

    private lateinit var viewModel: AuthViewModel
    val TAG = "ChangePasswordFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resetPasswordButton: Button = view.findViewById(R.id.changePasswordButton)
        resetPasswordButton.setOnClickListener {

            findNavController().navigate(R.id.loginFragment)
        }
    }
}