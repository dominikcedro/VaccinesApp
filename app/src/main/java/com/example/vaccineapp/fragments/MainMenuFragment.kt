package com.example.vaccineapp.fragments

import android.app.AlertDialog
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
import androidx.lifecycle.lifecycleScope
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.viewmodel.MainMenuViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainMenuFragment : Fragment() {


    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainMenuViewModel by viewModel()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.fetchVaccinationsToConfirm()
            val vaccinationsToConfirm = viewModel.getVaccinationsToConfirm()
            for (vaccination in vaccinationsToConfirm) {
                confirmVaccinationDialog(vaccination)
            }
        }
    }

    companion object {
        fun newInstance() = MainMenuFragment()
    }

    private fun confirmVaccinationDialog(scheduledVaccination: ScheduledVaccinationGetRequest) {
        val builder = AlertDialog.Builder(context)

        val vaccinationName = scheduledVaccination.vaccine.name
        val vaccinationDate = scheduledVaccination.dateTime

        builder.setTitle("Confirm Vaccination")
        builder.setMessage("Did you attend the $vaccinationName vaccination on $vaccinationDate?")

        builder.setPositiveButton("Yes") { dialog, which ->
            viewModel.confirmVaccination(scheduledVaccination)
        }

        builder.setNegativeButton("No") { dialog, which ->
            viewModel.declineVaccination(scheduledVaccination)
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}