package com.example.generallogin

data class UserModel(
    var email : String,
    var password: String,
)

data class UserToken(
    var accessToken : String,
)

data class LoginBackendResponse(
    val isSuccess : Boolean ?= null,
    //200: 성공, 300,400: 에러'
    val responseCode : String ?= null,
    val responseMessage : String ?= null,
    val result : Result ?= null,//하나의 객체 List XX
){
    data class Result(
        val email : String ?= null,
        val accessToken: String ?= null,
        val refreshToken: String ?= null
    )
}


// [GET] /app/users/{id}
data class UserDetailResponse(
    val isSuccess : Boolean ?= null,
    //200: 성공, 300,400: 에러'
    val responseCode : String ?= null,
    val responseMessage : String ?= null,
    val result : Result ?= null,//하나의 객체 List XX
){
    data class Result(
        val email : String ?= null,
        val username: String ?= null,
        val nickname : String ?= null,
        val birth : String ?= null
    )
}

