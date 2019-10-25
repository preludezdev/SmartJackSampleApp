package com.example.smartjacksampleapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartjacksampleapp.R
import com.example.smartjacksampleapp.network.RetrofitHelper
import com.example.smartjacksampleapp.network.vo.SmartJackRequest
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val intentIntegrator by lazy {
        IntentIntegrator(this)
            .setCaptureActivity(ZxingActivity::class.java)
            .setOrientationLocked(false)
            .setPrompt("사각형 안에 QR코드 또는 바코드를 정확히 넣어주세요.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initEvent()
    }

    private fun initEvent() {
        bt_enter.setOnClickListener {
            intentIntegrator.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data)
        } else {
            val contents = result.contents

            if (contents == null) {
                showToastMessage("취소 되었습니다.")
            } else {
                uploadData(contents)
            }
        }
    }

    private fun uploadData(jsonStr: String) {
        val data = Gson().fromJson(jsonStr, SmartJackRequest::class.java)

        if (!isValidateData(data)) {
            showToastMessage("올바른 데이터 양식이 아닙니다.")
        } else {
            RetrofitHelper
                .getInstance()
                .apiService
                .postData(data)
                .enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        showToastMessage("업로드 실패")
                        Log.d("test", t.message ?: "null")
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            showToastMessage("업로드 성공")
                        } else {
                            showToastMessage("업로드 실패")
                            Log.d("test", "${response.errorBody()}")
                        }
                    }
                })
        }
    }

    private fun isValidateData(smartJackRequest: SmartJackRequest): Boolean {
        with(smartJackRequest) {
            if (value.isEmpty() || name.isEmpty()) {
                return false
            }

            val phone = smartJackRequest.phone.split("-")

            return when {
                phone.size != 3 -> false
                phone[0] != "010" -> false
                (phone[1].length != 3) && (phone[1].length != 4) -> false
                phone[2].length != 4 -> false
                phone[1].toIntOrNull() == null -> false
                phone[2].toIntOrNull() == null -> false
                else -> true
            }
        }
    }

    private fun showToastMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
