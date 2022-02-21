package com.example.visioglobe.viewmodel

import com.example.visioglobe.MainCoroutineRule
import com.example.visioglobe.repository.LoginRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

class HomeViewModelTest {

    @MockK
    lateinit var loginRepository: LoginRepository

    private lateinit var sut: HomeViewModel

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this)
        sut = HomeViewModel(loginRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testUserIsConnectedFailed() {
        val mockIsConnectedSuccess = false

        coEvery { loginRepository.userIsConnected() } returns mockIsConnectedSuccess

        mainCoroutineRule.runBlockingTest {
            sut.userIsConnected()
        }

        assertThat(sut.isConnected.value).isEqualTo(mockIsConnectedSuccess)
    }
}