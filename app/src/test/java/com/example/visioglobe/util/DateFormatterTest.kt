package com.example.visioglobe.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.visioglobe.util.impl.DateFormatterImpl
import io.mockk.MockKAnnotations
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class DateFormatterTest {

    private lateinit var sut: DateFormatterImpl

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = DateFormatterImpl()
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun mapToDateStringTest() {
        // Given
        val dateMock: Long = 1628089255703
        val stringMock = "04/08/21 05:00 PM"

        // When
        val result = sut.mapToDateString(dateMock)

        // Then
        assertThat(result).isEqualTo(stringMock)
    }

    @Test
    fun mapToDateStringWrongValueTest() {
        mockkConstructor(Locale::class)
        // Given
        val dateMock: Long = -1
        val baseDateStringMock = "01/01/70 12:59 AM"

        // When
        val result = sut.mapToDateString(dateMock)

        // Then
        assertThat(result).isEqualTo(baseDateStringMock)
    }

}