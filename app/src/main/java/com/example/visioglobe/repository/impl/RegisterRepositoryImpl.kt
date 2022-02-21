package com.example.visioglobe.repository.impl

import android.app.Activity
import com.example.visioglobe.constants.USER_COLLECTION
import com.example.visioglobe.repository.RegisterRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : RegisterRepository {

    override suspend fun signUp(email: String, password: String, activity: Activity): Boolean {
        return suspendCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                activity
            ) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { tasks ->
                            auth.signOut()
                            cont.resume(tasks.isSuccessful)
                        }
                } else {
                    cont.resume(false)
                }
            }
        }
    }

    override fun writeNewUser(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        permission: String
    ) {
        val user = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "phone" to phone,
            "permission" to permission,
            "site" to "XXXXXXXXXXXXXX"
        )
        firestore.collection(USER_COLLECTION).document().set(user)
    }

}