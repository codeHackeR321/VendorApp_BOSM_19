package com.example.vendorapp.loginscreen.view

sealed class LoginUIState {

    object ShowLoadingState : LoginUIState()

    object ShowWorkingState : LoginUIState()

    object GoToMainScreen : LoginUIState()

    data class ErrorState(val message:String): LoginUIState()
}