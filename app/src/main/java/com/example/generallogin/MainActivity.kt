package com.example.generallogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.generallogin.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

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