package com.example.visioglobe.viewmodel

import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visioglobe.BaseApplication
import com.example.visioglobe.R
import com.example.visioglobe.domain.mapper.FurnitureNameMapper
import com.example.visioglobe.domain.model.Furniture
import com.example.visioglobe.ml.FurnitureClassifier
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val classifier: FurnitureClassifier,
    private val application: BaseApplication,
    val mapper: FurnitureNameMapper
) : ViewModel() {

    private val _prediction = MutableLiveData<List<Furniture>>()
    val prediction: LiveData<List<Furniture>> = _prediction

    fun runClassifier(imageUri: Uri) {
        val imageMedia = getInputImage(imageUri)

        viewModelScope.launch(Dispatchers.IO) {
            imageMedia?.let {
                _prediction.postValue(
                    classifier.detectFurniture(
                        imageMedia,
                        imageUri
                    )
                )
            }
        }
    }

    @VisibleForTesting
    fun getInputImage(imageUri: Uri): InputImage? {
        val appContext = application.applicationContext
        var mediaImage: InputImage? = null

        try {
            mediaImage = InputImage.fromFilePath(appContext, imageUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return mediaImage
    }

    fun getOutputDirectory(): File {
        val appContext = application.applicationContext
        val mediaDir = application.filesDir.let {
            getFile(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir.exists()) {
            getFile(mediaDir, getTime().toString() + PHOTO_EXTENSION)
        } else {
            getFile(appContext.filesDir, getTime().toString() + PHOTO_EXTENSION)
        }
    }

    @VisibleForTesting
    fun getTime() = System.currentTimeMillis()

    @VisibleForTesting
    fun getFile(parent: File, child: String) = File(parent, child)

    companion object {
        @VisibleForTesting
        const val PHOTO_EXTENSION = ".jpg"
    }
}