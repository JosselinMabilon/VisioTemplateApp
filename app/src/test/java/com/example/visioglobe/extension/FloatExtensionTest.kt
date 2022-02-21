package com.example.visioglobe.extension

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any

class FloatExtensionTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic("com.example.visioglobe.extension.FloatExtensionKt")
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun percentageTest() {
        // Given
        val floatTest2 = 32.12F

        // When

        // Then
        assertThat(floatTest2.percentage()).isEqualTo(floatTest2 * PERCENTAGE)
    }

}