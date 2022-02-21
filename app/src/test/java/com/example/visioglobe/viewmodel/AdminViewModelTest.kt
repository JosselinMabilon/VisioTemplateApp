package com.example.visioglobe.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.visioglobe.MainCoroutineRule
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.manager.AdminManager
import com.example.visioglobe.network.DataState
import com.example.visioglobe.repository.DataStoreRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AdminViewModelTest {
    @MockK
    lateinit var adminManager: AdminManager

    @MockK
    lateinit var dataStore: DataStoreRepository

    private lateinit var sut: AdminViewModel

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = AdminViewModel(adminManager, dataStore, mainCoroutineRule.dispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        unmockkAll()
    }

    ///////////////////////////////////////////////////
    //////      TEST DATA
    ///////////////////////////////////////////////////

    val userTest = User(
        "56vhz5hfjz56",
        "magali@gmail.com",
        "Magali",
        "Test",
        "admin",
        "0600000000",
        "Lyon"
    )

    val userTest2 = User(
        "fj7jHg4FKDP86g",
        "josselin@gmail.com",
        "Josselin",
        "Test",
        "user",
        "0600000000",
        "Lyon"
    )

    val userListTest = listOf(userTest, userTest2)
    val userIdMock = "jeiozjfe6HDK"

    ///////////////////////////////////////////////////
    //////      TEST BEGIN
    ///////////////////////////////////////////////////

    @ExperimentalCoroutinesApi
    @Test
    fun checkUserTest() {
        // Given
        val contextMock = mockk<Context>()
        val userMock = mockk<User>(relaxed = true)

        coEvery { dataStore.getUserInfo(defaultValue = "", context = contextMock) } returns userMock

        // When
        mainCoroutineRule.runBlockingTest { sut.checkUser(contextMock) }

        // Then
        assertThat(sut.userId.value).isEqualTo(userMock.id)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkUserFailTest() {
        // Given
        val contextMock = mockk<Context>()

        coEvery { dataStore.getUserInfo(defaultValue = "", context = contextMock) } throws Error()

        // When
        mainCoroutineRule.runBlockingTest { sut.checkUser(contextMock) }

        // Then
        assertThat(sut.userId.value).isNullOrEmpty()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun usersListValueTest() {
        // Given
        val spy = spyk(sut)
        val flowMock = flowOf(DataState.Success(userListTest))

        spy.userId.value = userIdMock

        coEvery { adminManager.getUsersOrderByPermission() } returns flowMock

        // When
        var result: List<User> = emptyList()
        mainCoroutineRule.runBlockingTest {
            result = spy.usersList.single()
        }

        // Then
        assertThat(result).isEqualTo(userListTest)
        assertThat(spy.errorState.value).isFalse
        assertThat(spy.loadingState.value).isFalse
        coVerify { adminManager.getUsersOrderByPermission() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun usersListSelfValueTest() {
        // Given
        val spy = spyk(sut)
        val flowMock = flowOf(DataState.Success(userListTest))
        val selfId = userTest.id
        spy.userId.value = selfId

        coEvery { adminManager.getUsersOrderByPermission() } returns flowMock

        // When
        var result: List<User> = emptyList()
        mainCoroutineRule.runBlockingTest {
            result = spy.usersList.single()
        }

        // Then
        assertThat(result).isNotEqualTo(userListTest)
        assertThat(result).isEqualTo(listOf(userTest2))
        assertThat(result).doesNotContain(userTest)
        assertThat(spy.errorState.value).isFalse
        assertThat(spy.loadingState.value).isFalse
        coVerify { adminManager.getUsersOrderByPermission() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun usersListErrorTest() {
        // Given
        val spy = spyk(sut)
        val dataStateErrorMock = mockk<DataState.Error<List<User>>>()
        val flowMock = flowOf(dataStateErrorMock)

        spy.userId.value = userIdMock

        coEvery { adminManager.getUsersOrderByPermission() } returns flowMock

        // When
        var listResult = emptyList<User>()
        mainCoroutineRule.runBlockingTest {
            spy.usersList.collect {
                listResult = it
            }
        }

        // Then
        assertThat(spy.errorState.value).isTrue
        assertThat(spy.loadingState.value).isFalse
        assertThat(listResult).isEmpty()
        coVerify { adminManager.getUsersOrderByPermission() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun usersListLoadingTest() {
        // Given
        val spy = spyk(sut)
        val dataStateErrorMock = mockk<DataState.Loading<List<User>>>()
        val flowMock = flowOf(dataStateErrorMock)

        spy.userId.value = userIdMock

        coEvery { adminManager.getUsersOrderByPermission() } returns flowMock

        // When
        var listResult = emptyList<User>()
        mainCoroutineRule.runBlockingTest {
            spy.usersList.collect {
                listResult = it
            }
        }

        // Then
        assertThat(spy.errorState.value).isFalse
        assertThat(spy.loadingState.value).isTrue
        assertThat(listResult).isEmpty()
    }

}