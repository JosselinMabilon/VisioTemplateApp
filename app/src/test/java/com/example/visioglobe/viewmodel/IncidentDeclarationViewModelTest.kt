package com.example.visioglobe.viewmodel

import android.net.Uri
import android.text.TextUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.visioglobe.MainCoroutineRule
import com.example.visioglobe.R
import com.example.visioglobe.domain.mapper.FurnitureNameMapper
import com.example.visioglobe.domain.model.Furniture
import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.getOrAwaitValue
import com.example.visioglobe.network.DataState
import com.example.visioglobe.repository.DataStoreRepository
import com.example.visioglobe.repository.IncidentRepository
import com.google.firebase.firestore.DocumentReference
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class IncidentDeclarationViewModelTest {

    ///////////////////////////////////////////////////////////////////////////
    // SYSTEM UNDER TEST (SUT)
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var sut: IncidentDeclarationViewModel

    ///////////////////////////////////////////////////////////////////////////
    // VARIABLES
    ///////////////////////////////////////////////////////////////////////////

    @MockK
    lateinit var dataStore: DataStoreRepository

    @MockK
    lateinit var repository: IncidentRepository

    @MockK
    lateinit var mapper: FurnitureNameMapper

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
        sut = IncidentDeclarationViewModel(dataStore, repository, mapper)
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
    fun setFurnitureSelectedTest() {
        // Given
        val furnitureMock = mockk<Furniture>()

        // When
        sut.setFurnitureSelected(furnitureMock)

        // Then
        assertThat(sut.selectedFurniture.getOrAwaitValue()).isEqualTo(furnitureMock)
    }

    @Test
    fun seFurnitureListTest() {
        // Given
        val furnitureMock = mockk<Furniture>()
        val furnitureListMock = listOf(furnitureMock)
        val furnitureArrayMock = arrayOf(furnitureMock)

        // When
        sut.setFurnitureList(furnitureArrayMock)

        // Then
        assertThat(sut.listFurniture).isEqualTo(furnitureListMock)

    }

    @Test
    fun onReporterNameChangeTest() {
        // Given
        val reporterNameMock = "Joe"

        // When
        sut.onReporterNameChange(reporterNameMock)

        // Then
        assertThat(sut.reporterName.getOrAwaitValue()).isEqualTo(reporterNameMock)
    }

    @Test
    fun onReporterFirstNameChangeTest() {
        // Given
        val reporterFirstNameMock = "Dupont"

        // When
        sut.onReporterFirstnameChange(reporterFirstNameMock)

        // Then
        assertThat(sut.reporterFirstname.getOrAwaitValue()).isEqualTo(reporterFirstNameMock)
    }

    @Test
    fun onEmailChangeTest() {
        // Given
        val emailMock = "joe.dupont@orange.com"

        // When
        sut.onEmailChange(emailMock)

        // Then
        assertThat(sut.email.getOrAwaitValue()).isEqualTo(emailMock)
    }

    @Test
    fun onPhoneNumberChangeTest() {
        // Given
        val phoneNumberMock = "0606060606"

        // When
        sut.onPhoneNumberChange(phoneNumberMock)

        // Then
        assertThat(sut.phoneNumber.getOrAwaitValue()).isEqualTo(phoneNumberMock)
    }

    @Test
    fun onSiteChangeTest() {
        // Given
        val siteMock = "Lyon"

        // When
        sut.onSiteChange(siteMock)

        // Then
        assertThat(sut.site.getOrAwaitValue()).isEqualTo(siteMock)
    }

    @Test
    fun onLocationChangeTest() {
        // Given
        val locationMock = "Equipe XXX"

        // When
        sut.onLocationChange(locationMock)

        // Then
        assertThat(sut.location.getOrAwaitValue()).isEqualTo(locationMock)
    }

    @Test
    fun onTitleChangeTest() {
        // Given
        val titleMock = "Ecran cassé"

        // When
        sut.onTitleChange(titleMock)

        // Then
        assertThat(sut.title.getOrAwaitValue()).isEqualTo(titleMock)
    }

    @Test
    fun onDescriptionEmptyChangeTest() {
        // Given
        val descriptionMock = ""

        // When
        sut.onDescriptionChange(descriptionMock)

        // Then
        assertThat(sut.description.getOrAwaitValue()).isEqualTo(descriptionMock)
    }

    @Test
    fun onDescriptionChangeTest() {
        // Given
        val descriptionMock = "L'écran est cassé"

        // When
        sut.onDescriptionChange(descriptionMock)

        // Then
        assertThat(sut.description.getOrAwaitValue()).isEqualTo(descriptionMock)
    }

    @Test
    fun onDateChangeTest() {
        // Given
        val calendarMock = mockk<Calendar>()

        // When
        sut.onDateChange(calendarMock)

        // Then
        assertThat(sut.incidentDate.getOrAwaitValue()).isEqualTo(calendarMock)
    }

    @Test
    fun resetErrorStateTest() {
        // Given
        // When
        sut.resetErrorState()
        // Then
        assertThat(sut.errorResId.value).isEqualTo(-1)
    }

    @Test
    fun createIncidentEmptyFieldTest() {
        // Given
        val spy = spyk(sut)
        val errorStateMock = R.string.register_empty_field

        every { spy.checkRequiredFields() } returns false
        every { spy.checkEmailPattern() } returns true
        every { spy.checkPhoneNumber() } returns true

        // When
        spy.createIncident()

        // Then
        assertThat(spy.errorResId.value).isEqualTo(errorStateMock)
    }

    @Test
    fun createIncidentWrongEmailTest() {
        // Given
        val spy = spyk(sut)
        val errorStateMock = R.string.login_wrong_email_pattern

        every { spy.checkRequiredFields() } returns true
        every { spy.checkEmailPattern() } returns false
        every { spy.checkPhoneNumber() } returns true

        // When
        spy.createIncident()

        // Then
        assertThat(spy.errorResId.value).isEqualTo(errorStateMock)
    }

    @Test
    fun createIncidentWrongPhoneTest() {
        // Given
        val spy = spyk(sut)
        val errorStateMock = R.string.register_check_phone_length

        every { spy.checkRequiredFields() } returns true
        every { spy.checkEmailPattern() } returns true
        every { spy.checkPhoneNumber() } returns false

        // When
        spy.createIncident()

        // Then
        assertThat(spy.errorResId.value).isEqualTo(errorStateMock)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun createIncidentSuccessTest() {
        // Given
        val spy = spyk(sut)
        val errorStateMock = -1
        val stringMock = "TestValue"
        val uuidMock = "TestUuid"
        val documentReferenceMock = mockk<DocumentReference>()
        val resultSuccessMock = DataState.Success(documentReferenceMock)

        spy._reporterName.value = stringMock
        spy._reporterFirstname.value = stringMock
        spy._site.value = stringMock
        spy._location.value = stringMock
        spy._title.value = stringMock
        spy._incidentDate.value = Calendar.getInstance()
        spy._selectedFurniture.value = Furniture("Other")
        spy._listFurniture = listOf(Furniture("Other"))

        val newIncident = Incident(
            reporterName = sut._reporterName.value!!,
            reporterFirstname = sut._reporterFirstname.value!!,
            email = sut._email.value,
            phoneNumber = sut._phoneNumber.value,
            site = sut._site.value!!,
            time = sut._incidentDate.value!!.timeInMillis,
            title = sut._title.value!!,
            description = sut._description.value!!,
            location = sut._location.value!!,
            furniture = sut._selectedFurniture.value!!,
        )

        every { spy.checkRequiredFields() } returns true
        every { spy.checkEmailPattern() } returns true
        every { spy.checkPhoneNumber() } returns true
        every { spy.getUuid() } returns uuidMock
        coEvery { spy.createImageIncident(uuidMock) } returns Unit
        coEvery { repository.addIncident(newIncident) } returns resultSuccessMock

        // When
        mainCoroutineRule.runBlockingTest {
            spy.createIncident()
            delay(1_000)
        }

        // Then
        assertThat(spy.errorResId.value).isEqualTo(errorStateMock)
        assertThat(sut.incidentDeclarationState.value).isEqualTo(resultSuccessMock)

        coVerify {
            spy.createImageIncident(uuidMock)
            repository.addIncident(newIncident)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun createIncidentFailTest() {
        // Given
        val spy = spyk(sut)
        val errorStateMock = -1
        val stringMock = "TestValue"
        val uuidMock = "TestUuid"
        val errorMock = "TestError"
        val resultFailMock = DataState.Error<DocumentReference>(errorMock)

        spy._reporterName.value = stringMock
        spy._reporterFirstname.value = stringMock
        spy._site.value = stringMock
        spy._location.value = stringMock
        spy._title.value = stringMock
        spy._incidentDate.value = Calendar.getInstance()
        spy._selectedFurniture.value = Furniture("Other")
        spy._listFurniture = listOf(Furniture("Other"))

        val newIncident = Incident(
            reporterName = sut._reporterName.value!!,
            reporterFirstname = sut._reporterFirstname.value!!,
            email = sut._email.value,
            phoneNumber = sut._phoneNumber.value,
            site = sut._site.value!!,
            time = sut._incidentDate.value!!.timeInMillis,
            title = sut._title.value!!,
            description = sut._description.value!!,
            location = sut._location.value!!,
            furniture = sut._selectedFurniture.value!!,
        )

        every { spy.checkRequiredFields() } returns true
        every { spy.checkEmailPattern() } returns true
        every { spy.checkPhoneNumber() } returns true
        every { spy.getUuid() } returns uuidMock
        coEvery { spy.createImageIncident(uuidMock) } returns Unit
        coEvery { repository.addIncident(newIncident) } returns resultFailMock

        // When
        mainCoroutineRule.runBlockingTest {
            spy.createIncident()
            delay(1_000)
        }

        // Then
        assertThat(spy.errorResId.value).isEqualTo(errorStateMock)
        assertThat(spy.incidentDeclarationState.value).isEqualTo(resultFailMock)

        coVerify {
            spy.createImageIncident(uuidMock)
            repository.addIncident(newIncident)
        }

    }

    @ExperimentalCoroutinesApi
    @Test
    fun createImageIncidentSuccessTest() {
        // Given
        val UUIDMock = "MockUUID"
        val uriMock = mockk<Uri>()
        val resultMock = DataState.Success(UUIDMock)

        sut._selectedFurniture.value = Furniture("Other", 0f, -1, uriMock)

        coEvery { repository.saveImage(uriMock, UUIDMock) } returns resultMock

        // When
        mainCoroutineRule.runBlockingTest {
            sut.createImageIncident(UUIDMock)
        }

        // Then
        assertThat(sut.incidentImageState.value).isEqualTo(resultMock)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun createImageIncidentFailTest() {
        // Given
        val UUIDMock = "MockUUID"
        val uriMock = mockk<Uri>()
        val mockError = "ErrorTest"
        val resultErrorMock = DataState.Error<String>(mockError)

        sut._selectedFurniture.value = Furniture("Other", 0f, -1, uriMock)

        coEvery { repository.saveImage(uriMock, UUIDMock) } returns resultErrorMock

        // When
        mainCoroutineRule.runBlockingTest {
            sut.createImageIncident(UUIDMock)
        }

        // Then
        assertThat(sut.incidentImageState.value).isEqualTo(resultErrorMock)
    }

    @Test
    fun checkRequiredFieldFalseTest() {
        // Given
        val stringMock = ""

        // When
        sut._reporterName.value = stringMock
        val result = sut.checkRequiredFields()

        // Then
        assertThat(result).isFalse
    }

    @Test
    fun checkRequiredFieldsTrueTest() {
        // Given
        val stringMock = "TestValue"

        // When
        sut._reporterName.value = stringMock
        sut._reporterFirstname.value = stringMock
        sut._site.value = stringMock
        sut._location.value = stringMock
        sut._title.value = stringMock
        val result = sut.checkRequiredFields()

        // Then
        assertThat(result).isTrue
    }

    @Test
    fun checkEmailPatternFalse() {
        // Given
        val wrongEmail = "joe.dupont@pasorange.com"

        // When
        sut._email.value = wrongEmail
        val result = sut.checkEmailPattern()

        // Then
        assertThat(result).isFalse
    }

    @Test
    fun checkEmailPatternTrue() {
        // Given
        val email = "joe.dupont@orange.com"

        // When
        sut._email.value = email
        val result = sut.checkEmailPattern()

        // Then
        assertThat(result).isTrue
    }

    @Test
    fun checkPhoneNumberFalseDigitTest() {
        // Given
        val wrongPhoneNumberDigit = "nodigit"

        // When
        sut._phoneNumber.value = wrongPhoneNumberDigit
        val result = sut.checkPhoneNumber()

        // Then
        assertThat(result).isFalse
    }

    @Test
    fun checkPhoneNumberFalseLengthTest() {
        // Given
        val wrongPhoneNumberLength = "069546"

        // When
        sut._phoneNumber.value = wrongPhoneNumberLength
        val result = sut.checkPhoneNumber()

        // Then
        assertThat(result).isFalse
    }

    @Test
    fun checkPhoneNumberTrue() {
        mockkStatic(TextUtils::class)
        // Given
        val phoneNumber = "0700000000"

        every { TextUtils.isDigitsOnly(any<String>()) } returns true
        sut._phoneNumber.value = phoneNumber

        // When
        val res = sut.checkPhoneNumber()

        // Then
        assertThat(res).isEqualTo(true)
    }

    @Test
    fun resetBackPressedStateTest() {
        // Given
        // When
        sut.resetBackPressedState()
        // Then
        assertThat(sut.backPressedState.value).isEqualTo(false)
    }

    @Test
    fun initBackPressedStateTest() {
        // Given
        // When
        sut.initBackPressedState()
        // Then
        assertThat(sut.backPressedState.value).isEqualTo(true)
    }
}