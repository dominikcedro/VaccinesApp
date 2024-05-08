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
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentAdminDashboardBinding
import com.example.vaccineapp.viewmodel.LoginViewModel
import com.example.vaccineapp.viewmodel.MainMenuViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Fragment for the dashboard.
 */
class AdminDashboardFragment : Fragment() {


    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainMenuViewModel by viewModel()
    val loginViewModel: LoginViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)

        val clock: TextClock = binding.clock
        clock.format24Hour = "HH:mm"

        val paint = clock.paint
        val width = paint.measureText(clock.text.toString())

        val textShader: Shader = LinearGradient(0f, 0f, width, clock.textSize, intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.redAccent),
            ContextCompat.getColor(requireContext(), R.color.orangeAccent),
        ), null, Shader.TileMode.CLAMP)

        clock.paint.shader = textShader

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        }
    }