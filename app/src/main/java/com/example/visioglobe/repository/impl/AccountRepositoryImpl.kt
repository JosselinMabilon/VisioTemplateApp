package com.example.visioglobe.repository.impl

import com.example.visioglobe.constants.*
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.network.entity.UserEntity
import com.example.visioglobe.network.mapper.UserNetworkMapper
import com.example.visioglobe.repository.AccountRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.isActive
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AccountRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val mapper: UserNetworkMapper
) : AccountRepository {

    override suspend fun logOut() {
        auth.signOut()
    }

    override suspend fun getUserId(): String {
        return suspendCoroutine { cont ->
            val email = auth.currentUser?.email
            firestore.collection(USER_COLLECTION)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.get(emailAttribute) == email) {
                            cont.resume(document.id)
                            break
                        }
                    }
                    if (!cont.context.isActive)
                        cont.resume("")
                }
        }
    }

    override suspend fun getUserInfos(id: String): User {
        return suspendCoroutine { cont ->
            firestore.collection(USER_COLLECTION).document(id).get().addOnSuccessListener { result ->
                cont.resume(
                    mapper.toDomainInModel(
                        UserEntity(
                            id = id,
                            email = result.get(emailAttribute) as String,
                            firstName = result.get(firstNameAttribute) as String,
                            lastName = result.get(lastNameAttribute) as String,
                            permission = result.get(permissionAttribute) as String,
                            phone = result.get(phoneAttribute) as String,
                            site = result.get(siteAttribute) as String
                        )
                    )
                )
            }
        }
    }
}