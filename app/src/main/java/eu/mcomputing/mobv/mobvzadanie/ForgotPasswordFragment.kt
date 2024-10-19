package eu.mcomputing.mobv.mobvzadanie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlin.random.Random

class ForgotPasswordFragment: Fragment(R.layout.fragment_forgot_password) {
    val TAG = "ForgotPasswordFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sendEmailCode: Button = view.findViewById(R.id.sendEmailCodeButton)

        sendEmailCode.setOnClickListener {
            val email:String = view.findViewById<TextInputEditText?>(R.id.forgotPasswordEditEmail).
            text.toString()

            if (email == "") {
                Log.d("TAG", "email je prazdny")
            }
            else {
                // Generate a random 6-digit code
                val code = Random.nextInt(100000, 999999).toString()

                // Create email intent
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                    putExtra(Intent.EXTRA_SUBJECT, "Password Reset Code")
                    putExtra(Intent.EXTRA_TEXT, "Your password reset code is: $code")
                }

                findNavController().navigate(R.id.forgotPasswordResetFragment)

//                try {
//                    // Open the email client
//                    startActivity(Intent.createChooser(intent, "Send Email"))
//                } catch (e: Exception) {
//                    Log.d(TAG, "No email app found: ${e.message}")
//                }
            }



        }


        Log.d(TAG, "ok")
    }
}