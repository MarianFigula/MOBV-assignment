package eu.mcomputing.mobv.mobvzadanie

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ChangePasswordFragment: Fragment(R.layout.fragment_forgot_password_reset) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resetPasswordButton: Button = view.findViewById(R.id.resetPasswordButton)
        resetPasswordButton.setOnClickListener {
            // TODO: moze sa asi s emailom poslat a predvyplnit ho v logine
            // TODO: emailova validacia
            findNavController().navigate(R.id.loginFragment)
        }
    }
}