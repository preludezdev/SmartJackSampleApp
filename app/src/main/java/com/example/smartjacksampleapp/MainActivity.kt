package com.example.smartjacksampleapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*

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

    private fun showToastMessage(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                showToastMessage("취소되었습니다.")
            } else {
                showToastMessage("스캔 이미지: ${result.contents}")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
