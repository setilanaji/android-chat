package com.ydh.chatyok

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.ydh.chatyok.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeFragment : Fragment() , UserAdapter.UserListener{


    private lateinit var binding: FragmentHomeBinding
    private lateinit var action: ListenerRegistration
    private val adapter by lazy { UserAdapter(requireContext(), this) }
    private val localSession by lazy { LocalSession(requireContext()) }
    private val service by lazy { NotificationClient.service }
    private val db by lazy { Firebase.firestore }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            rvUser.adapter = adapter
        }


        action = db.collection(ConstantUtil.COLLECTION).addSnapshotListener { value, error ->
            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }

            val users = mutableListOf<UserModel>()

            value?.let {
                for (doc in it) {
                    doc.toObject(UserModel::class.java).let { model ->
                        if (model.uid != localSession.uid) {
                            users.add(model)
                        }
                    }
                }
            }

            adapter.list = users

            println(users.map { it.email })
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener { Log.e("TOKEN", it) }


        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()

        action.remove()
    }

    override fun onStart() {
        super.onStart()

//        if (localSession.uid.isEmpty()) requireActivity().onBackPressed()
    }


    override fun onSend(userModel: UserModel, message: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val payload = PayloadModel(
                    DataModel("New message", message, userModel.email),
                    userModel.token
                )
                val response = service.sendNotification(payload)
                println(response)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }
    }

}