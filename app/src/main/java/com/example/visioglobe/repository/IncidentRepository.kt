package com.example.visioglobe.repository

import android.net.Uri
import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.network.DataState
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow

interface IncidentRepository {

    suspend fun addIncident(newIncident: Incident): DataState<DocumentReference>

    suspend fun getIncidents(): Flow<DataState<List<Incident>>>

    suspend fun loadImage(imageId: String): Flow<DataState<String>>

    suspend fun saveImage(imageUri: Uri, imageUuid: String): DataState<String>
}