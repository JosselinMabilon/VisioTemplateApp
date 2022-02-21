package com.example.visioglobe.ml.impl

import android.net.Uri
import androidx.annotation.VisibleForTesting
import com.example.visioglobe.domain.model.Furniture
import com.example.visioglobe.ml.FurnitureClassifier
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FurnitureClassifierImpl @Inject constructor() :
    FurnitureClassifier {

    override suspend fun detectFurniture(image: InputImage, imageUri: Uri): List<Furniture> {
        return suspendCoroutine { continuation ->
            getLabeler().process(image)
                .addOnSuccessListener { labels ->
                    val listFurniture = labels.map { obj ->
                        val furniture = Furniture(
                            name = obj.text,
                            confidence = obj.confidence,
                            index = obj.index,
                            imageUri = imageUri
                        )
                        Timber.tag(TAG).d("${furniture.imageUri}")
                        furniture
                    }
                    continuation.resume(listFurniture)
                }
                .addOnFailureListener { e ->
                    Timber.tag(TAG).e("No object detected")
                    continuation.resumeWithException(e)
                }
        }
    }

    @VisibleForTesting
    fun getLabeler(): ImageLabeler {
        val localModel = LocalModel.Builder()
            .setAssetFilePath(MODEL_PATH)
            .build()

        val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
            .setMaxResultCount(MAX_RESULT)
            .build()
        return ImageLabeling.getClient(customImageLabelerOptions)
    }

    companion object {
        private const val TAG = "FurnitureClassifier"
        private const val MODEL_PATH = "furnitureClassierv2.tflite"
        private const val MAX_RESULT = 25
    }
}