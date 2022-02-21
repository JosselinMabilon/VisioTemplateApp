package com.example.visioglobe.ml.impl

import android.net.Uri
import com.example.visioglobe.domain.model.Furniture
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class FurnitureClassifierImplTest {

    ///////////////////////////////////////////////////////////////////////////
    // SYSTEM UNDER TEST (SUT)
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var sut: FurnitureClassifierImpl

    ///////////////////////////////////////////////////////////////////////////
    // TESTS INIT
    ///////////////////////////////////////////////////////////////////////////

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = FurnitureClassifierImpl()
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        unmockkAll()
    }

    ///////////////////////////////////////////////////////////////////////////
    // TESTS
    ///////////////////////////////////////////////////////////////////////////

    @Test
    fun detectFurnitureTest() {
        // Given
        val spy = spyk(sut)
        val imageMock = mockk<InputImage>()
        val imageUriMock = mockk<Uri>()
        val labelerMock = mockk<ImageLabeler>()
        val labelMock = mockk<ImageLabel>()
        val listLabels = listOf(labelMock)

        val text = "text"
        val confidence = 0.9f
        val index = 0

        val callbackSlot = slot<OnSuccessListener<List<ImageLabel>>>()
        val task = Tasks.forResult(listLabels)

        every { spy.getLabeler() } returns labelerMock

        every { labelMock.text } returns text
        every { labelMock.confidence } returns confidence
        every { labelMock.index } returns index

        every {
            labelerMock.process(imageMock).addOnSuccessListener(capture(callbackSlot))
        } answers {
            callbackSlot.captured.onSuccess(listLabels)
            task
        }

        val result: List<Furniture>

        // When
        runBlocking {
            result = spy.detectFurniture(imageMock, imageUriMock)
        }

        // Then
        assertThat(result).isNotEmpty
        assertThat(result[0].index).isEqualTo(index)
        assertThat(result[0].confidence).isEqualTo(confidence)
        assertThat(result[0].name).isEqualTo(text)
        assertThat(result[0].imageUri).isEqualTo(imageUriMock)
    }

    @Test(expected = Exception::class)
    fun detectFurnitureFailureTest() {
        // Given
        val spy = spyk(sut)
        val imageMock = mockk<InputImage>()
        val imageUriMock = mockk<Uri>()
        val labelerMock = mockk<ImageLabeler>()
        val exceptionMock = mockk<Exception>()

        val callbackSlot = slot<OnFailureListener>()
        val task = Tasks.forResult(emptyList<ImageLabel>())

        every { spy.getLabeler() } returns labelerMock
        every {
            labelerMock.process(imageMock).addOnFailureListener(capture(callbackSlot))
        } answers {
            callbackSlot.captured.onFailure(exceptionMock)
            task
        }

        // When
        runBlocking {
            spy.detectFurniture(imageMock, imageUriMock)
        }
    }
}