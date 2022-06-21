package com.example.wanandroid.view.activity.login

import android.content.SharedPreferences
import android.os.Bundle
import com.example.wanandroid.R
import com.example.wanandroid.base.BaseActivity
import com.example.wanandroid.constant.Constant
import com.example.wanandroid.constant.Constant.PASSWORD
import com.example.wanandroid.constant.Constant.SHARED_PREFERENCES_NAME
import com.example.wanandroid.constant.Constant.USER_NANE
import com.example.wanandroid.databinding.ActivityLoginBinding
import com.example.wanandroid.view.activity.MainActivity
import com.example.wanandroid.view.ui.LoadingView
import com.example.wanandroid.viewmodel.LiveDataBus
import com.example.wanandroid.viewmodel.WanAndroidViewModel

class Login : BaseActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var viewModel: WanAndroidViewModel
    private lateinit var loadingView:LoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingView = LoadingView(this,R.style.CustomDialog)
        viewModel = getViewModel(WanAndroidViewModel::class.java)
        initButton()
    }

    private fun initButton() {
        binding.loginRegister.setOnClickListener {
            myStartActivity(Register::class.java, "注册")
            finish()
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
        }

        //点击注册按钮
        binding.loginButton.setOnClickListener {
            val userName = binding.loginEt1.text.toString()
            val passWord = binding.loginEt2.text.toString()
            if (userName.isBlank()) {
                binding.loginEt1.error = "用户名不能为空"
            }

            if (passWord.isBlank()) {
                binding.loginEt2.error = "密码不能为空"
            }

            if (userName.isNotBlank() && passWord.isNotBlank()) {
                loadingView.show()
                viewModel.requestLogin(userName,passWord)
                viewModel.loginBean.observe(this){
                    loadingView.dismiss()
                    if(it.errorCode == 0){
                        //将账号和密码保存至本地
                        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,MODE_PRIVATE)
                        sharedPreferences.edit().apply{
                            putString(USER_NANE,userName)
                            putString(PASSWORD,passWord)
                            commit()
                        }
                        myStartActivity(MainActivity::class.java)
                        showToast("登录成功")
                        finish()
                    }else{
                        showToast(it.errorMsg)
                    }
                }
            }
        }
    }
}