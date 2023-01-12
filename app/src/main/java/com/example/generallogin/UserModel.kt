package com.example.generallogin



data class UserModel(
    var id : String ?= null,
    var pw : String ?= null
)

data class LoginBackendResponse(
    val code : String,
    //200: 성공, 300,400: 에러
    val message : String,
    val token : String
)


