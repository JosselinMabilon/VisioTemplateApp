package com.example.visioglobe.manager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.visioglobe.MainCoroutineRule
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.manager.impl.AdminManagerImpl
import com.example.visioglobe.network.DataState
import com.example.visioglobe.repository.AdminRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AdminManagerTest {

    @MockK
    lateinit var adminRepository: AdminRepository

    private lateinit var sut: AdminManager

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
        sut = AdminManagerImpl(adminRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    ///////////////////////////////////////////////////
    //////      TEST DATA
    ///////////////////////////////////////////////////

    private val userTest = User(
        "56vhz5hfjz56",
        "magali.test@orange.com",
        "Magali",
        "Test",
        "admin",
        "06000000000",
        "Lyon"
    )

    val userTest2 = User(
        "56vhz5hfjz56",
        "josselin.test@orange.com",
        "Josselin",
        "Test",
        "user",
        "06000000000",
        "Lyon"
    )

    private val userListTest = listOf(userTest, userTest2)

    ///////////////////////////////////////////////////
    //////      TEST BEGIN
    ///////////////////////////////////////////////////

    @ExperimentalCoroutinesApi
    @Test
    fun getUsersOrderByPermissionLoadingTest() {
        // Given
        val dataStateMock = mockk<DataState.Loading<List<User>>>()
        val repositoryResponseMock = flowOf(dataStateMock)

        coEvery { adminRepository.getUsers() } returns repositoryResponseMock

        // When
        var result: DataState<List<User>> = DataState.Nothing<List<User>>()
        mainCoroutineRule.runBlockingTest {
            sut.getUsersOrderByPermission().collect { result = it }
        }

        // Then
        assertThat(result).isEqualTo(dataStateMock)
        coVerify { adminRepository.getUsers() }

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getUsersOrderByPermissionErrorTest() {
        // Given
        val dataStateMock = mockk<DataState.Error<List<User>>>()
        val repositoryResponseMock = flowOf(dataStateMock)

        coEvery { adminRepository.getUsers() } returns repositoryResponseMock

        // When
        var result: DataState<List<User>> = DataState.Nothing<List<User>>()
        mainCoroutineRule.runBlockingTest {
            sut.getUsersOrderByPermission().collect { result = it }
        }

        // Then
        assertThat(result).isEqualTo(dataStateMock)
        coVerify { adminRepository.getUsers() }

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getUsersOrderByPermissionSuccessTest() {
        // Given
        val userListSorted = userListTest.sortedBy { it.permission }
        val dataStateMock = DataState.Success(userListSorted)
        val repositoryResponseMock = flowOf(dataStateMock)

        coEvery { adminRepository.getUsers() } returns repositoryResponseMock

        // When
        var dataResult: List<User> = emptyList()
        mainCoroutineRule.runBlockingTest {
            val flowResult: Flow<DataState.Success<List<User>>> =
                sut.getUsersOrderByPermission() as Flow<DataState.Success<List<User>>>
            flowResult.collect { dataResult = it.data }
        }
        // Then
        assertThat(dataResult).isEqualTo(userListSorted)
    }

}