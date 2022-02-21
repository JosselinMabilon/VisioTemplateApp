package com.example.visioglobe.viewmodel

import com.example.visioglobe.BaseApplication
import com.example.visioglobe.MainCoroutineRule
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.repository.AccountRepository
import com.example.visioglobe.repository.DataStoreRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccountViewModelTest {

    @MockK
    lateinit var repository: AccountRepository

    @MockK
    lateinit var context: BaseApplication

    @MockK
    lateinit var dataStore: DataStoreRepository

    private lateinit var sut: AccountViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = AccountViewModel(repository, context, dataStore)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testLogOutFunction() {
        coEvery { repository.logOut() } returns Unit
        coEvery { dataStore.clearUserInfo(context) } returns Unit

        mainCoroutineRule.runBlockingTest {
            sut.logOut()
        }

        coVerifyAll {
            repository.logOut()
            dataStore.clearUserInfo(context)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetUserInfoFunction() {
        val mockDefaultValue = ""
        val mockUser = mockk<User>()

        coEvery { dataStore.getUserInfo(eq(mockDefaultValue), context) } returns mockUser

        mainCoroutineRule.runBlockingTest {
            sut.getUserInfo()
        }

        assertThat(sut.userInfo.value).isEqualTo(mockUser)
        coVerify { dataStore.getUserInfo(eq(mockDefaultValue), context) }
    }

    @Test
    fun testChangeLogOutStateToFalseFunction() {
        val mockLogOutState = false

        sut.logOutChangeToFalse()

        assertThat(sut.logOutState.value).isEqualTo(mockLogOutState)
    }

    @Test
    fun testChangeLogOutStateToTrueFunction() {
        val mockLogOutState = true

        sut.logOutChangeToTrue()

        assertThat(sut.logOutState.value).isEqualTo(mockLogOutState)
    }
}