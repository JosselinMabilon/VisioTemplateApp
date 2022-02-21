package com.example.visioglobe.viewmodel

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.visioglobe.BaseApplication
import com.example.visioglobe.MainCoroutineRule
import com.example.visioglobe.R
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.getOrAwaitValue
import com.example.visioglobe.repository.AccountRepository
import com.example.visioglobe.repository.DataStoreRepository
import com.example.visioglobe.repository.LoginRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

class LoginViewModelTest {

    @MockK
    lateinit var repository: LoginRepository

    @MockK
    lateinit var context: BaseApplication

    @MockK
    lateinit var dataStore: DataStoreRepository

    @MockK
    lateinit var accountRepository: AccountRepository

    private lateinit var sut: LoginViewModel

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        val mockBaseErrorMessage = "Email or Password must be incorrect"

        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this)
        every { context.getString(eq(R.string.login_incorrect_info)) } returns mockBaseErrorMessage
        sut = LoginViewModel(repository, context, dataStore, accountRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    ///////////////////////////////////////////////////
    //////      TEST BEGIN
    ///////////////////////////////////////////////////

    @ExperimentalCoroutinesApi
    @Test
    fun testUserIsConnectedFailed() {
        val mockIsConnectedSuccess = false

        coEvery { repository.userIsConnected() } returns mockIsConnectedSuccess

        mainCoroutineRule.runBlockingTest {
            sut.userIsConnected()
        }

        assertThat(sut.isConnected.value).isEqualTo(mockIsConnectedSuccess)
    }

    @Test
    fun testSignInFunctionWithEmptyEmail() {
        val mockEmail = ""
        val mockPassword = "test@password*"
        val mockActivity = mockk<Activity>()
        val mockErrorMessage = "Password or email is empty"
        val mockErrorState = true
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.login_empty_field)) } returns mockErrorMessage

        sut.signIn(mockEmail, mockPassword, mockActivity)

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignInSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignInFunctionWithEmptyPassword() {
        val mockEmail = "paul.dupon@test.com"
        val mockPassword = ""
        val mockActivity = mockk<Activity>()
        val mockErrorMessage = "Password or email is empty"
        val mockErrorState = true
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.login_empty_field)) } returns mockErrorMessage

        sut.signIn(mockEmail, mockPassword, mockActivity)

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignInSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignInFunctionWithWrongEmailPattern() {
        val mockEmail = "paul/dupon@test.com"
        val mockPassword = "test@password*"
        val mockActivity = mockk<Activity>()
        val mockErrorMessage = "Email have characters which are not allowed"
        val mockErrorState = true
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.login_wrong_email_pattern)) } returns mockErrorMessage

        sut.signIn(mockEmail, mockPassword, mockActivity)

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignInSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSignInFunctionCallFireBaseReturnFalse() {
        val mockEmail = "paul.dupon@orange.com"
        val mockPassword = "test@password*"
        val mockErrorMessage = "Email or Password must be incorrect"
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockEventSignInSuccess = 2
        val mockSignInResult = false

        coEvery {
            repository.signIn(
                eq(mockEmail),
                eq(mockPassword),
                mockActivity
            )
        } returns mockSignInResult
        coEvery { context.getString(eq(R.string.login_incorrect_info)) } returns mockErrorMessage

        mainCoroutineRule.runBlockingTest {
            sut.signIn(mockEmail, mockPassword, mockActivity)
            delay(1_000)
        }

        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignInSuccess.getOrAwaitValue()).isEqualTo(mockEventSignInSuccess)
        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        coVerify { repository.signIn(eq(mockEmail), eq(mockPassword), mockActivity) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSaveUserInfoFunction() {
        val idMock = "HFJKDSHFDKSHFJKDS"
        val userInfoMock = mockk<User>()

        coEvery { accountRepository.getUserId() } returns idMock
        coEvery { accountRepository.getUserInfos(eq(idMock)) } returns userInfoMock
        coEvery { dataStore.saveUserInfo(eq(userInfoMock), context) } returns Unit

        mainCoroutineRule.runBlockingTest {
            sut.saveUserInfo()
        }

        coVerify { dataStore.saveUserInfo(eq(userInfoMock), context) }
    }

    @Test
    fun testEventSignInResetFunctionAssignZero() {
        val mockEventSignInSuccess = 0

        sut.eventSignInReset()

        assertThat(sut.eventSignInSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testResetPasswordFunctionEmptyPassword() {
        val mockEmail = ""
        val mockErrorMessage = "email or email is empty"
        val mockErrorState = true
        val mockEventResetPasswordSuccess = 2

        every { context.getString(eq(R.string.register_empty_field)) } returns mockErrorMessage

        sut.resetPassword(mockEmail)

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventResetPasswordSuccess.value).isEqualTo(mockEventResetPasswordSuccess)
    }

    @Test
    fun testResetPasswordFunctionWithWrongEmailPattern() {
        val mockEmail = "paul/dupon@test.com"
        val mockErrorMessage = "Email have characters which are not allowed"
        val mockErrorState = true
        val mockEventResetPasswordSuccess = 2

        every { context.getString(eq(R.string.login_wrong_email_pattern)) } returns mockErrorMessage

        sut.resetPassword(mockEmail)

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventResetPasswordSuccess.value).isEqualTo(mockEventResetPasswordSuccess)
    }

    @Test
    fun testResetPasswordFunctionWithTrueAsReturnFunction() {
        val mockEmail = "paul.dupont@orange.com"
        val mockErrorState = false
        val mockEventResetPasswordSuccess = 1

        coEvery { repository.resetPassword(eq(mockEmail)) } returns true

        sut.resetPassword(mockEmail)

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.eventResetPasswordSuccess.value).isEqualTo(mockEventResetPasswordSuccess)
    }

    @Test
    fun testEventResetPasswordResetFunction() {
        val mockEventResetPasswordSuccess = 0

        sut.eventResetPasswordReset()

        assertThat(sut.eventResetPasswordSuccess.value).isEqualTo(mockEventResetPasswordSuccess)
    }

    @Test
    fun getUserIdTest() {
        // Given
        val userIdMock = "HFK48n6Hfz56O"

        coEvery { accountRepository.getUserId() } returns userIdMock

        // When
        var result = ""
        mainCoroutineRule.runBlockingTest {
            result = sut.getUserId()
        }

        // Then
        assertThat(result).isEqualTo(userIdMock)
    }
}