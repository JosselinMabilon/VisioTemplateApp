package com.example.visioglobe.repository.impl

import android.net.Uri
import com.example.visioglobe.constants.INCIDENT_COLLECTION
import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.network.DataState
import com.example.visioglobe.network.entity.IncidentEntity
import com.example.visioglobe.network.mapper.IncidentNetworkMapper
import com.example.visioglobe.repository.IncidentRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class IncidentRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firecloud: FirebaseStorage,
    private val mapper: IncidentNetworkMapper
) : IncidentRepository {

    override suspend fun addIncident(newIncident: Incident): DataState<DocumentReference> {
        val incidentDocument = firestore.collection(INCIDENT_COLLECTION)
        val incidentNetwork = mapper.mapToEntity(newIncident)

        return suspendCoroutine { cont ->
            incidentDocument.add(incidentNetwork).addOnSuccessListener {
                Timber.tag(TAG).i("DocumentSnapshot successfully written with ID $it")
                cont.resume(DataState.Success(it))
            }.addOnFailureListener {
                Timber.tag(TAG).w("Error writing document with exception: $it")
                cont.resume(DataState.Error(it.localizedMessage))
            }
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getIncidents() = flow {
        emit(DataState.Loading())

        val collection = firestore.collection(INCIDENT_COLLECTION)
        val dataResult = collection
            .orderBy(DATE_FIELD, Query.Direction.DESCENDING)
            .get()
            .await()

        if (dataResult != null && !dataResult.isEmpty) {
            val listResult = dataResult.toObjects(IncidentEntity::class.java).map {
                mapper.mapToDomainModel(it)
            }
            emit(DataState.Success(listResult))
        }
    }.catch {
        emit(DataState.Error(it.localizedMessage))

    }.flowOn(Dispatchers.IO)

    override suspend fun loadImage(imageId: String) = flow<DataState<String>> {
        emit(DataState.Loading())

        val storageRef = firecloud.reference
        val imagesPathRef: StorageReference = storageRef.child(IMAGE_INCIDENT_PATH)
        val imageRef = imagesPathRef.child("$imageId$IMAGE_EXTENSION")

        val result = imageRef.downloadUrl.await()

        if (result != null && result.toString().isNotEmpty()) {
            Timber.tag(TAG).i("Image URI retrieved :$result")
            emit(DataState.Success(result.toString()))
        } else {
            emit(DataState.Error(result.toString()))
        }
    }.catch {
        emit(DataState.Error(it.localizedMessage))
    }.flowOn(Dispatchers.IO)

    override suspend fun saveImage(imageUri: Uri, imageUuid: String): DataState<String> {

        val storageRef = firecloud.reference
        val fileName = "$imageUuid$IMAGE_EXTENSION"
        val imagesPathRef: StorageReference = storageRef.child("$IMAGE_INCIDENT_PATH/$fileName")

        return suspendCoroutine { cont ->
            imagesPathRef.putFile(imageUri).addOnSuccessListener {
                Timber.tag(TAG).i("Image successfully written with ref ${it.storage}")
                cont.resume(DataState.Success(imageUuid))
            }.addOnFailureListener {
                Timber.tag(TAG).w("Error writing image with exception: $it")
                cont.resume(DataState.Error(it.localizedMessage))
            }
        }
    }

    companion object {
        private const val IMAGE_INCIDENT_PATH = "images"

        private const val DATE_FIELD = "date"
        private const val IMAGE_EXTENSION = ".jpg"

        private const val TAG = "IncidentRepository"
    }
}