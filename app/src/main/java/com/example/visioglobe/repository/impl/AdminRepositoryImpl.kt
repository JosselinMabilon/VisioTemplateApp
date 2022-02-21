package com.example.visioglobe.repository.impl

import com.example.visioglobe.constants.USER_COLLECTION
import com.example.visioglobe.constants.permissionAttribute
import com.example.visioglobe.network.DataState
import com.example.visioglobe.network.entity.UserEntity
import com.example.visioglobe.network.mapper.UserNetworkMapper
import com.example.visioglobe.repository.AdminRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class AdminRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val mapper: UserNetworkMapper
) : AdminRepository {

    @ExperimentalCoroutinesApi
    override suspend fun getUsers() = flow {
        emit(DataState.Loading())

        val collection = firestore.collection(USER_COLLECTION)
        val dataResult = collection
            .get()
            .await()

        if (dataResult != null && !dataResult.isEmpty) {
            val listResult = mapper.toDomainModelList(
                dataResult.toObjects(UserEntity::class.java)
            )
            emit(DataState.Success(listResult))
        }
    }.catch {
        emit(DataState.Error(it.localizedMessage))

    }.flowOn(Dispatchers.IO)

}