package com.example.vaccineapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextClock
import androidx.fragment.app.Fragment
import com.example.vaccineapp.databinding.FragmentDashboardBinding
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Color
import android.widget.TextView


class MainMenuFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val clock: TextClock = binding.clock
        clock.format24Hour = "HH:mm"

        val paint = clock.paint
        val width = paint.measureText(clock.text.toString())

        val textShader: Shader = LinearGradient(0f, 0f, width, clock.textSize, intArrayOf(
            Color.parseColor("#F97C3C"),
            Color.parseColor("#FDB54E"),
            Color.parseColor("#64B678"),
            Color.parseColor("#478AEA"),
            Color.parseColor("#8446CC"),
        ), null, Shader.TileMode.CLAMP)

        clock.paint.shader = textShader

        return binding.root
    }

    companion object {
        fun newInstance() = MainMenuFragment()
    }
}