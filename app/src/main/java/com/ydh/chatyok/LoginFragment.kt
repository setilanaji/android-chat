package com.ydh.chatyok

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private val db by lazy { Firebase.firestore }
    private val localSession by lazy { LocalSession(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://chatyok.page.link/finishSignUp"
            // This must be true
            handleCodeInApp = true
            setAndroidPackageName(
                "com.ydh.chatyok",
                true, /* installIfNotAvailable */
                "12" /* minimumVersion */)
        }

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(),
            SIGN_IN_RESULT_CODE)


        return inflater.inflate(R.layout.fragment_login, container, false)
    }
//
//    override fun onStart() {
//        super.onStart()
//        if (localSession.uid.isNotEmpty()) findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                user?.uid?.let { uid ->
                    localSession.uid = uid

                    db.collection(ConstantUtil.COLLECTION).document(uid)
                        .set(UserModel(uid, user.email!!))
                        .addOnSuccessListener {
//                            showLoading(false)

                        }.addOnFailureListener { exc ->
                            exc.printStackTrace()

//                            showLoading(false)
                        }
                }
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

            }

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

