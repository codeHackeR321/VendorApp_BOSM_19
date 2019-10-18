package com.example.vendorapp.shared

import com.example.vendorapp.neworderscreen.model.IncompleteOrderStatus

sealed class UIState {

    object ShowLoadingState : UIState()

    object ShowInitialState : UIState()

    object ShowWorkingState : UIState()

    object NoInternetConnection: UIState()

    object ErrorRoom: UIState()

    object GoToMainScreen : UIState()

    object LogoutSuccess: UIState()

    data class LogoutFail(val message: String): UIState()

    data class ErrorState(val message:String): UIState()

    data class ErrorStateChangeStatus(val message:String): UIState()

    data class ErrorStateChangeStatusMenuActivity(val message:String): UIState()

    data class SuccessStateChangeStatus(val message:String): UIState()

    data class SuccessStateChangeStatusMenuActivity(val message:String): UIState()

    data class SuccessStateFetchingOrders(val message:String,/*val orderId :Int,*/val incompleteOrderList: List<IncompleteOrderStatus>): UIState()

    data class ErrorStateFetchingOrders(val message:String/*,val orderId :Int*/,val incompleteOrderList: List<IncompleteOrderStatus>): UIState()
}