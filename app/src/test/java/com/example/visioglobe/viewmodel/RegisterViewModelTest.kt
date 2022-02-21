package com.example.visioglobe.viewmodel

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.visioglobe.BaseApplication
import com.example.visioglobe.MainCoroutineRule
import com.example.visioglobe.R
import com.example.visioglobe.constants.PASSWORD_PATTERN
import com.example.visioglobe.repository.RegisterRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

class RegisterViewModelTest {

    @MockK
    lateinit var repository: RegisterRepository

    @MockK
    lateinit var context: BaseApplication

    private lateinit var sut: RegisterViewModel
    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Rule @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        val mockBaseErrorMessage =
            "There\\'s a problem, please make sure your information are correct or your are maybe already register"

        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this)
        every { context.getString(eq(R.string.register_incorrect_info)) } returns mockBaseErrorMessage
        sut = RegisterViewModel(repository, context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testSignUpFunctionWithEmptyFirstName() {
        val mockFirstName = ""
        val mockLastName = "Dupont"
        val mockEmail = "julien.dupont@orange.com"
        val mockPhoneNumber = "0752654899"
        val mockPassword = "test@Password*1"
        val mockConfirmPassword = "test@Password*1"
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockErrorMessage = "fields are empty"
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.register_empty_field)) } returns mockErrorMessage

        sut.signUp(
            mockFirstName,
            mockLastName,
            mockEmail,
            mockPhoneNumber,
            mockPassword,
            mockConfirmPassword,
            mockActivity
        )

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignUpFunctionWithEmptyLastName() {
        val mockFirstName = "Julien"
        val mockLastName = ""
        val mockEmail = "julien.dupont@orange.com"
        val mockPhoneNumber = "0752654899"
        val mockPassword = "test@Password*1"
        val mockConfirmPassword = "test@Password*1"
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockErrorMessage = "fields are empty"
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.register_empty_field)) } returns mockErrorMessage

        sut.signUp(
            mockFirstName,
            mockLastName,
            mockEmail,
            mockPhoneNumber,
            mockPassword,
            mockConfirmPassword,
            mockActivity
        )

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignUpFunctionWithEmptyEmail() {
        val mockFirstName = "Julien"
        val mockLastName = "Dupont"
        val mockEmail = ""
        val mockPhoneNumber = "0752654899"
        val mockPassword = "test@Password*1"
        val mockConfirmPassword = "test@Password*1"
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockErrorMessage = "fields are empty"
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.register_empty_field)) } returns mockErrorMessage

        sut.signUp(
            mockFirstName,
            mockLastName,
            mockEmail,
            mockPhoneNumber,
            mockPassword,
            mockConfirmPassword,
            mockActivity
        )

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignUpFunctionWithEmptyPassword() {
        val mockFirstName = "Julien"
        val mockLastName = "Dupont"
        val mockEmail = "julien.dupont@orange.com"
        val mockPhoneNumber = "0752654899"
        val mockPassword = ""
        val mockConfirmPassword = "test@Password*1"
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockErrorMessage = "fields are empty"
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.register_empty_field)) } returns mockErrorMessage

        sut.signUp(
            mockFirstName,
            mockLastName,
            mockEmail,
            mockPhoneNumber,
            mockPassword,
            mockConfirmPassword,
            mockActivity
        )

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignUpFunctionWithEmptyConfirmPassword() {
        val mockFirstName = "Julien"
        val mockLastName = "Dupont"
        val mockEmail = "julien.dupont@orange.com"
        val mockPhoneNumber = "0752654899"
        val mockPassword = "test@Password*1"
        val mockConfirmPassword = ""
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockErrorMessage = "fields are empty"
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.register_empty_field)) } returns mockErrorMessage

        sut.signUp(
            mockFirstName,
            mockLastName,
            mockEmail,
            mockPhoneNumber,
            mockPassword,
            mockConfirmPassword,
            mockActivity
        )

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignUpFunctionWithWrongEmailPattern() {
        val mockFirstName = "Julien"
        val mockLastName = "Dupont"
        val mockEmail = "julien/dupon@test.com"
        val mockPhoneNumber = "0752654899"
        val mockPassword = "test@Password*1"
        val mockConfirmPassword = "test@Password*1"
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockErrorMessage = "Email have characters which are not allowed"
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.login_wrong_email_pattern)) } returns mockErrorMessage

        sut.signUp(
            mockFirstName,
            mockLastName,
            mockEmail,
            mockPhoneNumber,
            mockPassword,
            mockConfirmPassword,
            mockActivity
        )

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignUpFunctionWithPhoneNumberNotEnoughNumber() {
        val mockFirstName = "Julien"
        val mockLastName = "Dupont"
        val mockEmail = "julien.dupont@orange.com"
        val mockPhoneNumber = "075265"
        val mockPassword = "test@Password*1"
        val mockConfirmPassword = "test@Password*1"
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockErrorMessage = "Not enough number in this phone number"
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.register_check_phone_length)) } returns mockErrorMessage

        sut.signUp(
            mockFirstName,
            mockLastName,
            mockEmail,
            mockPhoneNumber,
            mockPassword,
            mockConfirmPassword,
            mockActivity
        )

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignUpFunctionWithPhoneNumberTooManyNumber() {
        val mockFirstName = "Julien"
        val mockLastName = "Dupont"
        val mockEmail = "julien.dupont@orange.com"
        val mockPhoneNumber = "0752654581254"
        val mockPassword = "test@Password*1"
        val mockConfirmPassword = "test@Password*1"
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockErrorMessage = "Too many number in this phone number"
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.register_check_phone_length)) } returns mockErrorMessage

        sut.signUp(
            mockFirstName,
            mockLastName,
            mockEmail,
            mockPhoneNumber,
            mockPassword,
            mockConfirmPassword,
            mockActivity
        )

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignUpFunctionWithWrongPasswordPattern() {
        val mockFirstName = "Julien"
        val mockLastName = "Dupont"
        val mockEmail = "julien.dupont@orange.com"
        val mockPhoneNumber = "0748956899"
        val mockPassword = "testpw"
        val mockConfirmPassword = "test@Password*1"
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockErrorMessage = "Wrong password pattern"
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.register_wrong_password_pattern)) } returns mockErrorMessage

        sut.signUp(
            mockFirstName,
            mockLastName,
            mockEmail,
            mockPhoneNumber,
            mockPassword,
            mockConfirmPassword,
            mockActivity
        )

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testSignUpFunctionWithConfirmPasswordDifferentFromPassword() {
        val mockFirstName = "Julien"
        val mockLastName = "Dupont"
        val mockEmail = "julien.dupont@orange.com"
        val mockPhoneNumber = "0748956899"
        val mockPassword = "test@Password*1"
        val mockConfirmPassword = "test@Password*2"
        val mockActivity = mockk<Activity>()
        val mockErrorState = true
        val mockErrorMessage = "Confirm password different from password"
        val mockEventSignInSuccess = 2

        every { context.getString(eq(R.string.register_wrong_confirm_password)) } returns mockErrorMessage

        sut.signUp(
            mockFirstName,
            mockLastName,
            mockEmail,
            mockPhoneNumber,
            mockPassword,
            mockConfirmPassword,
            mockActivity
        )

        assertThat(sut.errorState.value).isEqualTo(mockErrorState)
        assertThat(sut.errorMessage.value).isEqualTo(mockErrorMessage)
        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testEventSignInResetFunctionAssignZero() {
        val mockEventSignInSuccess = 0

        sut.eventSignUpReset()

        assertThat(sut.eventSignUpSuccess.value).isEqualTo(mockEventSignInSuccess)
    }

    @Test
    fun testGeneratePasswordFunctionReturnValue() {
        val result = sut.generatePassword()

        assertThat(result.matches(PASSWORD_PATTERN.toRegex())).isEqualTo(true)
    }
}