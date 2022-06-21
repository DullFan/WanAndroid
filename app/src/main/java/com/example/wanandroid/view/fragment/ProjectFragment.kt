package com.example.wanandroid.view.fragment

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanandroid.R
import com.example.wanandroid.databinding.FragmentProjectBinding
import com.google.android.material.badge.BadgeDrawable

class ProjectFragment : Fragment() {
    private lateinit var binding:FragmentProjectBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProjectBinding.inflate(layoutInflater,container,false)
        binding.projectLottie.apply {
            setAnimation(R.raw.lottie_loading)
            loop(true)
            playAnimation()
        }
        return binding.root
    }
}