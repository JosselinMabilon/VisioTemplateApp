package com.example.visioglobe.ml

import android.net.Uri
import com.example.visioglobe.domain.model.Furniture
import com.google.mlkit.vision.common.InputImage

interface FurnitureClassifier {

    suspend fun detectFurniture(image: InputImage, imageUri: Uri): List<Furniture>
}