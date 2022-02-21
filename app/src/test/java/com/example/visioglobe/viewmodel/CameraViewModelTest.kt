package com.example.visioglobe.viewmodel

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.visioglobe.BaseApplication
import com.example.visioglobe.MainCoroutineRule
import com.example.visioglobe.R
import com.example.visioglobe.domain.mapper.FurnitureNameMapper
import com.example.visioglobe.domain.model.Furniture
import com.example.visioglobe.getOrAwaitValue
import com.example.visioglobe.ml.impl.FurnitureClassifierImpl
import com.google.mlkit.vision.common.InputImage
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.sql.Timestamp
import java.time.Clock

class CameraViewModelTest {

    @MockK
    lateinit var classifier: FurnitureClassifierImpl

    @MockK
    lateinit var application: BaseApplication

    @MockK
    lateinit var mapper: FurnitureNameMapper

    private lateinit var sut: CameraViewModel

    @Rule @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = CameraViewModel(classifier = classifier, application = application, mapper = mapper)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        unmockkAll()
    }


    @Test
    fun getInputImageTestSuccess() {

        mockkStatic(InputImage::class)

        // Given
        val mediaImageMock = mockk<InputImage>()
        val imageUriMock = mockk<Uri>()
        val contextMock = mockk<Context>()

        every { application.applicationContext } returns contextMock
        every { InputImage.fromFilePath(contextMock, imageUriMock) } returns mediaImageMock

        // When
        val result = sut.getInputImage(imageUriMock)

        // Then
        assertThat(result).isEqualTo(mediaImageMock)
        assertThat(result).isInstanceOf(InputImage::class.java)
    }

    @Test
    fun getOutputDirExistCaseMediaDirExists() {
        // Given
        val spy = spyk(sut)
        val appContextMock = mockk<Context>()
        val appNameString = "VisioGlobe"
        val fileDirMock = mockk<File>()
        val fileMock2 = mockk<File>()
        val resultFile = mockk<File>()
        val currentTime = 1594805911400

        every { application.applicationContext } returns appContextMock
        every { appContextMock.resources.getString(R.string.app_name) } returns appNameString
        every { application.filesDir } returns fileDirMock
        every { spy.getFile(fileDirMock, appNameString) } returns fileMock2
        every { fileMock2.mkdirs() } returns true
        every { fileMock2.exists() } returns true
        every { spy.getTime() } returns currentTime
        every {
            spy.getFile(
                fileMock2,
                currentTime.toString() + CameraViewModel.PHOTO_EXTENSION
            )
        } returns resultFile

        // When
        val result = spy.getOutputDirectory()

        // Then
        assertThat(result).isEqualTo(resultFile)
    }

    @Test
    fun getOutputDirCaseMediaDirNotExists() {
        // Given
        val spy = spyk(sut)
        val appContextMock = mockk<Context>()
        val appNameString = "VisioGlobe"
        val fileDirMock = mockk<File>()
        val fileMock2 = mockk<File>()
        val resultFile = mockk<File>()
        val currentTime = 1594805911400

        every { application.applicationContext } returns appContextMock
        every { appContextMock.resources.getString(R.string.app_name) } returns appNameString
        every { application.filesDir } returns fileDirMock
        every { appContextMock.filesDir } returns fileDirMock
        every { spy.getFile(fileDirMock, appNameString) } returns fileMock2
        every { fileMock2.mkdirs() } returns true
        every { fileMock2.exists() } returns false
        every { spy.getTime() } returns currentTime
        every {
            spy.getFile(
                fileDirMock,
                currentTime.toString() + CameraViewModel.PHOTO_EXTENSION
            )
        } returns resultFile

        // When
        val result = spy.getOutputDirectory()

        // Then
        assertThat(result).isEqualTo(resultFile)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun runClassifierEmptyTest() {
        // Given
        val spy = spyk(sut)
        val imageUriMock = mockk<Uri>()
        val inputImageMock = mockk<InputImage>()
        val furnitureListMock = emptyList<Furniture>()


        every { spy.getInputImage(imageUriMock) } returns inputImageMock
        coEvery {
            classifier.detectFurniture(
                inputImageMock,
                imageUriMock
            )
        } returns furnitureListMock

        // When
        mainCoroutineRule.runBlockingTest { spy.runClassifier(imageUriMock) }

        // Then
        assertThat(sut.prediction.getOrAwaitValue()).isEqualTo(furnitureListMock)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun runClassifierSuccessTest() {
        // Given
        val imageUriMock = mockk<Uri>()
        val spy = spyk(sut)
        val inputImageMock = mockk<InputImage>()
        val furnitureMock = mockk<Furniture>()
        val listFurnitureMock = listOf(furnitureMock)

        every { spy.getInputImage(imageUriMock) } returns inputImageMock
        coEvery {
            classifier.detectFurniture(
                inputImageMock,
                imageUriMock
            )
        } returns listFurnitureMock

        // When
        mainCoroutineRule.runBlockingTest {
            spy.runClassifier(imageUriMock)
        }

        // Then
        assertThat(spy.prediction.getOrAwaitValue()).isEqualTo(listFurnitureMock)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun runClassifierCaseImageMediaNullTest() {
        // Given
        val imageUriMock = mockk<Uri>()
        val spy = spyk(sut)

        every { spy.getInputImage(imageUriMock) } returns null

        // When
        mainCoroutineRule.runBlockingTest {
            spy.runClassifier(imageUriMock)
        }

        // Then
        assertThat(spy.prediction.value).isNullOrEmpty()
    }
}