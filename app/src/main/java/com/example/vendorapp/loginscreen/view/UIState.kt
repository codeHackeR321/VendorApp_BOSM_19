package com.example.vendorapp.loginscreen.view

sealed class UIState {

    object ShowLoadingState : UIState()

    object ShowInitialState : UIState()

    object ShowWorkingState : UIState()

    object GoToMainScreen : UIState()

    data class SuccessState(val message:String): UIState()

    data class ErrorState(val message:String): UIState()
}