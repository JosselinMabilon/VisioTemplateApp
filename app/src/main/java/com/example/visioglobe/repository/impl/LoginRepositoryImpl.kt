package com.example.visioglobe.repository.impl

import android.app.Activity
import com.example.visioglobe.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : LoginRepository {

    override suspend fun userIsConnected(): Boolean {
        return suspendCoroutine { const ->
            if (auth.currentUser?.email != null) {
                const.resume(true)
            } else {
                const.resume(false)
            }
        }
    }

    override suspend fun signIn(email: String, password: String, activity: Activity) : Boolean {
        return suspendCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified == true) {
                        cont.resume(true)
                    } else {
                        cont.resume(false)
                    }
                } else {
                    cont.resume(false)
                }
            }
        }
    }

    override suspend fun resetPassword(email: String): Boolean {
        return suspendCoroutine { cont ->
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        cont.resume(true)
                    } else {
                        cont.resume(false)
                    }
                }
        }
    }
}