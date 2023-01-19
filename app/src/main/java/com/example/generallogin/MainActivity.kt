package com.example.generallogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import android.widget.Toast
import com.example.generallogin.databinding.ActivityMainBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOError
import java.io.IOException

class MainActivity : AppCompatActivity() {

    var token_value : String ?= null
    val bearer = "Bearer "
    var token: String ?= null

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // ==로그인 버튼 클릭 시==
        binding.btnLogin.setOnClickListener {
            val id = binding.editTVId.text.toString().trim()//trim : 문자열 공백제거
            val pw = binding.editTVPw.text.toString().trim()
            //var state : Boolean

            saveData(id, pw)//db (shared preference)에 데이터 저장 (자동 로그인 용)

            // == 백엔드 통신 부분 ==
            val api = Api.create()
            val data = UserModel(id, pw)

            api.userLogin(data).enqueue(object : Callback<LoginBackendResponse> {

                override fun onResponse(
                    call: Call<LoginBackendResponse>,
                    response: Response<LoginBackendResponse>
                ) {
                    Log.d("로그인 통신 성공",response.toString())
                    Log.d("로그인 통신 성공", response.body().toString())

                    val body = response.body();
                    if (body != null) {
                        token_value = body.result?.accessToken
                        Log.d("로그인--- 토큰 타입", "{${token_value.toString()}}")
                    }
                    Log.d("로그인 통신 성공", "{${token_value}}")
                    Log.d("로그인 토큰 벨류", "$token_value")
                    token = bearer + token_value
                    Log.d("로그인 내가 보낸거", token!!)

                    when (response.code()) {
                        200 -> {
                            saveData(id, pw)
                        }
                        405 -> Toast.makeText(this@MainActivity, "로그인 실패 : 아이디나 비번이 올바르지 않습니다", Toast.LENGTH_LONG).show()
                        500 -> Toast.makeText(this@MainActivity, "로그인 실패 : 서버 오류", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
                    // 실패
                    Log.d("로그인 통신 실패",t.message.toString())
                    Log.d("로그인 통신 실패","fail")
                }
            })
        }


        // ==get 버튼 클릭 시==
        binding.btnGet.setOnClickListener {
            val email : String ?= null
            val username : String ?= null
            val nickname : String ?= null
            val birth : String ?= null

            // == 백엔드 통신 부분 ==
            val api = Api.create()
            val data = UserDetailResponse.Result(email, username, nickname, birth)

            api.PostUserToken(UserToken(accessToken = token!!)).enqueue(object : Callback<LoginBackendResponse> {
                override fun onResponse(
                    call: Call<LoginBackendResponse>,
                    response: Response<LoginBackendResponse>
                ) {
                    Log.d("로그인 내가 보낸거", token!!)

                    Log.d("로그인 GET 통신 성공", response.toString())
                    Log.d("로그인 GET 통신 성공", response.body().toString())
                }

                override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
                    Log.d("로그인 GET 통신 실패",t.message.toString())
                    Log.d("로그인 GET 통신 실패","fail")
                }


            })

            api.GetUserInfo(token!!).enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    Log.d("로그인 POST 통신 성공",response.toString())
                    Log.d("로그인 POST 통신 성공", response.body().toString())
                    binding.dataReceived.text = response.body().toString()
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    // 실패
                    Log.d("로그인 POST 통신 실패",t.message.toString())
                    Log.d("로그인 POST 통신 실패","fail")
                }
            })
        }
    }
    fun saveData( id : String, pw : String){
        val prefID = getSharedPreferences("userID", MODE_PRIVATE)
        val prefPW = getSharedPreferences("userPW", MODE_PRIVATE)
        val editID = prefID.edit()
        val editPW = prefPW.edit()
        editID.putString("id", id)
        editPW.putString("pw", pw)
        editID.apply()//save
        editPW.apply()//save
        Log.d("로그인 데이터", "saved")
    }
}