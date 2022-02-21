package com.example.visioglobe.viewmodel

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visioglobe.R
import com.example.visioglobe.constants.EMAIL_PATTERN
import com.example.visioglobe.constants.PHONE_NUMBER_LENGTH
import com.example.visioglobe.domain.mapper.FurnitureNameMapper
import com.example.visioglobe.domain.model.Furniture
import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.network.DataState
import com.example.visioglobe.repository.DataStoreRepository
import com.example.visioglobe.repository.IncidentRepository
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class IncidentDeclarationViewModel @Inject constructor(
    private val dataStore: DataStoreRepository,
    private val incidentRepository: IncidentRepository,
    val mapper: FurnitureNameMapper
) :
    ViewModel() {

    @VisibleForTesting
    val _selectedFurniture = MutableLiveData<Furniture>()
    val selectedFurniture: LiveData<Furniture>
        get() = _selectedFurniture

    @VisibleForTesting
    var _listFurniture: List<Furniture> = emptyList()
    val listFurniture: List<Furniture>
        get() = _listFurniture

    @VisibleForTesting
    var _incidentDate: MutableLiveData<Calendar> =
        MutableLiveData(Calendar.getInstance())
    val incidentDate: LiveData<Calendar>
        get() = _incidentDate

    @VisibleForTesting
    val _reporterName = MutableLiveData("")
    val reporterName: LiveData<String> = _reporterName

    @VisibleForTesting
    val _reporterFirstname = MutableLiveData("")
    val reporterFirstname: LiveData<String> = _reporterFirstname

    @VisibleForTesting
    val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    @VisibleForTesting
    val _phoneNumber = MutableLiveData("")
    val phoneNumber: LiveData<String> = _phoneNumber

    @VisibleForTesting
    val _site = MutableLiveData("")
    val site: LiveData<String> = _site

    @VisibleForTesting
    val _location = MutableLiveData("")
    val location: LiveData<String> = _location

    @VisibleForTesting
    val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    @VisibleForTesting
    val _description = MutableLiveData("")
    val description: LiveData<String> = _description

    var errorResId: MutableState<Int> = mutableStateOf(NO_ERROR)

    var backPressedState: MutableState<Boolean> = mutableStateOf(false)

    private var _incidentDeclarationState: MutableState<DataState<DocumentReference>> =
        mutableStateOf(DataState.Nothing())
    val incidentDeclarationState: State<DataState<DocumentReference>>
        get() = _incidentDeclarationState

    @VisibleForTesting
    var _incidentImageState: MutableState<DataState<String>> =
        mutableStateOf(DataState.Nothing())
    val incidentImageState: State<DataState<String>>
        get() = _incidentImageState

    fun getUserInfo(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val userInfoStored = dataStore.getUserInfo(defaultValue = "", context = context)
            _email.postValue(userInfoStored.email)
            _phoneNumber.postValue(userInfoStored.phone)
            _reporterName.postValue(userInfoStored.lastName)
            _reporterFirstname.postValue(userInfoStored.firstName)
        }
    }

    fun onReporterNameChange(reporterName: String) {
        _reporterName.value = reporterName
    }

    fun onReporterFirstnameChange(reporterFirstname: String) {
        _reporterFirstname.value = reporterFirstname
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun onSiteChange(site: String) {
        _site.value = site
    }

    fun onLocationChange(location: String) {
        _location.value = location
    }

    fun onTitleChange(title: String) {
        _title.value = title
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onDateChange(calendar: Calendar) {
        _incidentDate.value = calendar
    }

    fun setFurnitureSelected(furnitureArg: Furniture) {
        _selectedFurniture.value = furnitureArg
    }

    fun setFurnitureList(listFurnitureArg: Array<Furniture>) {
        _listFurniture = listFurnitureArg.toList()
    }

    fun resetErrorState() {
        errorResId.value = NO_ERROR
    }

    fun resetBackPressedState() {
        backPressedState.value = false
    }

    fun initBackPressedState() {
        backPressedState.value = true
    }

    fun createIncident() {
        when {
            !checkRequiredFields() -> {
                errorResId.value = R.string.register_empty_field
            }
            !checkEmailPattern() -> {
                errorResId.value = R.string.login_wrong_email_pattern
            }
            !checkPhoneNumber() -> {
                errorResId.value = R.string.register_check_phone_length
            }
            else -> {
                errorResId.value = NO_ERROR

                val newIncident = Incident(
                    reporterName = reporterName.value!!,
                    reporterFirstname = reporterFirstname.value!!,
                    email = email.value,
                    phoneNumber = phoneNumber.value,
                    site = site.value!!,
                    time = incidentDate.value!!.timeInMillis,
                    title = title.value!!,
                    description = description.value!!,
                    location = location.value!!,
                    furniture = selectedFurniture.value!!,
                )

                val imageUuid = getUuid()

                viewModelScope.launch(Dispatchers.IO) {
                    createImageIncident(imageUuid)
                    if (_incidentImageState.value is DataState.Success) {
                        newIncident.imageId = imageUuid
                    }
                    _incidentDeclarationState.value =
                        incidentRepository.addIncident(newIncident = newIncident)
                }
            }
        }
    }

    @VisibleForTesting
    suspend fun createImageIncident(imageUuid: String) {
        selectedFurniture.value!!.imageUri?.let {
            _incidentImageState.value =
                incidentRepository.saveImage(it, imageUuid)
        }
    }

    @VisibleForTesting
    fun getUuid(): String {
        return UUID.randomUUID().toString().uppercase()
    }

    @VisibleForTesting
    fun checkRequiredFields(): Boolean {
        return !(reporterFirstname.value.isNullOrEmpty() || reporterName.value.isNullOrEmpty() || site.value.isNullOrEmpty() || title.value.isNullOrEmpty() || location.value.isNullOrEmpty())
    }

    @VisibleForTesting
    fun checkEmailPattern(): Boolean {
        return (email.value.isNullOrEmpty() || email.value!!.matches(EMAIL_PATTERN.toRegex()))
    }

    @VisibleForTesting
    fun checkPhoneNumber(): Boolean {
        return (phoneNumber.value.isNullOrEmpty() || ((phoneNumber).value!!.isDigitsOnly() && phoneNumber.value!!.length == PHONE_NUMBER_LENGTH))
    }

    companion object {
        const val NO_ERROR = -1
    }

}
