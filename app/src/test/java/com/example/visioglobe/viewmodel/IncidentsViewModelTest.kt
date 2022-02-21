package com.example.visioglobe.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.visioglobe.MainCoroutineRule
import com.example.visioglobe.repository.IncidentRepository
import com.example.visioglobe.util.DateFormatter
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IncidentsViewModelTest {

    @MockK
    lateinit var repository: IncidentRepository

    @MockK
    lateinit var dateFormatter: DateFormatter

    lateinit var sut: IncidentsViewModel

    @Rule @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = IncidentsViewModel(repository, dateFormatter)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun resetStateTest() {
        sut.resetState()
        assertThat(sut.loadingState.value).isFalse
        assertThat(sut.errorState.value).isFalse
    }

}