package com.example.wanandroid.view.activity.login

import android.content.SharedPreferences
import android.os.Bundle
import com.example.wanandroid.R
import com.example.wanandroid.base.BaseActivity
import com.example.wanandroid.constant.Constant
import com.example.wanandroid.databinding.ActivityRegisterBinding
import com.example.wanandroid.view.activity.MainActivity
import com.example.wanandroid.view.ui.LoadingView
import com.example.wanandroid.viewmodel.WanAndroidViewModel

class Register : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel:WanAndroidViewModel
    private lateinit var loadingView: LoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingView = LoadingView(this,R.style.CustomDialog)
        viewModel = getViewModel(WanAndroidViewModel::class.java)
        initButton()
    }

    //点击事件
    private fun initButton() {
        binding.registerLogin.setOnClickListener {
            myStartActivity(Login::class.java, "登录")
            finish()
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
        }
        //点击注册按钮
        binding.registerButton.setOnClickListener {
            val userName = binding.registerEt1.text.toString()
            val passWord = binding.registerEt2.text.toString()
            val rePassWord = binding.registerEt3.text.toString()
            if(userName.length < 3){
                binding.registerEt1.error = "用户名不能少于三位"
            }

            if (userName.isBlank()) {
                binding.registerEt1.error = "用户名不能为空"
            }

            if (passWord.isBlank()) {
                binding.registerEt2.error = "密码不能为空"
            }

            if (rePassWord.isBlank()) {
                binding.registerEt3.error = "密码不能为空"
            }

            if (userName.isNotBlank() && passWord.isNotBlank() && rePassWord.isNotBlank() && userName.length >= 3) {
                loadingView.show()
                viewModel.requestRegister(userName,passWord,rePassWord)
                viewModel.registerBean.observe(this){
                    loadingView.dismiss()
                    showLog(it.toString())
                    if(it.errorCode == 0){
                        //将账号和密码保存至本地
                        val sharedPreferences: SharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCES_NAME,MODE_PRIVATE)
                        sharedPreferences.edit().apply{
                            putString(Constant.USER_NANE,userName)
                            putString(Constant.PASSWORD,passWord)
                            commit()
                        }
                        myStartActivity(MainActivity::class.java)
                        showToast("注册成功")
                        finish()
                    }else{
                        showToast(it.errorMsg)
                    }
                }
            }
        }
    }
}