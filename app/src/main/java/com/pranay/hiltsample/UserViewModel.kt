package com.pranay.hiltsample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pranay.hiltsample.base.BaseViewModel
import com.pranay.hiltsample.api.model.User
import com.pranay.hiltsample.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val userRepository: UserRepository,val helloPrinter: HelloPrinter) : BaseViewModel(){

    private val userData = MutableLiveData<DisplayData>()
    fun getData(): LiveData<DisplayData> = userData


    private val printerData = MutableLiveData<String>()
    fun _PrinterData(): LiveData<String> = printerData

    fun fetchUser(name: String) = viewModelScope.launch{
        printerData.postValue(helloPrinter.print())
        kotlin.runCatching{
            val fetchRepository = userRepository.fetchRepository(name)
            userData.value = getUserDisplayData(fetchRepository)
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun getUserDisplayData(user: User): DisplayData = DisplayData(name = user.name?:"", avatarUrl = user.avatarUrl?:"")

    data class DisplayData(val name :String = "",val avatarUrl:String = "")
}