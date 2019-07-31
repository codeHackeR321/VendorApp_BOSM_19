package com.example.vendorapp.loginscreen.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
  @SerializedName( "user_id") val id: String,
  @SerializedName( "JWT") val jwt: String,
  @SerializedName( "detail") val message: String

)