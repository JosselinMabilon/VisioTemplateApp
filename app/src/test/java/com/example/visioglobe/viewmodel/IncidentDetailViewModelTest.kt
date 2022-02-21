package com.example.visioglobe.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.visioglobe.MainCoroutineRule
import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.getOrAwaitValue
import com.example.visioglobe.repository.IncidentRepository
import com.example.visioglobe.util.DateFormatter
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IncidentDetailViewModelTest {

    ///////////////////////////////////////////////////////////////////////////
    // SYSTEM UNDER TEST (SUT)
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var sut: IncidentDetailViewModel

    ///////////////////////////////////////////////////////////////////////////
    // VARIABLES
    ///////////////////////////////////////////////////////////////////////////


    @MockK
    lateinit var repository: IncidentRepository

    @MockK
    lateinit var dateFormatter: DateFormatter

    @Rule @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    ///////////////////////////////////////////////////////////////////////////
    // TESTS INIT
    ///////////////////////////////////////////////////////////////////////////

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = IncidentDetailViewModel(repository, dateFormatter)
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
    fun setSelectedIncidentTest() {
        // Given
        val incidentMock = mockk<Incident>()

        // When
        sut.setSelectedIncident(incidentMock)

        // Then
        assertThat(sut.selectedIncident.getOrAwaitValue()).isEqualTo(incidentMock)
    }

    @Test
    fun resetStateTest() {
        // Given
        // When
        sut.resetState()

        // Then
        assertThat(sut.errorState.value).isFalse
        assertThat(sut.loadingState.value).isFalse
    }
}