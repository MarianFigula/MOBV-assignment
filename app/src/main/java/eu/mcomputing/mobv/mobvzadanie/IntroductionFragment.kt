package eu.mcomputing.mobv.mobvzadanie

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class IntroductionFragment: Fragment(R.layout.fragment_intro) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton: Button = view.findViewById(R.id.buttonSignIn)
        val registerButton: Button = view.findViewById(R.id.buttonSignUp)

        loginButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.loginFragment)
        }

        registerButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.signUpFragment)

        }

    }
}